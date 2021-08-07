package ru.ugasu.app.service;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
public class ConfigServiceImplTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ConfigService configService;

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {

        @MockBean
        RestTemplate restTemplate;

        @Bean
        ConfigService configService() {
            return new ConfigServiceImpl(restTemplate);
        }

    }

    @Test
    public void whenSuccess() throws Exception {
        String dockerContent = String.join(System.lineSeparator(),
            "st1", "str2", "str3"
        );
        JSONObject jsonObject = new JSONObject();
        jsonObject
                .put("id", "1")
                .put("name", "Java, Maven")
                .put("version", "1.0")
                .put("description", "Some desc")
                .put(
                        "language", new JSONObject()
                                .put("id", "1").put("name", "Java").put("version", "14"))
                .put(
                        "builder", new JSONObject()
                                .put("id", "1").put("name", "Maven").put("version", "3.6"))
                .put("dockerFile", dockerContent);
        String requestURL = "http://admin-api/configuration/1";
        Mockito.when(restTemplate.getForObject(requestURL, String.class)).thenReturn(jsonObject.toString());
        Optional<Path> result = configService.uploadDockerfile(1);
        assertFalse(result.isEmpty());
        assertEquals(dockerContent, Files.readString(result.get()));
    }

}