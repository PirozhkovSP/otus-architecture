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

class CommandHardStopTest {

    @Test
    void hardStopTest() throws InterruptedException {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        CommandExecutor commandExecutor = new CommandExecutorQueue(queue, exceptionHandler);

        Command mockCommand1 = mock(Command.class);
        Command stopCommand = new CommandHardStop(commandExecutor);
        Command mockCommand2 = mock(Command.class);
        Command mockCommand3 = mock(Command.class);
        queue.add(mockCommand1);
        queue.add(stopCommand);
        queue.add(mockCommand2);
        queue.add(mockCommand3);

        Command command = new CommandStart(commandExecutor);
        command.execute();

        sleep(1000);

        verify(mockCommand1, times(1)).execute();
        verify(mockCommand2, times(0)).execute();
        verify(mockCommand3, times(0)).execute();
    }
}