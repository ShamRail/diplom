package ru.ugasu.app.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.Builder;
import ru.ugasu.app.model.Language;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BuilderRepositoryTest {

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    BuilderRepository builderRepository;

    @Test
    public void whenCreateAndGet() {
        Language language = new Language("Java", "14");
        languageRepository.save(language);
        Builder builder = new Builder("maven", "3.6", List.of(language));
        builderRepository.save(builder);
        Builder output = builderRepository.findById(builder.getId()).orElse(null);
        assertEquals(builder, output);
        assertEquals(List.of(language), builder.getLanguages());
    }

    @Test
    public void whenUpdate() {
        Language language1 = new Language("Java", "14");
        Language language2 = new Language("Python", "3.6");
        languageRepository.saveAll(List.of(language1, language2));

        Builder builder = new Builder("maven", "3.6", new ArrayList<>(Arrays.asList(language1)));
        builderRepository.save(builder);

        Builder updated = new Builder(builder.getId(), "pip", "3", new ArrayList<>(Arrays.asList(language2)));
        builderRepository.save(updated);

        Builder output = builderRepository.findById(builder.getId()).orElse(null);
        assertEquals(updated, output);
        assertEquals(List.of(language2), output.getLanguages());
    }

    @Test
    public void whenFindAll() {
        Language language = languageRepository.save(new Language("Java", "14"));
        Builder builder1 = builderRepository.save(new Builder("maven", "3.6", List.of(language)));
        Builder builder2 = builderRepository.save(new Builder("ant", "1.1", List.of(language)));
        List<Builder> builders = builderRepository.findAll();
        assertEquals(2, builders.size());
        assertEquals(builder1, builders.get(0));
        assertEquals(builder2, builders.get(1));
        assertEquals(List.of(language), builders.get(0).getLanguages());
        assertEquals(List.of(language), builders.get(1).getLanguages());
    }

    @Test
    public void whenDelete() {
        Language language = languageRepository.save(new Language("java", "14"));
        Builder builder = builderRepository.save(new Builder("maven", "3.6", List.of(language)));
        builderRepository.deleteById(builder.getId());
        assertTrue(builderRepository.findById(builder.getId()).isEmpty());
        assertEquals(language, languageRepository.findById(language.getId()).orElse(null));
    }

    @Autowired
    EntityManager entityManager;

    @Test
    public void whenSaveAndGetLanguages() {
        Language language1 = languageRepository.save(new Language("java", "14"));
        Language language2 = languageRepository.save(new Language("java", "16"));
        Builder builder = builderRepository.saveAndFlush(new Builder("maven", "3.6", List.of(language1, language2)));

        entityManager.flush();
        entityManager.clear();

        Builder output = builderRepository.findById(builder.getId()).orElse(null);
        assertEquals(List.of(language1, language2), output.getLanguages());
    }

    @Test
    public void whenUpdateBuilderLanguages() {
        Language language1 = languageRepository.save(new Language("java", "14"));
        Language language2 = languageRepository.save(new Language("java", "16"));
        Builder builder = builderRepository.saveAndFlush(new Builder("maven", "3.6", List.of(language1, language2)));

        entityManager.flush();
        entityManager.clear();

        Language language3 = languageRepository.save(new Language("Groovy", "1.0"));
        Language language4 = languageRepository.save(new Language("Scala", "2.0"));
        Builder updated = builderRepository.findById(builder.getId()).orElse(null);
        updated.setLanguages(new ArrayList<>(List.of(language3, language4)));
        builderRepository.save(updated);

        entityManager.flush();
        entityManager.clear();

        Builder output = builderRepository.findById(builder.getId()).orElse(null);
        assertEquals(List.of(language3, language4), output.getLanguages());
    }

    @Test
    public void whenUpdateBuilderLanguagesByConstructor() {
        Language language1 = languageRepository.save(new Language("java", "14"));
        Language language2 = languageRepository.save(new Language("java", "16"));
        Builder builder = builderRepository.saveAndFlush(new Builder("maven", "3.6", List.of(language1, language2)));

        entityManager.flush();
        entityManager.clear();

        Language language3 = languageRepository.save(new Language("Groovy", "1.0"));
        Language language4 = languageRepository.save(new Language("Scala", "2.0"));
        Builder updated = new Builder(
                builder.getId(), builder.getName(), builder.getVersion(), Arrays.asList(language3, language4)
        );
        builderRepository.save(updated);

        entityManager.flush();
        entityManager.clear();

        Builder output = builderRepository.findById(builder.getId()).orElse(null);
        assertEquals(List.of(language3, language4), output.getLanguages());
    }

}
