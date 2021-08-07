package ru.ugasu.app.controller;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.repo.ProjectRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ProjectRepository projectRepository;

    @Test
    public void whenFound() throws Exception {
        int id = 1;

        Project project = new Project();
        project.setId(id);
        project.setName("Java project");
        project.setConfigurationId(1);
        project.setBuildStatus(BuildStatus.BUILT);
        project.setImageID("123abc");
        project.setArchiveInnerDir("someDir");
        project.setBuildCommand("mvn install");
        project.setInFiles("in/file.txt");
        project.setOutFiles("out/file.txt");
        project.setRunCommand("java -jar program.jar");
        project.setSourceCodeURL("some url");

        Mockito.when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        mvc.perform(get("/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(project.getId())))
                .andExpect(jsonPath("$.name", Is.is(project.getName())))
                .andExpect(jsonPath("$.configurationId", Is.is(project.getConfigurationId())))
                .andExpect(jsonPath("$.buildStatus", Is.is("BUILT")))
                .andExpect(jsonPath("$.imageID", Is.is(project.getImageID())))
                .andExpect(jsonPath("$.archiveInnerDir", Is.is(project.getArchiveInnerDir())))
                .andExpect(jsonPath("$.buildCommand", Is.is(project.getBuildCommand())))
                .andExpect(jsonPath("$.inFiles", Is.is(project.getInFiles())))
                .andExpect(jsonPath("$.outFiles", Is.is(project.getOutFiles())))
                .andExpect(jsonPath("$.runCommand", Is.is(project.getRunCommand())))
                .andExpect(jsonPath("$.sourceCodeURL", Is.is(project.getSourceCodeURL())));

    }

    @Test
    public void whenNotFound() throws Exception {
        int id = 0;
        Mockito.when(projectRepository.findById(id)).thenReturn(Optional.empty());
        mvc.perform(get("/projects/" + id)).andExpect(status().isNotFound());
    }
}