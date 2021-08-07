package ru.ugasu.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.model.dto.ProjectDTO;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.build.BuildService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuildController.class)
@RunWith(SpringRunner.class)
public class BuildControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    BuildService buildService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void whenSuccessBuilding() throws Exception {
        ProjectDTO projectDTO = getProjectDTO(1112);
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectDTO);
        Project project = getProject(projectDTO);
        Build output = getBuild();

        Mockito.when(buildService.getBuild(project)).thenReturn(Optional.empty());
        Mockito.when(projectRepository.save(project)).thenReturn(project);
        Mockito.when(buildService.start(project)).thenReturn(output);

        mvc.perform(
                post("/build")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.startAt", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.buildStatus", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.message", Is.is(Matchers.notNullValue()))).andReturn();

    }

    @Test
    public void whenBuildAndGetBuildSuccessStatus() throws Exception {
        ProjectDTO projectDTO = getProjectDTO(1113);
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectDTO);
        Project project = getProject(projectDTO);
        Build build = getBuild();

        Mockito.when(buildService.getBuild(project)).thenReturn(Optional.empty());
        Mockito.when(projectRepository.save(project)).thenReturn(project);
        Mockito.when(buildService.start(project)).thenReturn(build);

        mvc.perform(
                post("/build")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andReturn();

        build.setBuildStatus(BuildStatus.BUILT);
        build.setMessage("Build succeed");
        Mockito.when(buildService.getBuild(project)).thenReturn(Optional.of(build));

        mvc.perform(
                get("/build-status").param("projectID", String.valueOf(1113))
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.startAt", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.endAt", Is.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.buildStatus", Is.is("BUILT")))
                .andExpect(jsonPath("$.message", Is.is("Build succeed")));

    }

    @Test
    public void whenInvalidConfiguration() throws Exception {
        ProjectDTO projectDTO = getProjectDTO(1114);
        projectDTO.setConfigID(null);
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectDTO);

        mvc.perform(
                post("/build")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).
                andExpect(status().is4xxClientError());
    }

    @Test
    public void whenRunBuildTwiceThenConflict() throws Exception {
        ProjectDTO projectDTO = getProjectDTO(1115);
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectDTO);
        Project project = getProject(projectDTO);
        Build output = getBuild();

        Mockito.when(buildService.getBuild(project)).thenReturn(Optional.empty());
        Mockito.when(projectRepository.save(project)).thenReturn(project);
        Mockito.when(buildService.start(project)).thenReturn(output);

        mvc.perform(
                post("/build")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().is2xxSuccessful());

        Mockito.when(buildService.getBuild(project)).thenReturn(Optional.of(output));

        mvc.perform(
                post("/build")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isConflict());

    }

    @Test
    public void whenMissingBuildCommandThenWillBeBadRequest() throws Exception {
        ProjectDTO projectDTO = getProjectDTO(1116);
        projectDTO.setBuildCommand(null);

        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectDTO);

        mvc.perform(
                post("/build")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(status().isBadRequest());
    }

    private ProjectDTO getProjectDTO(int id) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(id);
        projectDTO.setArchiveInnerDir("java_docker_sample-master");
        projectDTO.setSourceCodeURL("https://github.com/ShamRail/app-runner/raw/master/src/test/data/docker/apps/java-maven-project.zip");
        projectDTO.setBuildCommand("mvn install");
        projectDTO.setConfigID(1);
        projectDTO.setRunCommand("java -jar target/program.jar");
        return projectDTO;
    }

    private Build getBuild() {
        Build output = new Build();
        output.setId(1);
        output.setStartAt(LocalDateTime.now());
        output.setEndAt(LocalDateTime.now());
        output.setBuildStatus(BuildStatus.STARTED);
        output.setMessage("Build is started!");
        return output;
    }

    private Project getProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setArchiveInnerDir(projectDTO.getArchiveInnerDir());
        project.setBuildCommand(projectDTO.getBuildCommand());
        project.setConfigurationId(projectDTO.getConfigID());
        project.setInFiles(projectDTO.getInFiles());
        project.setOutFiles(projectDTO.getOutFiles());
        project.setRunCommand(projectDTO.getRunCommand());
        project.setSourceCodeURL(projectDTO.getSourceCodeURL());
        project.setName(projectDTO.getName());
        return project;
    }

}
