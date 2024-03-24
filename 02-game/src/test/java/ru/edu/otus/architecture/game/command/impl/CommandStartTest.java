package ru.edu.otus.architecture.game.command.impl;

import static java.lang.Thread.sleep;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.handler.ExceptionHandler;
import ru.edu.otus.architecture.game.executor.CommandExecutor;
import ru.edu.otus.architecture.game.executor.impl.CommandExecutorQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class CommandStartTest {
    @Test
    void startTest() throws InterruptedException {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        CommandExecutor commandExecutor = new CommandExecutorQueue(queue, exceptionHandler);

        Command mockCommand = mock(Command.class);
        queue.add(mockCommand);

        Command command = new CommandStart(commandExecutor);
        command.execute();
        sleep(1000);

        verify(mockCommand, times(1)).execute();
    }

}