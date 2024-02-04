package ru.edu.otus.architecture.game.exception.handler.impl;

import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandLogException;
import ru.edu.otus.architecture.game.exception.handler.ExceptionHandler;

public class ExceptionHandlerRetryingAndLog implements ExceptionHandler {
    @Override
    public Command handle(Exception exception, Command command) {
        try {
            command.execute();
            return () -> {};
        } catch (Exception e) {
            return new CommandLogException(e);
        }
    }
}
