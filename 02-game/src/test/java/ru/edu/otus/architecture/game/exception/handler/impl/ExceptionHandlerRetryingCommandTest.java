package ru.edu.otus.architecture.game.exception.handler.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandRetry;

import java.util.ArrayList;
import java.util.List;

class ExceptionHandlerRetryingCommandTest {

    @Test
    void testRetry() {
        List<Command> commands = new ArrayList<>();

        Command command = () -> {};
        new ExceptionHandlerRetryingCommand(commands).handle(new RuntimeException("Test"), command).execute();

        assertEquals(1, commands.size());
        assertEquals(CommandRetry.class, commands.get(0).getClass());
    }

}