package ru.edu.otus.architecture.game.exception.handler.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandLogException;

import java.util.ArrayList;
import java.util.List;

class ExceptionHandlerLogCommandTest {

    @Test
    void handleExceptionTest() {
        List<Command> commands = new ArrayList<>();

        new ExceptionHandlerLogCommand(commands).handle(new RuntimeException("Test"), null).execute();

        assertEquals(1, commands.size());
        assertEquals(CommandLogException.class, commands.get(0).getClass());
    }

}