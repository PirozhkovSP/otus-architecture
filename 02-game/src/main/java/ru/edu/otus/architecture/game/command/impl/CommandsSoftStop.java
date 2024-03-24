package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.executor.CommandExecutor;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class CommandsSoftStop implements Command {
    private final CommandExecutor commandExecutor;
    private final BlockingQueue<Command> commands;
    @Override
    public void execute() {
        if (!commands.isEmpty()) {
            commands.add(new CommandsSoftStop(commandExecutor, commands));
        } else {
            commandExecutor.stop();
        }
    }
}
