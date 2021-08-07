package ru.ugasu.app.service.build.logger;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class FileBuildLogger implements BuildLogger {

    private final List<String> buffer = new ArrayList<>();

    private final String logPath;

    public FileBuildLogger(String logPath) {
        this.logPath = logPath;
    }

    @Override
    public void append(String line) {
        buffer.add(line);
    }

    @Override
    public void flush() {
        try {
            Files.write(Path.of(logPath), buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
