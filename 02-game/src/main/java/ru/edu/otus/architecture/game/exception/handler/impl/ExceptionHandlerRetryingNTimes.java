package ru.edu.otus.architecture.game.exception.handler.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandRetryNTimes;
import ru.edu.otus.architecture.game.exception.handler.ExceptionHandler;

import java.util.Collection;

@RequiredArgsConstructor
public class ExceptionHandlerRetryingNTimes implements ExceptionHandler {
    private final Collection<Command> queue;
    private final int retries;

    @Override
    public Command handle(Exception exception, Command command) {
        return () -> queue.add(new CommandRetryNTimes(command, retries, queue));
    }
}
