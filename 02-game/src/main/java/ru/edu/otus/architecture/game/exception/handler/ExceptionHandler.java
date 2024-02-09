package ru.edu.otus.architecture.game.exception.handler;

import ru.edu.otus.architecture.game.command.Command;

public interface ExceptionHandler {
    Command handle(Exception exception, Command command);
}
