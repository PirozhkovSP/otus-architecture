package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.executor.CommandExecutor;
import ru.edu.otus.architecture.game.state.impl.NormalState;

import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

@RequiredArgsConstructor
public class CommandRun implements Command {
    private final CommandExecutor commandExecutor;
    private final BlockingQueue<Command> queue;
    @Override
    public void execute() {
        Function<Object[], Object> func = args -> new NormalState(queue);
        ((Command) IoC.resolve("IoC.Register", "State", func)).execute();
        Function<Object[], Object> funcC = args -> commandExecutor;
        ((Command) IoC.resolve("IoC.Register", "CommandExecutor", funcC)).execute();
        commandExecutor.start();
    }
}
