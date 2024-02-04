package ru.edu.otus.architecture.game.executor.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.handler.ExceptionHandler;
import ru.edu.otus.architecture.game.executor.CommandExecutor;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class CommandExecutorQueue implements CommandExecutor {
    private final Collection<Command> commands;
    private final ExceptionHandler exceptionHandler;

    @Override
    public void execute() {
        while (!commands.isEmpty()) {
            Iterator<Command> commandIterator = commands.iterator();
            while (commandIterator.hasNext()) {
                Command command = commandIterator.next();
                commandIterator.remove();
                try {
                    command.execute();
                } catch (Exception exception) {
                    exceptionHandler.handle(exception, command).execute();
                }
            }
        }
    }
}
