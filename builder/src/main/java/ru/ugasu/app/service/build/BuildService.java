package ru.ugasu.app.service.build;

import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.service.io.load.AppRepositoryLoader;

import java.util.Optional;

// TODO добавить инспекцию лога
public interface BuildService {

    /**
     * Сборка выполняется асинхронно, т.к. может занять продолжительное время.
     * Для этого задача по сборке помещается в пул потоков.
     *
     * Процесс сборки проекта (в идеальном случае):
     * 1. Скачивание проекта
     * @see AppRepositoryLoader
     * 2. Разархивация проекта
     * @see ru.ugasu.app.service.io.archive.DecompressService
     * 3. Копируем подготовленный Dockerfile
     * 4. Запуск сборки проекта c нужными аргументами
     *
     * @param project собираемый проект
     * @return объект с состоянием начатой сборки. Далее статус сборки фиксируется в БД
     */
    Build start(Project project);

    /**
     * Получение информации о сборке
     * @param project собираемый проект
     * @return объект состояния сборки или null если сборка данного проекта не начиналась
     */
    Optional<Build> getBuild(Project project);

}
