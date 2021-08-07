package ru.ugasu.app.service.build.logger;

public interface BuildLogger {
    void append(String line);
    void flush();
}
