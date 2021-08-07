package ru.ugasu.app.service;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.repo.ProjectRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ProjectServiceImplTest {

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {

        @MockBean
        RestTemplate restTemplate;

        @MockBean
        ProjectRepository projectRepository;

        @Bean
        ProjectService projectService() {
            return new ProjectServiceImpl(projectRepository, restTemplate);
        }

    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectService projectService;

    final String json = new JSONObject()
            .put("id", 1)
            .put("name", "Java Project")
            .put("sourceCodeURL", "http://...")
            .put("buildCommand", "mvn install")
            .put("runCommand", "java -jar program.jar")
            .put("inFiles", "in/phrases.txt")
            .put("outFiles", "out/result.txt")
            .put("configurationId", 1)
            .put("buildStatus", "BUILT")
            .put("imageID", "1234")
            .put("archiveInnerDir", "some dir").toString();

    @Test
    public void whenSucceed() {
        String url = "http://builder-api/projects/1";
        Mockito.when(restTemplate.getForObject(url, String.class)).thenReturn(json);
        Project result = projectService.findById(1).get();
        assertEquals(Integer.valueOf(1), result.getId());
        assertEquals("BUILT", result.getBuildStatus());
        assertEquals("1234", result.getImageID());
        assertEquals("in/phrases.txt", result.getInFiles());
        assertEquals("out/result.txt", result.getOutFiles());
        assertEquals("java -jar program.jar", result.getRunCommand());
    }

    @Test
    public void whenNotFound() {
        String url = "http://builder-api/projects/1";
        Mockito.when(restTemplate.getForObject(url, String.class)).thenThrow(HttpClientErrorException.NotFound.class);
        assertEquals(Optional.empty(), projectService.findById(1));
    }

}