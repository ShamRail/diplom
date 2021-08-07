package ru.ugasu.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.repo.LanguageRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithMockUser
public class LanguageControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void whenSave() throws Exception {
        Language language = new Language("Java", "11");
        mvc.perform(
                post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(language))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(language.getName())))
                .andExpect(jsonPath("$.version", Is.is(language.getVersion())))
                .andExpect(jsonPath("$.id", Is.is(Matchers.notNullValue())));
    }

    @Test
    public void whenFindById() throws Exception {
        Language language = languageRepository.save(new Language("Java", "11"));
        mvc.perform(
                get(String.format("/languages/%d", language.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(language.getName())))
                .andExpect(jsonPath("$.version", Is.is(language.getVersion())))
                .andExpect(jsonPath("$.id", Is.is(language.getId())));
    }

    @Test
    public void whenFindAll() throws Exception {
        Language language1 = languageRepository.save(new Language("Java", "11"));
        Language language2 = languageRepository.save(new Language("Python", "3.6"));
        Language language3 = languageRepository.save(new Language("C++", "17"));
        mvc.perform(
                get("/languages")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Is.is(3)))
                .andExpect(jsonPath("$.[0].name", Is.is(language1.getName())))
                .andExpect(jsonPath("$.[0].version", Is.is(language1.getVersion())))
                .andExpect(jsonPath("$.[0].id", Is.is(language1.getId())))

                .andExpect(jsonPath("$.[1].name", Is.is(language2.getName())))
                .andExpect(jsonPath("$.[1].version", Is.is(language2.getVersion())))
                .andExpect(jsonPath("$.[1].id", Is.is(language2.getId())))

                .andExpect(jsonPath("$.[2].name", Is.is(language3.getName())))
                .andExpect(jsonPath("$.[2].version", Is.is(language3.getVersion())))
                .andExpect(jsonPath("$.[2].id", Is.is(language3.getId())));
    }

    @Test
    public void whenUpdate() throws Exception {
        Language language = languageRepository.save(new Language("Java", "11"));
        Language updated = new Language("Java", "16");
        mvc.perform(
                put(String.format("/languages/%d", language.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated))
        )
                .andExpect(status().isOk());
        Language output = languageRepository.findById(language.getId()).orElse(null);
        assertEquals(updated.getName(), output.getName());
        assertEquals(updated.getVersion(), output.getVersion());
    }

    @Test
    public void whenDelete() throws Exception {
        Language language = languageRepository.save(new Language("Java", "11"));
        mvc.perform(
                delete(String.format("/languages/%d", language.getId()))
        )
                .andExpect(status().isOk());
        assertFalse(languageRepository.existsById(language.getId()));
    }

    @Test
    public void whenUpdateNotFound() throws Exception {
        Language updated = new Language("Java", "16");
        mvc.perform(
                put("/languages/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteNotFound() throws Exception {
        Language language = languageRepository.save(new Language("Java", "11"));
        mvc.perform(
                delete("/languages/0")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByIdNotFound() throws Exception {
        mvc.perform(get("/languages/0"))
                .andExpect(status().isNotFound());
    }

}
