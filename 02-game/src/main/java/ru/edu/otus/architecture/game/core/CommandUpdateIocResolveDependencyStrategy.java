package ru.edu.otus.architecture.game.core;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public class CommandUpdateIocResolveDependencyStrategy implements Command {
    private final BiFunction<String, Object[], Object> updater;
    @Override
    public void execute() {
        IoC.setStrategy(updater);
    }
}
