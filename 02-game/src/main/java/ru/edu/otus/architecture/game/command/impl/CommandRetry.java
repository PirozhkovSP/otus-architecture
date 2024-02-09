package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;

@RequiredArgsConstructor
public class CommandRetry implements Command {
    private final Command commandForRetry;
    @Override
    public void execute() {
        commandForRetry.execute();
    }
}
