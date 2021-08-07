package ru.ugasu.app.service.logger;

/**
 * Логгер используемый при работе с контейнером
 */
public interface AppLogger {
    void append(String line);
    void flush();
}
