package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.RetriesLimitExceededException;

import java.util.Collection;

@RequiredArgsConstructor
public class CommandRetryNTimes implements Command {
    private final Command commandForRetry;
    private final int retries;
    private final Collection<Command> commands;
    private int retried;
    @Override
    public void execute() {
        if (retried < retries) {
            try {
                commandForRetry.execute();
            } catch (Exception e) {
                commands.add(this);
            }
            retried++;
        } else {
            throw new RetriesLimitExceededException();
        }
    }
}
