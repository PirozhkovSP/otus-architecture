package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.executor.CommandExecutor;

@RequiredArgsConstructor
public class CommandStart implements Command {
    private final CommandExecutor commandExecutor;
    @Override
    public void execute() {
        commandExecutor.start();
    }
}
