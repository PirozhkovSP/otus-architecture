package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.executor.CommandExecutor;
import ru.edu.otus.architecture.game.state.State;
import ru.edu.otus.architecture.game.state.impl.MoveToState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

@RequiredArgsConstructor
public class CommandMoveTo implements Command {
    private final BlockingQueue<Command> queue;
    private final BlockingQueue<Command> newQueue;
    @Override
    public void execute() {
        CommandExecutor commandExecutor = IoC.resolve("CommandExecutor");
        ExecutorService executorService = IoC.resolve("ExecutorService");
        executorService.execute(() -> commandExecutor.updateBehaviour(() -> {
            Function<Object[], Object> func = args -> new MoveToState(queue, newQueue);
            ((Command) IoC.resolve("IoC.Register", "State", func)).execute();
            State state = IoC.resolve("State");
            while (state != null) {
                State finalState = state;
                Function<Object[], Object> funcNext = args -> finalState.next();
                ((Command) IoC.resolve("IoC.Register", "State", funcNext)).execute();
                state = IoC.resolve("State");
            }
        }));
    }
}
