package ru.ugasu.app.service;

import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.run.App;
import ru.ugasu.app.service.command.CommandInfo;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Сервис по работе с запускаемым приложением
 */
public interface AppService {

    /**
     * Запускает контейнер, но НЕ приложение внутри него
     * @param project проект, на основе которого будет создан контейнер
     * @return объект запущенного приложения
     */
    App start(Project project);

    /**
     * Копирует файл в контейнер
     * @param app контейнер, в который копируем
     * @param content содержимое, которое копируемся
     * @param file путь в контейнере
     * @return результат запуска
     */
    CommandInfo copyIn(App app, InputStream content, Path file);

    /**
     * Копирует файл из контейнера. Если копирование происходит удачно, то файл находится по относительному
     * пути path. Путь задается относительно директории приложения
     * @param app контейнер, в который копируем
     * @param file путь в контейнере
     * @return результат копирования
     */
    CommandInfo copyFrom(App app, Path file);

    /**
     * Уничтожает контейнер
     * @param app приложение, которое находится в контейнере
     * @return информация об уничтожение контейнера
     */
    CommandInfo kill(App app);

}
