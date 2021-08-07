package ru.ugasu.app.service;

/**
 * STARTED - контейнер запущен. Можно выполнять команды
 * DIED - контейнер остановлен и удален
 * FALLEN - произошла ошибка при запуске или удалении
 */
public enum  AppStatus {
    STARTED, DIED, FALLEN
}
