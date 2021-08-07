package ru.ugasu.app.service.command;

/**
 * Отображет статус команды
 * STARTED - началось выполнение команды
 * COMPLETE - команда завершилась успешно
 * FALLEN - не удалось выполнить команду
 */
public enum  CommandStatus {
    COMPLETE, FALLEN, STARTED
}
