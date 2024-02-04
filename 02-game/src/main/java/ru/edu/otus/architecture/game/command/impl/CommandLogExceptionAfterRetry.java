package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.edu.otus.architecture.game.command.Command;

@Slf4j
@RequiredArgsConstructor
public class CommandLogExceptionAfterRetry implements Command {
    private final Exception exception;

    @Override
    public void execute() {
        log.error("Retries not succeeded", exception);
    }
}
