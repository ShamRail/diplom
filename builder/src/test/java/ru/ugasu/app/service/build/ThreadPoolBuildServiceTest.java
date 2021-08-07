package ru.ugasu.app.service.build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.repo.BuildRepository;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.ConfigService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThreadPoolBuildServiceTest {

    static String testDataPath;

    static {
        try {
            testDataPath = Path.of("./", "src", "test", "data").toRealPath().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BuildService buildService;

    @Autowired
    BuildRepository buildRepository;

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {

        @Bean
        ConfigService configService() {
            return new ConfigService() {

                final Map<Integer, Optional<Path>> dockerFiles = Map.of(
                        1, Optional.of(Path.of(testDataPath, "docker", "dockerfiles", "java", "Dockerfile")),
                        2, Optional.of(Path.of(testDataPath, "docker", "dockerfiles", "c", "Dockerfile")),
                        3, Optional.of(Path.of(testDataPath, "docker", "dockerfiles", "cpp", "Dockerfile")),
                        4, Optional.of(Path.of(testDataPath, "docker", "dockerfiles", "csharp", "Dockerfile"))
                );

                @Override
                public Optional<Path> uploadDockerfile(Integer configID) {
                    return dockerFiles.get(configID);
                }
            };
        }

    }

    @Test
    public void buildOnlyJavaProject() throws InterruptedException, IOException {
        Project project = createJavaProject(111);
        Build build = buildService.start(project);
        TimeUnit.SECONDS.sleep(30);
        assertProject(project, build);
    }

    @Test
    public void whenBuildMultipleProjects() throws InterruptedException, IOException {
        Project java = createJavaProject(1111);
        Project c = createCProject();
        Project cpp = createCPPProject();
        Project cs = createCSharpProject();

        Build javaBuild = buildService.start(java);
        Build cBuild = buildService.start(c);
        Build cppBuild = buildService.start(cpp);
        Build csBuild = buildService.start(cs);

        TimeUnit.SECONDS.sleep(50);

        assertProject(java, javaBuild);
        assertProject(c, cBuild);
        assertProject(cpp, cppBuild);
        assertProject(cs, csBuild);
    }

    private void assertProject(Project project, Build build) throws IOException {
        String log = Files.readString(Path.of(build.getLogPath()));
        Assert.assertTrue(log.contains("Building complete"));
        Assert.assertTrue(buildService.getBuild(project).isPresent());
        Assert.assertEquals(BuildStatus.BUILT, buildRepository.findById(build.getId()).orElse(null).getBuildStatus());
        Assert.assertEquals(BuildStatus.BUILT, projectRepository.findById(project.getId()).orElse(null).getBuildStatus());
        Assert.assertNotNull(projectRepository.findById(project.getId()).orElse(null).getImageID());
    }

    Project createJavaProject(int id) {
        return projectRepository.save(new Project(
                id, "Test project", "file:" + testDataPath + "/docker/apps/java-maven-project.zip",
                "java_docker_sample-master",
                "mvn install", "test", 1
        ));
    }

    Project createCProject() {
        return projectRepository.save(new Project(
                222, "Test C project", "file:" + testDataPath + "/docker/apps/c-gcc.zip",
                "c",
                "gcc demo.c -o program -std=c11", "test", 2
        ));
    }

    Project createCPPProject() {
        return projectRepository.save(new Project(
                333, "Test C++ project", "file:" + testDataPath + "/docker/apps/cpp-gcc.zip",
                "cpp",
                "g++ demo.cpp -o program", "test", 3
        ));
    }

    Project createCSharpProject() {
        return projectRepository.save(new Project(
                444, "Test C# project", "file:" + testDataPath + "/docker/apps/csharp-netcore-3.1.zip",
                "csharp_demo",
                "dotnet build", "test", 4
        ));
    }

}
