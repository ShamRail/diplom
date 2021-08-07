package ru.ugasu.app.service;

import com.github.dockerjava.api.DockerClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.run.App;
import ru.ugasu.app.repo.AppRepository;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.command.CommandInfo;
import ru.ugasu.app.service.command.CommandStatus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleAppServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AppService appService;

    @Autowired
    AppRepository appRepository;

    @Autowired
    DockerClient dockerClient;

    static Project project;

    static boolean isBuilt = false;

    @Before
    public void buildProject() throws Exception {
        if (isBuilt) {
            return;
        }

        String testData = Path.of("./", "src", "test", "data").toRealPath().toString();
        String projectPath = Path.of(testData, "java_project").toAbsolutePath().toString();
        String imageID = dockerClient.buildImageCmd(new File(projectPath)).start().awaitImageId();

        project = new Project();
        project.setRunCommand("java -jar program.jar");
        project.setId(9999);
        project.setBuildStatus("BUILT");
        project.setImageID(imageID);
        projectRepository.save(project);

        isBuilt = true;
    }

    @Test
    public void whenRun() {
        App app = appService.start(project);
        assertEquals(AppStatus.STARTED, appRepository.findById(app.getId()).orElse(null).getAppStatus());
        assertNotNull(app.getStartAt());
        assertNotNull(app.getWsURI());
    }

    @Test
    public void whenCopyInAndCopyOut() throws IOException {
        App app = appService.start(project);

        StringJoiner content = new StringJoiner(System.lineSeparator());
        content.add("Hello");
        content.add("How are you?");
        content.add("I'am fine!");

        try (InputStream in = new ByteArrayInputStream(content.toString().getBytes())) {
            CommandInfo copyCommand = appService.copyIn(app, in, Path.of("in/file/phrases.txt"));
            assertEquals(CommandStatus.COMPLETE, copyCommand.getCommandStatus());
        }

        CommandInfo copyOutCommand = appService.copyFrom(app, Path.of("src/main/java/demo/Main.java"));
        assertEquals(CommandStatus.COMPLETE, copyOutCommand.getCommandStatus());
        assertTrue(Files.exists(Path.of(app.getAppPath(), "src/main/java/demo/Main.java")));
    }

    @Test
    public void whenCopyInAndCopyOutWithDuplicates() throws IOException {
        App app = appService.start(project);

        StringJoiner content = new StringJoiner(System.lineSeparator());
        content.add("Hello");
        content.add("How are you?");
        content.add("I'am fine!");

        try (InputStream in = new ByteArrayInputStream(content.toString().getBytes())) {
            CommandInfo copyCommand = appService.copyIn(app, in, Path.of("in/file/phrases.txt"));
            assertEquals(CommandStatus.COMPLETE, copyCommand.getCommandStatus());
        }

        try (InputStream in = new ByteArrayInputStream(content.toString().getBytes())) {
            CommandInfo copyCommand = appService.copyIn(app, in, Path.of("in/file/phrases.txt"));
            assertEquals(CommandStatus.COMPLETE, copyCommand.getCommandStatus());
        }

        CommandInfo copyOutCommand = appService.copyFrom(app, Path.of("src/main/java/demo/Main.java"));
        assertEquals(CommandStatus.COMPLETE, copyOutCommand.getCommandStatus());
        assertTrue(Files.exists(Path.of(app.getAppPath(), "src/main/java/demo/Main.java")));

        CommandInfo copyOutCommand2 = appService.copyFrom(app, Path.of("src/main/java/demo/Main.java"));
        assertEquals(CommandStatus.COMPLETE, copyOutCommand2.getCommandStatus());
        assertTrue(Files.exists(Path.of(app.getAppPath(), "src/main/java/demo/Main.java")));
    }


    @Test
    public void whenRunAndKill() {
        App app = appService.start(project);
        CommandInfo commandInfo = appService.kill(app);
        assertEquals(CommandStatus.COMPLETE, commandInfo.getCommandStatus());
    }

    @Test
    public void whenTryToRunNotBuiltProject() {
        Project project = new Project();
        project.setId(1999);
        projectRepository.save(project);
        App app = appService.start(project);
        assertEquals(AppStatus.FALLEN, app.getAppStatus());
    }

    @Test
    public void whenRunWith2Threads() throws Exception {

        StringJoiner content = new StringJoiner(System.lineSeparator());
        content.add("Hello");
        content.add("How are you?");
        content.add("I'am fine!");

        Thread thread1 = new Thread(() -> {
            App javaApp = appService.start(project);

            try (InputStream in = new ByteArrayInputStream(content.toString().getBytes())) {
                CommandInfo copyCommand = appService.copyIn(javaApp, in, Path.of("in/file/phrases.txt"));
                assertEquals(CommandStatus.COMPLETE, copyCommand.getCommandStatus());
            } catch (IOException e) {
                e.printStackTrace();
            }

            CommandInfo copyOutCommand = appService.copyFrom(javaApp, Path.of("src/main/java/demo/Main.java"));
            assertEquals(CommandStatus.COMPLETE, copyOutCommand.getCommandStatus());
            assertTrue(Files.exists(Path.of(javaApp.getAppPath(), "src/main/java/demo/Main.java")));
        });

        Thread thread2 = new Thread(() -> {
            App javaApp = appService.start(project);

            try (InputStream in = new ByteArrayInputStream(content.toString().getBytes())) {
                CommandInfo copyCommand = appService.copyIn(javaApp, in, Path.of("in/file/phrases.txt"));
                assertEquals(CommandStatus.COMPLETE, copyCommand.getCommandStatus());
            } catch (IOException e) {
                e.printStackTrace();
            }

            CommandInfo copyOutCommand = appService.copyFrom(javaApp, Path.of("src/main/java/demo/Main.java"));
            assertEquals(CommandStatus.COMPLETE, copyOutCommand.getCommandStatus());
            assertTrue(Files.exists(Path.of(javaApp.getAppPath(), "src/main/java/demo/Main.java")));
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

    }


}
