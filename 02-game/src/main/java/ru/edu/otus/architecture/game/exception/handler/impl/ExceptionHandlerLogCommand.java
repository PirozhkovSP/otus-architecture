package ru.edu.otus.architecture.game.exception.handler.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandLogException;
import ru.edu.otus.architecture.game.exception.handler.ExceptionHandler;

import java.util.Collection;

@RequiredArgsConstructor
public class ExceptionHandlerLogCommand implements ExceptionHandler {
    private final Collection<Command> queue;

    @Override
    public Command handle(Exception exception, Command command) {
        return () -> queue.add(new CommandLogException(exception));
    }
}
