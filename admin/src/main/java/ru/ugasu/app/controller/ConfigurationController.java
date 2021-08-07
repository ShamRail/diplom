package ru.ugasu.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.Builder;
import ru.ugasu.app.model.Configuration;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.model.dto.ConfigurationDTO;
import ru.ugasu.app.repo.BuilderRepository;
import ru.ugasu.app.repo.ConfigurationRepository;
import ru.ugasu.app.repo.LanguageRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private BuilderRepository builderRepository;

    @Value("${app.configurations}")
    private String configsPath;

    @PostConstruct
    public void initDir() {
        try {
            Files.createDirectories(Paths.get(configsPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @PostMapping
    public Configuration save(@RequestBody ConfigurationDTO configurationDTO) throws IOException {
        validateFields(configurationDTO);
        Language language = languageRepository.findById(configurationDTO.getLanguageID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid language ID"));
        Builder builder = builderRepository.findById(configurationDTO.getBuilderID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder ID"));
        Path targetDockerFile = createAndWriteDockerFile(configurationDTO);
        return configurationRepository.save(mapFields(language, builder, targetDockerFile, configurationDTO));
    }

    private void validateFields(ConfigurationDTO configurationDTO) {
        if (configurationDTO.getLanguageID() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid language ID");
        }
        if (configurationDTO.getBuilderID() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder ID");
        }
        if (configurationDTO.getDockerContent() == null || configurationDTO.getDockerContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuration must be not empty");
        }
    }

    private Configuration mapFields(Language language, Builder builder, Path targetDockerFile, ConfigurationDTO configurationDTO) {
        Configuration configuration = new Configuration();
        configuration.setLanguage(language);
        configuration.setBuilder(builder);
        configuration.setDescription(configurationDTO.getDescription());
        configuration.setDockerFile(targetDockerFile.toAbsolutePath().toString());
        configuration.setName(configurationDTO.getName());
        configuration.setVersion(configurationDTO.getVersion());
        return configuration;
    }

    private Path createAndWriteDockerFile(ConfigurationDTO configurationDTO) throws IOException {
        Path targetDockerFile = Path.of(configsPath, UUID.randomUUID().toString() + System.currentTimeMillis(), "Dockerfile");
        Files.createDirectory(targetDockerFile.getParent());
        Files.createFile(targetDockerFile);
        Files.write(targetDockerFile, configurationDTO.getDockerContent().getBytes());
        return targetDockerFile;
    }

    @PutMapping("/{id}")
    public void put(@PathVariable int id, @RequestBody ConfigurationDTO configurationDTO) throws IOException {
        validateFields(configurationDTO);
        Configuration configuration = configurationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid language ID"));
        Language language = languageRepository.findById(configurationDTO.getLanguageID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid language ID"));
        Builder builder = builderRepository.findById(configurationDTO.getBuilderID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder ID"));

        Path dockerFilePath = Path.of(configuration.getDockerFile());
        Files.deleteIfExists(dockerFilePath);
        Files.deleteIfExists(dockerFilePath.getParent());

        Path targetDockerFilePath = createAndWriteDockerFile(configurationDTO);

        Configuration updated = mapFields(language, builder, targetDockerFilePath, configurationDTO);
        updated.setId(configuration.getId());
        configurationRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws IOException {
        Optional<Configuration> configurationOptional = configurationRepository.findById(id);
        if (configurationOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid config id");
        }
        Path dockerFilePath = Path.of(configurationOptional.get().getDockerFile());
        Files.deleteIfExists(dockerFilePath);
        Files.deleteIfExists(dockerFilePath.getParent());
        configurationRepository.delete(configurationOptional.get());
    }

    @GetMapping("/all")
    public List<Configuration> findAll() {
        return configurationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Configuration findById(@PathVariable int id) throws IOException {
        Configuration configuration = configurationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid config id"));
        if (Files.exists(Path.of(configuration.getDockerFile()))) {
            configuration.setDockerFile(Files.readString(Path.of(configuration.getDockerFile())));
        } else {
            configuration.setDockerFile("");
        }
        return configuration;
    }


}
