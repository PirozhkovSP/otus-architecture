package ru.edu.otus.architecture.game.exception.handler.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandLogException;

class ExceptionHandlerRetryingAndLogTest {

    @Test
    void testRetry() {
        Command commandForRetry = mock(Command.class);
        new ExceptionHandlerRetryingAndLog().handle(new RuntimeException("Test"), commandForRetry).execute();

        verify(commandForRetry, times(1)).execute();
    }

    @Test
    void testLog() {
        Command commandForRetry = mock(Command.class);
        doThrow(new RuntimeException("Test")).when(commandForRetry).execute();

        Command logCommand = new ExceptionHandlerRetryingAndLog().handle(new RuntimeException("Test"), commandForRetry);

        verify(commandForRetry, times(1)).execute();
        assertEquals(CommandLogException.class, logCommand.getClass());
    }
}