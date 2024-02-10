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
        Function<Object[], Object> func = (Object[] args) -> 1;
        Command command = IoC.resolve("IoC.Register", "someDependency", func);

        assertEquals(1, (Integer) IoC.resolve("someDependency"));
    }

}