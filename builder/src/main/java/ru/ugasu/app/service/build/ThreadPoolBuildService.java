package ru.ugasu.app.service.build;

import com.github.dockerjava.api.DockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.repo.BuildRepository;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.build.logger.BuildLogger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@Transactional
public abstract class ThreadPoolBuildService implements BuildService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolBuildService.class.getSimpleName());

    @Value("${app.projects.build.logpath}")
    private String logPath;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private DockerClient dockerClient;

    @PostConstruct
    public void initDirs() {
        try {
            Files.createDirectories(Path.of(logPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @PreDestroy
    public void killPool() {
        executorService.shutdown();
    }

    @Override
    public Build start(Project project) {
        String buildPath = Path.of(logPath, UUID.randomUUID().toString()).toString() + ".txt";
        Build build = new Build(project, "Start build project", buildPath, BuildStatus.STARTED);
        build.setStartAt(LocalDateTime.now());

        deletePreviousImageAndDbData(project);
        buildRepository.save(build);

        projectRepository.updateStatusById(project.getId(), BuildStatus.STARTED);
        executorService.submit(buildTask(
                project, build, buildLogger(buildPath)
        ));
        return build;
    }

    @Override
    public Optional<Build> getBuild(Project project) {
        return buildRepository.findByProject(project);
    }

    @Override
    public void removeProject(Project project) {
        LOGGER.info("Remove project with id {}", project.getId());
        deletePreviousImageAndDbData(project);
        projectRepository.delete(project);
    }

    @Lookup
    protected abstract CallableBuildTask buildTask(Project project, Build build, BuildLogger buildLogger);

    @Lookup
    protected abstract BuildLogger buildLogger(String logPath);

    private void deletePreviousImageAndDbData(Project project) {
        try {
            LOGGER.info("Remove previous image");
            if (project.getImageID() != null) {
                dockerClient.removeImageCmd(project.getImageID())
                        .withForce(true)
                        .exec();
            }
        } catch (Exception e) {
            LOGGER.info("Failed to remove image. Image with id {} not found", project.getImageID());
        }
        var buildOptional = buildRepository.findByProject(project);
        if (buildOptional.isPresent()) {
            LOGGER.info("Delete build logs");
            try {
                Files.delete(Path.of(buildOptional.get().getLogPath()));
            } catch (IOException e) {
                LOGGER.info("Failed to remove build logs. File is not found.");
            }
        }
        LOGGER.info("Remove previous builds from DB");
        buildRepository.deleteByProject(project);
    }

}
