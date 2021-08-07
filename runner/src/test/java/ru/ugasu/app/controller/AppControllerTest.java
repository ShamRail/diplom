package ru.ugasu.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.run.App;
import ru.ugasu.app.repo.AppRepository;
import ru.ugasu.app.service.AppService;
import ru.ugasu.app.service.AppStatus;
import ru.ugasu.app.service.ProjectService;
import ru.ugasu.app.service.command.CommandInfo;
import ru.ugasu.app.service.command.CommandStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
@RunWith(SpringRunner.class)
public class AppControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ProjectService projectService;

    @MockBean
    AppService appService;

    @MockBean
    AppRepository appRepository;

    @Autowired
    ObjectMapper objectMapper;

    static Project project;

    static boolean isCreated = false;

    @Before
    public void createJavaProject() {
        if (isCreated) {
            return;
        }
        isCreated = true;
        project = new Project(8080, "Java + Maven",
                "java -jar program.jar", "in/phrases.txt",
                "out/phrases.txt", "123", "BUILT", 1
        );
    }

    @Test
    public void whenRunAppFromExistingProject() throws Exception {
        App app = getApp();

        Mockito.when(projectService.findById(project.getId())).thenReturn(Optional.of(project));
        Mockito.when(projectService.save(project)).thenReturn(project);
        Mockito.when(appService.start(project)).thenReturn(app);

        mvc.perform(
                post("/run")
                        .param("projectID", String.valueOf(project.getId()))
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.startAt", Matchers.notNullValue()))
                .andExpect(jsonPath("$.appStatus", Is.is("STARTED")))
                .andExpect(jsonPath("$.containerID", Matchers.notNullValue()))
                .andExpect(jsonPath("$.wsURI", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Is.is("Environment is started")))
                .andExpect(jsonPath("$.project.name", Is.is(project.getName())))
                .andExpect(jsonPath("$.project.runCommand", Is.is(project.getRunCommand())))
                .andExpect(jsonPath("$.project.inFiles", Is.is(project.getInFiles())))
                .andExpect(jsonPath("$.project.outFiles", Is.is(project.getOutFiles())))
                .andExpect(jsonPath("$.project.imageID", Is.is(project.getImageID())))
                .andExpect(jsonPath("$.project.configurationId", Is.is(project.getConfigurationId())));

    }

    @Test
    public void whenRunAndUploadFile() throws Exception {
        App app = new App();
        app.setId(1);
        app.setAppStatus(AppStatus.STARTED);
        app.setAppPath("/path/to/file");

        CommandInfo commandInfo = new CommandInfo("File successfully copied!", CommandStatus.COMPLETE);

        Mockito.when(appRepository.findById(app.getId())).thenReturn(Optional.of(app));
        Mockito.when(appService.copyIn(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(commandInfo);

        String content = String.join(System.lineSeparator(),
            "Hello", "How are you?", "I'm fine!"
        );
        MockMultipartFile file = new MockMultipartFile(
                "file", "file.txt", MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );
        mvc.perform(
                multipart("/uploadFile").file(file)
                .param("appID", String.valueOf(app.getId()))
                .param("filePath", "in/phrases.txt")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(jsonPath("$.commandStatus", Is.is("COMPLETE")))
                .andExpect(jsonPath("$.message", Is.is("File successfully copied!")));
    }

    @Test
    public void whenRunUploadAndDownload() throws Exception {
        App app = new App();
        app.setId(1);
        app.setAppStatus(AppStatus.STARTED);
        app.setAppPath("");

        CommandInfo commandInfo = new CommandInfo("File successfully copied!", CommandStatus.COMPLETE);

        Mockito.when(appRepository.findById(app.getId())).thenReturn(Optional.of(app));
        Mockito.when(appService.copyIn(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(commandInfo);

        String content = String.join(System.lineSeparator(),
                "Hello", "How are you?", "I'm fine!"
        );
        MockMultipartFile file = new MockMultipartFile(
                "file", "file.txt", MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );
        mvc.perform(
                multipart("/uploadFile").file(file)
                        .param("appID", String.valueOf(app.getId()))
                        .param("filePath", "in/phrases.txt")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        Mockito.when(appRepository.findById(app.getId())).thenReturn(Optional.of(app));
        Mockito.when(appService.copyFrom(Mockito.any(), Mockito.any())).thenReturn(commandInfo);

        Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".txt");
        Files.writeString(tempFile, content);
        byte[] filesInBytes = mvc.perform(
                get("/downloadFile")
                .param("appID", String.valueOf(app.getId()))
                .param("filePath", tempFile.toAbsolutePath().toString())
        )
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsByteArray();

        Assert.assertEquals(content, new String(filesInBytes));
    }

    @Test
    public void whenRunWithNotExistProject() throws Exception {
        Mockito.when(appRepository.findById(-1)).thenReturn(Optional.empty());
        mvc.perform(post("/run").param("projectID", "-1"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void whenRunNotBuiltProject() throws Exception {
        Project project = new Project(3000);
        project.setBuildStatus("STARTED");
        Mockito.when(projectService.findById(3000)).thenReturn(Optional.of(project));
        mvc.perform(post("/run").param("projectID", "3000"))
                .andExpect(status().isConflict());

    }

    @Test
    public void whenUploadToUnExistedApp() throws Exception {
        String content = String.join(System.lineSeparator(),
                "Hello", "How are you?", "I'm fine!"
        );
        MockMultipartFile file = new MockMultipartFile(
                "file", "file.txt", MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );
        Mockito.when(appRepository.findById(-1)).thenReturn(Optional.empty());
        mvc.perform(
                multipart("/uploadFile").file(file)
                        .param("appID", "-1")
                        .param("filePath", "in/phrases.txt")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void whenUploadToNotStartedApp() throws Exception {
        App app = new App();
        app.setId(1);
        app.setAppStatus(AppStatus.FALLEN);

        String content = String.join(System.lineSeparator(),
                "Hello", "How are you?", "I'm fine!"
        );
        MockMultipartFile file = new MockMultipartFile(
                "file", "file.txt", MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );
        Mockito.when(appRepository.findById(1)).thenReturn(Optional.of(app));
        mvc.perform(
                multipart("/uploadFile").file(file)
                        .param("appID", String.valueOf(app.getId()))
                        .param("filePath", "in/phrases.txt")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isConflict());
    }

    @Test
    public void whenSuccessToKillApp() throws Exception {
        App app = new App();
        app.setId(1);
        app.setAppStatus(AppStatus.STARTED);

        CommandInfo commandInfo = new CommandInfo("", CommandStatus.COMPLETE);

        Mockito.when(appRepository.findById(app.getId())).thenReturn(Optional.of(app));
        Mockito.when(appService.kill(app)).thenReturn(commandInfo);

        mvc.perform(get("/kill").queryParam("id", String.valueOf(app.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void whenKillKilledApp() throws Exception {
        App app = new App();
        app.setId(1);
        app.setAppStatus(AppStatus.DIED);

        Mockito.when(appRepository.findById(app.getId())).thenReturn(Optional.of(app));

        mvc.perform(get("/kill").queryParam("id", String.valueOf(app.getId())))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenFailedToKill() throws Exception {
        App app = new App();
        app.setId(1);
        app.setAppStatus(AppStatus.STARTED);

        CommandInfo commandInfo = new CommandInfo("Failed", CommandStatus.FALLEN);

        Mockito.when(appRepository.findById(app.getId())).thenReturn(Optional.of(app));
        Mockito.when(appService.kill(app)).thenReturn(commandInfo);

        mvc.perform(get("/kill").queryParam("id", String.valueOf(app.getId())))
                .andExpect(status().isBadRequest());
    }


    private App getApp() {
        App app = new App();
        app.setId(1);
        app.setStartAt(LocalDateTime.now());
        app.setEndAt(LocalDateTime.now());
        app.setAppStatus(AppStatus.STARTED);
        app.setContainerID("12345");
        app.setWsURI("/container/12345");
        app.setMessage("Environment is started");
        app.setAppPath("/path");
        app.setProject(project);
        return app;
    }

}
