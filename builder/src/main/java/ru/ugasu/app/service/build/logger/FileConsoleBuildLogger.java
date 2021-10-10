package ru.ugasu.app.service.build.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class FileConsoleBuildLogger implements BuildLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileConsoleBuildLogger.class.getSimpleName());

    private final List<String> buffer = new ArrayList<>();

    private final String logPath;

    public FileConsoleBuildLogger(String logPath) {
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
            buffer.forEach(LOGGER::info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
