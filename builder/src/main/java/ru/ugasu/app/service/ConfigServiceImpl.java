package ru.ugasu.app.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Value("${ADMIN_HOST}")
    private String adminHost;

    private final RestTemplate restTemplate;

    public ConfigServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Path> uploadDockerfile(Integer configID) {
        try {
            String json = restTemplate.getForObject(adminHost + "/configuration/" + configID, String.class);
            JSONObject object = new JSONObject(json);
            String dockerContent = object.getString("dockerFile");
            Path path = Files.createTempDirectory(UUID.randomUUID().toString());
            Path targetPath = Path.of(path.toString(), "Dockerfile");
            Files.writeString(targetPath, dockerContent);
            return Optional.of(targetPath);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
