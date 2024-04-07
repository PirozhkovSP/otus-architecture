package ru.edu.otus.architecture.game.executor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandHardStop;
import ru.edu.otus.architecture.game.command.impl.CommandMoveTo;
import ru.edu.otus.architecture.game.command.impl.CommandRun;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.core.command.CommandInit;
import ru.edu.otus.architecture.game.executor.CommandExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

class CommandExecutorStateTest {
    @BeforeEach
    void init() {
        new CommandInit().execute();
        Function<Object[], Object> func = args -> Executors.newFixedThreadPool(5);
        ((Command) IoC.resolve("IoC.Register", "ExecutorService", func)).execute();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Function<Object[], Object> funcL = args -> countDownLatch;
        ((Command) IoC.resolve("IoC.Register", "Latch", funcL)).execute();
    }
    @Test
    void stateMachineStopTest() throws InterruptedException {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);

        CommandExecutor commandExecutor = new CommandExecutorState();

        Command moveTo = new CommandHardStop(commandExecutor);
        queue.add(moveTo);

        Command mock1 = mock(Command.class);
        queue.add(mock1);

        Command mock2 = mock(Command.class);
        queue.add(mock2);

        Command mock3 = mock(Command.class);
        queue.add(mock3);

        new CommandRun(commandExecutor, queue).execute();

        CountDownLatch countDownLatch = IoC.resolve("Latch");
        countDownLatch.await(3, TimeUnit.SECONDS);
        assertEquals(2, countDownLatch.getCount());
    }

    @Test
    void stateMachineMoveToTest() throws InterruptedException {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        BlockingQueue<Command> newQueue = new ArrayBlockingQueue<>(10, true);

        Command moveTo = new CommandMoveTo(queue, newQueue);
        queue.add(moveTo);

        Command mock1 = mock(Command.class);
        queue.add(mock1);

        Command mock2 = mock(Command.class);
        queue.add(mock2);

        Command mock3 = mock(Command.class);
        queue.add(mock3);

        CommandExecutor commandExecutor = new CommandExecutorState();

        new CommandRun(commandExecutor, queue).execute();
        CountDownLatch countDownLatch = IoC.resolve("Latch");
        countDownLatch.await(3, TimeUnit.SECONDS);
        assertEquals(0, countDownLatch.getCount());
    }

}