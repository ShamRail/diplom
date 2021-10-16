package ru.ugasu.app.service.build;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.repo.BuildRepository;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.ConfigService;
import ru.ugasu.app.service.build.logger.BuildLogger;
import ru.ugasu.app.service.io.archive.DecompressService;
import ru.ugasu.app.service.io.load.AppRepositoryLoader;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class CallableBuildTask implements BuildTask, Callable<Build> {

    private Project project;

    private Build build;

    private BuildLogger buildLogger;

    @Autowired
    private AppRepositoryLoader appRepositoryLoader;

    @Autowired
    @Qualifier("zipDecompressService")
    private DecompressService decompressService;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private ConfigService configService;

    @Value("${app.projects.build.dockerlogpath}")
    private String dockerLogDir;

    public CallableBuildTask(Project project,
                             Build build,
                             BuildLogger buildLogger) {
        this.project = project;
        this.build = build;
        this.buildLogger = buildLogger;
    }

    @Override
    public Build call() throws Exception {
        return build(project);
    }

    @Override
    public Build build(Project project) {
        Optional<Path> archivePath = uploadProject();
        if (archivePath.isEmpty()) {
            buildLogger.flush();
            return build;
        }
        Optional<Path> sourcePath = decompress(archivePath.get());
        if (sourcePath.isEmpty()) {
            buildLogger.flush();
            return build;
        }
        Optional<String> imageID = buildImage(sourcePath.get());
        if (imageID.isEmpty()) {
            buildLogger.flush();
            return build;
        }
        updateBuildInDB(BuildStatus.BUILT, "Build succeed");
        project.setImageID(imageID.get());
        project.setBuildStatus(BuildStatus.BUILT);
        projectRepository.save(project);
        buildLogger.flush();
        return build;
    }

    void updateBuildInDB(BuildStatus buildStatus, String message) {
        build.setEndAt(LocalDateTime.now());
        build.setBuildStatus(buildStatus);
        build.setMessage(message);
        buildRepository.save(build);
        projectRepository.updateStatusById(build.getProject().getId(), buildStatus);
    }

    private Optional<Path> uploadProject() {
        Optional<Path> archivePath = Optional.empty();
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile(String.valueOf(System.currentTimeMillis()), ".zip");
            buildLogger.append(String.format("Create temp file at %s", tempFile.toString()));
            buildLogger.append(String.format("Try to download from %s", project.getSourceCodeURL()));
            updateBuildInDB(BuildStatus.DOWNLOADING, "Download project");
            appRepositoryLoader.load(project.getSourceCodeURL(), tempFile.toString());
            buildLogger.append("Download is complete");
            archivePath = Optional.of(tempFile);
        } catch (Exception e) {
            String message = String.format(
                    "Failed to download project. Please, check your path or url. %s is unreachable",
                    project.getSourceCodeURL()
            );
            buildLogger.append(message);
            updateBuildInDB(BuildStatus.FALLEN, message);
            buildLogger.append("Delete temp file");
            try {
                Files.deleteIfExists(tempFile);
            } catch (Exception ex) {
                buildLogger.append(String.format(
                        "Failed to delete temp file at path %s. %s, %s",
                        tempFile.toString(), ex.getClass(), ex.getMessage())
                );
            }
        }
        return archivePath;
    }

    private Optional<Path> decompress(Path archive) {
        Optional<Path> sourcePath = Optional.empty();
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("project" + System.currentTimeMillis());
            buildLogger.append(String.format("Create temp dir at %s", tempDir.toString()));
            buildLogger.append(String.format("Try to decompress %s", tempDir.toString()));
            decompressService.decompress(archive, tempDir);
            buildLogger.append("Decompressing complete");
            sourcePath = Optional.of(tempDir);
        } catch (Exception e) {
            String message = "Failed to decompress archive. Please, check archive data is valid";
            updateBuildInDB(BuildStatus.FALLEN, message);
            buildLogger.append(String.format("%s. Exception info: %s. %s", message, e.getClass(), e.getMessage()));
            buildLogger.append("Delete temp dir");
            try {
                FileUtils.deleteDirectory(tempDir.toFile());
            } catch (Exception ex) {
                buildLogger.append(String.format("Failed to delete temp dir. %s, %s", ex.getClass(), ex.getMessage()));
            }
        }
        return sourcePath;
    }

    private Optional<String> buildImage(Path sourcePath) {
        Optional<String> imageID = Optional.empty();
        try {

            updateBuildInDB(BuildStatus.PREPARING, "Configuring docker");

            if (project.getArchiveInnerDir() != null && !project.getArchiveInnerDir().isEmpty()) {
                buildLogger.append(String.format("cd to %s", project.getArchiveInnerDir()));
                sourcePath = Path.of(sourcePath.toString(), project.getArchiveInnerDir());
            }

            Optional<Path> dockerFilePath = configService.uploadDockerfile(project.getConfigurationId());
            String dockerFile = "";
            if (dockerFilePath.isEmpty()) {
                String message = "Failed to download Dockerfile";
                buildLogger.append(message);
                updateBuildInDB(BuildStatus.FALLEN, message);
                return Optional.empty();
            }
            dockerFile = dockerFilePath.get().toAbsolutePath().toString();

            buildLogger.append(String.format("Copy Dockerfile from %s ", dockerFile));
            Path target = sourcePath.resolve("Dockerfile");
            if (Files.exists(target)) {
                buildLogger.append("Override existed Dockerfile");
                Files.delete(target);
            }
            Files.copy(Path.of(dockerFile), target);
            buildLogger.append("Dockerfile copied");

            buildLogger.append("Preparing args");
            BuildImageCmd buildImageCmd = putArgs(
                    dockerClient.buildImageCmd(sourcePath.toFile()).withNoCache(true)
            );
            buildLogger.append("Args prepared. Start building");

            updateBuildInDB(BuildStatus.BUILDING, "Start building");

            var file = Path.of(dockerLogDir, System.currentTimeMillis() + ".txt");
            Files.createFile(file);
            try (PrintStream logOut = new PrintStream(file.toFile())) {
                imageID = Optional.of(buildImageCmd.exec(new LoggedBuildResponseItem(logOut)).awaitImageId());
            }
            build.setDockerLogPath(file.toString());
            buildLogger.append(String.format("Building complete %s", imageID.get()));

        } catch (Exception ex) {
            String message = String.format(
                    "Failed while building Docker image. Check your build command and configuration. Exception info: %s, %s",
                    ex.getClass(), ex.getMessage()
            );
            updateBuildInDB(BuildStatus.FALLEN, message);
            buildLogger.append(message);
            imageID = Optional.empty();
        } finally {
            buildLogger.append("Delete source code dir");
            try {
                FileUtils.deleteDirectory(sourcePath.toFile());
            } catch (Exception e) {
                buildLogger.append(String.format(
                        "Failed to delete source code dir at path %s. %s, %s",
                        sourcePath.toString(), e.getClass(), e.getMessage())
                );
            }
        }
        return imageID;
    }

    private BuildImageCmd putArgs(BuildImageCmd buildImageCmd) {
        buildImageCmd.withBuildArg("BUILD_COMMAND", project.getBuildCommand());
        return buildImageCmd;
    }

}
