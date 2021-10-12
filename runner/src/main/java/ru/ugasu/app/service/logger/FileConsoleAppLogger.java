package ru.ugasu.app.service.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Логгер буфферизирует данные и когда вызывается flush() сохраняет данные
 */
@Component
@Scope("prototype")
public class FileConsoleAppLogger implements AppLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileConsoleAppLogger.class.getSimpleName());

    private final String path;

    private List<String> buffer = new CopyOnWriteArrayList<>();

    public FileConsoleAppLogger(String path) {
        this.path = path;
    }

    @Override
    public void append(String line) {
        buffer.add(line);
    }

    @Override
    public void flush() {
        try {
            Path path = Path.of(this.path);
            Files.deleteIfExists(path);
            Files.write(path, buffer);
            buffer.forEach(LOGGER::info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
