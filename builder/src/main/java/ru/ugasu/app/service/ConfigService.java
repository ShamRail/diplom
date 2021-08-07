package ru.ugasu.app.service;

import java.nio.file.Path;
import java.util.Optional;

public interface ConfigService {
    Optional<Path> uploadDockerfile(Integer configID);
}
