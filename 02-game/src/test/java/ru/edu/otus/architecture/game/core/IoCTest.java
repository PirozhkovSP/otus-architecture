package ru.edu.otus.architecture.game.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.command.CommandInit;

import java.util.Map;
import java.util.function.Function;

class IoCTest {

    @BeforeAll
    static void init() {
        new CommandInit().execute();
        Map<String, Function<Object[], Object>> scope = IoC.resolve("IoC.Scope.Create");
        Command command = IoC.resolve("IoC.Scope.Current.Set", scope);
        command.execute();
    }

    @Test
    void shouldResolveDependencyInCurrentScope() {
        Function<Object[], Object> func = args -> ((Integer)args[0]) + (Integer) args[1] + (Integer) args[2];
        Command command = IoC.resolve("IoC.Register", "someDependency", func);
        command.execute();

        assertEquals(6, (Integer) IoC.resolve("someDependency", 1, 2, 3));
    }
}