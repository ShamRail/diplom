package ru.ugasu.app.repo;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.Builder;
import ru.ugasu.app.model.Configuration;
import ru.ugasu.app.model.Language;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ConfigurationRepositoryTest {

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    BuilderRepository builderRepository;

    @Autowired
    ConfigurationRepository configurationRepository;

    @Test
    public void whenCreate() {
        Language language = languageRepository.save(new Language("java", "14"));
        Builder builder = builderRepository.save(new Builder("maven", "3.6", List.of(language)));
        Configuration configuration = configurationRepository.save(new Configuration(
                "Java 14, maven 3.6", "1.0", language, builder,
                "Standart java", "/path/to/Dockerfile"
        ));
        Configuration output = configurationRepository.findById(configuration.getId()).orElse(null);
        assertEquals(configuration, output);
        assertEquals(language, configuration.getLanguage());
        assertEquals(builder, configuration.getBuilder());
        assertEquals(configuration.getDescription(), output.getDescription());
        assertEquals(configuration.getDockerFile(), output.getDockerFile());
    }

    @Test
    public void whenUpdate() {
        Language language = languageRepository.save(new Language("java", "14"));
        Language updatedLanguage = languageRepository.save(new Language("java", "16"));
        Builder builder = builderRepository.save(new Builder("maven", "3.6", List.of(language)));
        Builder updatedBuilder = builderRepository.save(new Builder("maven", "3.8", List.of(language)));
        Configuration configuration = configurationRepository.save(new Configuration(
                "Java 14, maven 3.6", "1.0", language, builder,
                "Standart java", "/path/to/Dockerfile"
        ));
        Configuration updatedConfiguration = configurationRepository.save(new Configuration(
                configuration.getId(), "Java 16, maven 3.8", "1.5", updatedLanguage, updatedBuilder,
                "Updated java", "/updated/path/to/Dockerfile"
        ));
        Configuration output = configurationRepository.findById(configuration.getId()).orElse(null);
        assertEquals(updatedConfiguration, output);
        assertEquals(updatedLanguage, configuration.getLanguage());
        assertEquals(updatedBuilder, configuration.getBuilder());
        assertEquals(updatedConfiguration.getDescription(), output.getDescription());
        assertEquals(updatedConfiguration.getDockerFile(), output.getDockerFile());
    }

    @Test
    public void whenDelete() {
        Language language = languageRepository.save(new Language("java", "14"));
        Builder builder = builderRepository.save(new Builder("maven", "3.6", List.of(language)));
        Configuration configuration = configurationRepository.save(new Configuration(
                "Java 14, maven 3.6", "1.0", language, builder,
                "Standart java", "/path/to/Dockerfile"
        ));
        configurationRepository.deleteById(configuration.getId());
        assertTrue(configurationRepository.findById(configuration.getId()).isEmpty());
        assertEquals(language, languageRepository.getOne(language.getId()));
        assertEquals(builder, builderRepository.getOne(builder.getId()));
    }

    @Test
    public void whenFindAll() {
        Language language1 = languageRepository.save(new Language("java", "14"));
        Language language2 = languageRepository.save(new Language("java", "16"));
        Builder builder1 = builderRepository.save(new Builder("maven", "3.6", List.of(language1)));
        Builder builder2 = builderRepository.save(new Builder("maven", "3.8", List.of(language1)));
        Configuration configuration1 = configurationRepository.save(new Configuration(
                "Config1", "1.0", language1, builder1,
                "Standart java", "/path/to/Dockerfile"
        ));
        Configuration configuration2 = configurationRepository.save(new Configuration(
                 "Config2", "1.5", language2, builder2,
                "Updated java", "/updated/path/to/Dockerfile"
        ));
        List<Configuration> configurations = configurationRepository.findAll();

        assertEquals(2, configurations.size());

        assertEquals(configuration1, configurations.get(0));
        assertEquals(language1, configuration1.getLanguage());
        assertEquals(builder1, configuration1.getBuilder());
        assertEquals(configuration1.getDescription(), configurations.get(0).getDescription());
        assertEquals(configuration1.getDockerFile(), configurations.get(0).getDockerFile());

        assertEquals(configuration2, configurations.get(1));
        assertEquals(language2, configuration2.getLanguage());
        assertEquals(builder2, configuration2.getBuilder());
        assertEquals(configuration2.getDescription(), configurations.get(1).getDescription());
        assertEquals(configuration2.getDockerFile(), configurations.get(1).getDockerFile());
    }

}
