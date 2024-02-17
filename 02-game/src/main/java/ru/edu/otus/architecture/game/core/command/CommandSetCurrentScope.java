package ru.edu.otus.architecture.game.core.command;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;

@RequiredArgsConstructor
public class CommandSetCurrentScope implements Command {
    private final Object scope;
    @Override
    public void execute() {
        CommandInit.setCurrentScope(scope);
    }
}
