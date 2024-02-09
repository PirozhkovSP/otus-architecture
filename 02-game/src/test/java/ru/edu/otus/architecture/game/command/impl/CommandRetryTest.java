package ru.edu.otus.architecture.game.command.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;

class CommandRetryTest {

    @Test
    void retryTest() {
        Command commandForRetry = mock(Command.class);
        doNothing().when(commandForRetry).execute();

        new CommandRetry(commandForRetry).execute();

        verify(commandForRetry, times(1)).execute();
    }

}