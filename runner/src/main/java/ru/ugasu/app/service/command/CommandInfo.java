package ru.ugasu.app.service.command;

import ru.ugasu.app.model.TimeRanged;

/**
 * Отображает информацию о команде
 */
public class CommandInfo extends TimeRanged {

    /**
     * Сообщение. Здесь будет лог ошибки, если что-то пошло не так
     */
    private String message;

    /**
     * Статус команды
     */
    private CommandStatus commandStatus;

    public CommandInfo() { }

    public CommandInfo(String message, CommandStatus commandStatus) {
        this.message = message;
        this.commandStatus = commandStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
    }
}
