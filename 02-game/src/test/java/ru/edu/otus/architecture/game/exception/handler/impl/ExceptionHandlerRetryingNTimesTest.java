package ru.edu.otus.architecture.game.exception.handler.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandRetryNTimes;

import java.util.ArrayList;
import java.util.List;

class ExceptionHandlerRetryingNTimesTest {

    @Test
    void testRetry() {
        List<Command> commands = new ArrayList<>();

        Command command = () -> {};
        new ExceptionHandlerRetryingNTimes(commands, 1).handle(new RuntimeException("Test"), command).execute();

        assertEquals(1, commands.size());
        Command commandRetry = commands.get(0);
        assertEquals(CommandRetryNTimes.class, commandRetry.getClass());
    }

}