package ru.ugasu.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ugasu.app.model.Builder;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.model.dto.ConfigurationDTO;
import ru.ugasu.app.repo.BuilderRepository;
import ru.ugasu.app.repo.ConfigurationRepository;
import ru.ugasu.app.repo.LanguageRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithMockUser
public class ConfigurationControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    BuilderRepository builderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ConfigurationRepository configurationRepository;

    @Test
    public void whenCreate() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", List.of(language)));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        String json = mvc.perform(
                post("/configuration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", Is.is(configurationDTO.getName())))
                .andExpect(jsonPath("$.version", Is.is(configurationDTO.getVersion())))
                .andExpect(jsonPath("$.description", Is.is(configurationDTO.getDescription())))
                .andExpect(jsonPath("$.dockerFile", Matchers.notNullValue()))
                .andExpect(jsonPath("$.language.name", Is.is(language.getName())))
                .andExpect(jsonPath("$.language.version", Is.is(language.getVersion())))
                .andExpect(jsonPath("$.builder.name", Is.is(builder.getName())))
                .andExpect(jsonPath("$.builder.version", Is.is(builder.getVersion())))
                .andReturn().getResponse().getContentAsString();

        String path = JsonPath.parse(json).read("$.dockerFile");
        Path dockerFile = Path.of(path);
        assertEquals(dockerFileContent, Files.readString(dockerFile));

    }

    @Test
    public void whenFindById() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", List.of(language)));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        String json = mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andReturn().getResponse().getContentAsString();

        int id = JsonPath.parse(json).read("$.id");
        mvc.perform(get("/configuration/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", Is.is(configurationDTO.getName())))
                .andExpect(jsonPath("$.version", Is.is(configurationDTO.getVersion())))
                .andExpect(jsonPath("$.description", Is.is(configurationDTO.getDescription())))
                .andExpect(jsonPath("$.dockerFile", Matchers.notNullValue()))
                .andExpect(jsonPath("$.language.name", Is.is(language.getName())))
                .andExpect(jsonPath("$.language.version", Is.is(language.getVersion())))
                .andExpect(jsonPath("$.builder.name", Is.is(builder.getName())))
                .andExpect(jsonPath("$.builder.version", Is.is(builder.getVersion())));

    }

    @Test
    public void whenDeleteId() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", List.of(language)));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        String json = mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andReturn().getResponse().getContentAsString();

        int id = JsonPath.parse(json).read("$.id");
        mvc.perform(delete("/configuration/" + id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenUpdate() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", List.of(language)));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        String json = mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andReturn().getResponse().getContentAsString();

        language = languageRepository.save(new Language("C++", "17"));
        builder = builderRepository.save(new Builder("CMake", "4.0", List.of(language)));
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("C++ + CMake config");
        configurationDTO.setName("C++ + CMake");
        configurationDTO.setVersion("2.0");
        String dockerFileContent2 = String.join(System.lineSeparator(),
                "FROM cmake",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent2);

        int id = JsonPath.parse(json).read("$.id");
        mvc.perform(
                put("/configuration/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configurationDTO)))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/configuration/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", Is.is(configurationDTO.getName())))
                .andExpect(jsonPath("$.version", Is.is(configurationDTO.getVersion())))
                .andExpect(jsonPath("$.description", Is.is(configurationDTO.getDescription())))
                .andExpect(jsonPath("$.dockerFile", Matchers.notNullValue()))
                .andExpect(jsonPath("$.language.name", Is.is(language.getName())))
                .andExpect(jsonPath("$.language.version", Is.is(language.getVersion())))
                .andExpect(jsonPath("$.builder.name", Is.is(builder.getName())))
                .andExpect(jsonPath("$.builder.version", Is.is(builder.getVersion())));
    }

    @Test
    public void whenFindAll() throws Exception {
        //configurationRepository.deleteAll();

        Language language1 = languageRepository.save(new Language("Java", "14"));
        Builder builder1 = builderRepository.save(new Builder("Maven", "3.6", List.of(language1)));

        ConfigurationDTO configurationDTO1 = new ConfigurationDTO();
        configurationDTO1.setBuilderID(builder1.getId());
        configurationDTO1.setLanguageID(language1.getId());
        configurationDTO1.setDescription("Java + Maven config");
        configurationDTO1.setName("Java + Maven");
        configurationDTO1.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO1.setDockerContent(dockerFileContent);

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO1))
        );

        ConfigurationDTO configurationDTO2 = new ConfigurationDTO();
        Language language2 = languageRepository.save(new Language("C++", "17"));
        Builder builder2 = builderRepository.save(new Builder("CMake", "4.0", List.of(language2)));
        configurationDTO2.setBuilderID(builder2.getId());
        configurationDTO2.setLanguageID(language2.getId());
        configurationDTO2.setDescription("C++ + CMake config");
        configurationDTO2.setName("C++ + CMake");
        configurationDTO2.setVersion("2.0");
        String dockerFileContent2 = String.join(System.lineSeparator(),
                "FROM cmake",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO2.setDockerContent(dockerFileContent2);

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO2)
        ));

        mvc.perform(get("/configuration/all"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].name", Is.is(configurationDTO1.getName())))
                .andExpect(jsonPath("$.[0].version", Is.is(configurationDTO1.getVersion())))
                .andExpect(jsonPath("$.[0].description", Is.is(configurationDTO1.getDescription())))
                .andExpect(jsonPath("$.[0].dockerFile", Matchers.notNullValue()))
                .andExpect(jsonPath("$.[0].language.name", Is.is(language1.getName())))
                .andExpect(jsonPath("$.[0].language.version", Is.is(language1.getVersion())))
                .andExpect(jsonPath("$.[0].builder.name", Is.is(builder1.getName())))
                .andExpect(jsonPath("$.[0].builder.version", Is.is(builder1.getVersion())))

                .andExpect(jsonPath("$.[1].name", Is.is(configurationDTO2.getName())))
                .andExpect(jsonPath("$.[1].version", Is.is(configurationDTO2.getVersion())))
                .andExpect(jsonPath("$.[1].description", Is.is(configurationDTO2.getDescription())))
                .andExpect(jsonPath("$.[1].dockerFile", Matchers.notNullValue()))
                .andExpect(jsonPath("$.[1].language.name", Is.is(language2.getName())))
                .andExpect(jsonPath("$.[1].language.version", Is.is(language2.getVersion())))
                .andExpect(jsonPath("$.[1].builder.name", Is.is(builder2.getName())))
                .andExpect(jsonPath("$.[1].builder.version", Is.is(builder2.getVersion())));
    }

    @Test
    public void whenLanguageIsNotSpecified() throws Exception {
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", null));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenBuilderInNotSpecified() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().isNotFound());

    }

    @Test
    public void whenContentIsNotSpecified() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", List.of(language)));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void whenContentIsEmpty() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", List.of(language)));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        configurationDTO.setDockerContent("");

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void whenLanguageIsNotValid() throws Exception {
        Builder builder = builderRepository.save(new Builder("Maven", "3.6", null));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setBuilderID(builder.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        configurationDTO.setLanguageID(-1);
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenBuilderInNotValid() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));

        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setLanguageID(language.getId());
        configurationDTO.setDescription("Java + Maven config");
        configurationDTO.setName("Java + Maven");
        configurationDTO.setVersion("1.0");
        configurationDTO.setBuilderID(-1);
        String dockerFileContent = String.join(System.lineSeparator(),
                "FROM maven",
                "WORKDIR app",
                "COPY . .",
                "ARG BUILD_COMMAND",
                "RUN echo $BUILD_COMMAND > echo.txt",
                "RUN $BUILD_COMMAND > build_log.txt",
                "ENTRYPOINT [\"bash\"]"
        );
        configurationDTO.setDockerContent(dockerFileContent);

        mvc.perform(
                post("/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO))
        )
                .andExpect(status().isNotFound());

    }

}
