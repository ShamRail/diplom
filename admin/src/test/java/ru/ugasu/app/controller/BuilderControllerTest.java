package ru.ugasu.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.ugasu.app.model.dto.BuilderDTO;
import ru.ugasu.app.repo.BuilderRepository;
import ru.ugasu.app.repo.LanguageRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser
public class BuilderControllerTest {

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    BuilderRepository builderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @Test
    public void whenSave() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        BuilderDTO builderDTO = new BuilderDTO("Maven", "3.6", new Integer[] {language.getId()});
        mvc.perform(
                post("/builders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(builderDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(builderDTO.getName())))
                .andExpect(jsonPath("$.version", Is.is(builderDTO.getVersion())));
    }

    @Test
    public void whenSaveWithNoExistLanguage() throws Exception {
        Language language = new Language("Java", "14");
        BuilderDTO builderDTO = new BuilderDTO("Maven", "3.6", new Integer[] {language.getId()});
        mvc.perform(
                post("/builders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(builderDTO))
        )
                .andExpect(status().isNotFound());
    }

    @Autowired
    EntityManager entityManager;

    @Test
    public void whenUpdate() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder(
                "Maven", "3.6", List.of(language)
        ));
        Language updated = languageRepository.save(new Language("Groovy", "5.0"));
        Builder updatedBuilder = new Builder(
                "Ant", "1.0", List.of(updated)
        );
        BuilderDTO builderDTO = new BuilderDTO(
                updatedBuilder.getName(), updatedBuilder.getVersion(), new Integer[] {updatedBuilder.getLanguages().get(0).getId()}
        );
        mvc.perform(
                put(String.format("/builders/%d", builder.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(builderDTO))
        ).andExpect(status().isOk());
        Builder output = entityManager.createQuery(
                "SELECT b FROM Builder AS b JOIN FETCH b.languages WHERE b.id=:id", Builder.class)
                .setParameter("id", builder.getId()).getSingleResult();
        List<Language> languages = output.getLanguages();
        assertEquals(updatedBuilder.getName(), output.getName());
        assertEquals(updatedBuilder.getVersion(), output.getVersion());
        assertEquals(updated.getName(), languages.get(0).getName());
        assertEquals(updated.getVersion(), languages.get(0).getVersion());
    }

    @Test
    public void whenUpdateWithNoExistsLanguage() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder(
                "Maven", "3.6", List.of(language)
        ));
        Language updated = new Language("Groovy", "5.0");
        Builder updatedBuilder = new Builder(
                "Ant", "1.0", List.of(updated)
        );
        BuilderDTO builderDTO = new BuilderDTO(
                updatedBuilder.getName(), updatedBuilder.getVersion(), new Integer[] {updatedBuilder.getLanguages().get(0).getId()}
        );
        mvc.perform(
                put(String.format("/builders/%d", builder.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(builderDTO))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateNotExistedBuilder() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = new Builder("Maven", "3.6", List.of(language));

        Builder updatedBuilder = new Builder(
                "Ant", "1.0", List.of(language)
        );
        BuilderDTO builderDTO = new BuilderDTO(
                updatedBuilder.getName(), updatedBuilder.getVersion(), new Integer[] {updatedBuilder.getLanguages().get(0).getId()}
        );
        mvc.perform(
                put(String.format("/builders/%d", builder.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(builderDTO))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void whenDelete() throws Exception {
        Builder builder = builderRepository.save(new Builder("Maven", "3.6"));
        mvc.perform(delete("/builders/" + builder.getId()))
                .andExpect(status().isOk());
        assertFalse(builderRepository.existsById(builder.getId()));
    }

    @Test
    public void whenDeleteWithNoExisted() throws Exception {
        mvc.perform(delete("/builders/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindById() throws Exception {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder = builderRepository.save(new Builder(
                "Maven", "3.6", List.of(language)
        ));
        mvc.perform(get("/builders/" + builder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(builder.getName())))
                .andExpect(jsonPath("$.version", Is.is(builder.getVersion())))
                .andExpect(jsonPath("$.languages[0].name", Is.is(language.getName())))
                .andExpect(jsonPath("$.languages[0].version", Is.is(language.getVersion())));
    }

}
