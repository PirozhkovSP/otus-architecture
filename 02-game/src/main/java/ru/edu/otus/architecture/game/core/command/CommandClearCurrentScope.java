package ru.edu.otus.architecture.game.core.command;

import ru.edu.otus.architecture.game.command.Command;

public class CommandClearCurrentScope implements Command {
    @Override
    public void execute() {
        CommandInit.setCurrentScope(null);
    }
}
