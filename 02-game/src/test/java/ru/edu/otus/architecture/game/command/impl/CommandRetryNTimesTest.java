package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.RetriesLimitExceededException;

import java.util.ArrayList;
import java.util.List;

class CommandRetryNTimesTest {

    @Test
    @DisplayName("Command retries")
    void retryTest() {
        List<Command> commands = new ArrayList<>();

        Command commandForRetry = mock(Command.class);
        doThrow(new RuntimeException("Test")).when(commandForRetry).execute();

        CommandRetryNTimes commandRetryNTimes = new CommandRetryNTimes(commandForRetry, 1, commands);
        commandRetryNTimes.execute();

        verify(commandForRetry, times(1)).execute();
        assertEquals(1, commands.size());
        assertEquals(commandRetryNTimes, commands.get(0));
    }

    @Test
    @DisplayName("Throws exception when retries limit exceeded")
    void exceptionTest() {
        List<Command> commands = new ArrayList<>();

        Command commandForRetry = mock(Command.class);
        doNothing().when(commandForRetry).execute();

        Command retryCommand = new CommandRetryNTimes(commandForRetry, 1, commands);
        retryCommand.execute();

        assertThrows(RetriesLimitExceededException.class, retryCommand::execute);
    }
}