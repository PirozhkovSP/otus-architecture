package ru.edu.otus.architecture.game.executor.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.executor.CommandExecutor;
import ru.edu.otus.architecture.game.state.State;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

@RequiredArgsConstructor
public class CommandExecutorState implements CommandExecutor {
    private Future<?> future;
    private volatile boolean stopped = false;
    private Runnable behaviour = () -> {
        State state = IoC.resolve("State");
        while (state != null && !stopped) {
            State finalState = state;
            Function<Object[], Object> func = (args) -> finalState.next();
            ((Command) IoC.resolve("IoC.Register", "State", func)).execute();
            state = IoC.resolve("State");
            System.out.println();
        }
    };

    @Override
    public void start() {
        ExecutorService executorService = IoC.resolve("ExecutorService");
        future = executorService.submit(behaviour);
    }

    @Override
    public void stop() {
        ExecutorService executorService = IoC.resolve("ExecutorService");
        updateBehaviour(() -> {});
    }

    @Override
    public void updateBehaviour(Runnable action) {
        stopped = true;
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopped = false;
        behaviour = action;
        ExecutorService executorService = IoC.resolve("ExecutorService");
        future = executorService.submit(behaviour);
    }
}
