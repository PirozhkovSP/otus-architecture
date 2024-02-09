package ru.edu.otus.architecture.game.exception.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.edu.otus.architecture.game.exception.handler.ExceptionHandlerChoose.registerHandler;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandLogExceptionAfterRetry;
import ru.edu.otus.architecture.game.command.impl.CommandRetryNTimes;
import ru.edu.otus.architecture.game.exception.RetriesLimitExceededException;
import ru.edu.otus.architecture.game.exception.handler.impl.ExceptionHandlerLogCommand;
import ru.edu.otus.architecture.game.exception.handler.impl.ExceptionHandlerRetryingNTimes;
import ru.edu.otus.architecture.game.executor.CommandExecutor;
import ru.edu.otus.architecture.game.executor.impl.CommandExecutorQueue;

import java.util.ArrayDeque;
import java.util.Deque;

class ExceptionHandlerChooseTest {

    @Test
    void testRetryAndLogStrategy() {
        Deque<Command> queue = new ArrayDeque<>();

        ExceptionHandlerChoose exceptionHandler = new ExceptionHandlerChoose();

        Command throwingCommand = mock(Command.class);
        doThrow(new RuntimeException("Test")).when(throwingCommand).execute();

        ExceptionHandlerRetryingNTimes exceptionHandlerRetryingNTimes = mock(ExceptionHandlerRetryingNTimes.class);
        CommandRetryNTimes commandRetryNTimes = mock(CommandRetryNTimes.class);
        when(exceptionHandlerRetryingNTimes.handle(any(), any())).thenReturn(() -> queue.add(commandRetryNTimes));
        doThrow(new RetriesLimitExceededException()).when(commandRetryNTimes).execute();

        ExceptionHandlerLogCommand exceptionHandlerLogCommand = mock(ExceptionHandlerLogCommand.class);
        CommandLogExceptionAfterRetry commandLogExceptionAfterRetry = mock(CommandLogExceptionAfterRetry.class);
        when(exceptionHandlerLogCommand.handle(any(), any())).thenReturn(() -> queue.add(commandLogExceptionAfterRetry));


        registerHandler(throwingCommand.getClass(), RuntimeException.class, exceptionHandlerRetryingNTimes);
        registerHandler(commandRetryNTimes.getClass(), RetriesLimitExceededException.class, exceptionHandlerLogCommand);

        CommandExecutor commandExecutor = new CommandExecutorQueue(queue, exceptionHandler);

        queue.add(throwingCommand);
        commandExecutor.execute();

        verify(commandRetryNTimes, times(1)).execute();
        verify(commandLogExceptionAfterRetry, times(1)).execute();
    }

}