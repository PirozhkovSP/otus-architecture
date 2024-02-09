package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;

import java.util.Collection;

@RequiredArgsConstructor
public class CommandMacro implements Command {
    private final Collection<Command> commands;

    @Override
    public void execute() {
        commands.forEach(Command::execute);
    }
}
