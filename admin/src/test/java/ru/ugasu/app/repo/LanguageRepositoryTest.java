package ru.ugasu.app.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.Language;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void whenCreateAndGet() {
        Language language = new Language("Java", "14");
        languageRepository.save(language);
        Language output = languageRepository.getOne(language.getId());
        assertEquals(language, output);
    }

    @Test
    public void whenFindById() {
        Language language = new Language("Java", "14");
        languageRepository.save(language);
        Language output = languageRepository.findById(language.getId()).orElse(null);
        assertEquals(language, output);
    }

    @Test
    public void whenUpdate() {
        Language language = new Language("Java", "14");
        languageRepository.save(language);
        Language updated = new Language(language.getId(), "Python", "3.6");
        languageRepository.save(updated);
        assertEquals(updated, languageRepository.findById(language.getId()).orElse(null));
    }

    @Test
    public void whenDelete() {
        Language language = new Language("Java", "14");
        languageRepository.save(language);
        languageRepository.deleteById(language.getId());
        assertTrue(languageRepository.findById(language.getId()).isEmpty());
    }

    @Test
    public void whenFindAll() {
        Language language1 = new Language("Java", "14");
        Language language2 = new Language("Python", "3.6");
        languageRepository.saveAll(List.of(language1, language2));
        assertEquals(List.of(language1, language2), languageRepository.findAll());
    }

}