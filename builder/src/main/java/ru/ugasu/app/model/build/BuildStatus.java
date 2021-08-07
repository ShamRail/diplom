package ru.ugasu.app.model.build;

/**
 * Статус сборки
 * STARTED - сборка началась
 * DOWNLOADING - происходит скачиваение исходного кода
 * PREPARING - происходит распаковка проекта. Выбирается, подготавливается и подкладывется Dockerfile
 * BUILDING - происходит сборка проекта
 * BUILT - проект собран
 * FALLEN - не удалось собрать проект
 */
public enum BuildStatus {
    STARTED, DOWNLOADING, PREPARING, BUILDING, BUILT, FALLEN
}
