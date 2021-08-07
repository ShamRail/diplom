package ru.ugasu.app.service.logger;

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
public class FileAppLogger implements AppLogger {

    private final String path;

    private List<String> buffer = new CopyOnWriteArrayList<>();

    public FileAppLogger(String path) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
