package ru.edu.otus.architecture.game.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.command.CommandInit;

import java.util.Map;
import java.util.function.Function;

class IoCTest {

    @BeforeEach
    void init() {
        new CommandInit().execute();
        Map<String, Function<Object[], Object>> scope = IoC.resolve("IoC.Scope.Create");
        Command command = IoC.resolve("IoC.Scope.Current.Set", scope);
        command.execute();
    }

    @AfterEach
    void dispose() {
        Command command = IoC.resolve("IoC.Scope.Current.Clear");
        command.execute();
    }

    @Test
    @DisplayName("Проверка создания помещенного в контейнер объекта в текущем скопе")
    void shouldResolveDependencyInCurrentScope() {
        Function<Object[], Object> func = args -> (Integer)args[0] + (Integer) args[1] + (Integer) args[2];
        Command command = IoC.resolve("IoC.Register", "someDependency", func);
        command.execute();

        assertEquals(6, (Integer) IoC.resolve("someDependency", 1, 2, 3));
    }

    @Test
    @DisplayName("Выбрасывает исключение при попытке получить незарегистрированную зависимость")
    void shouldShouldThrowOnUnregisteredDependency() {
        assertThrows(RuntimeException.class, () -> IoC.resolve("unregistered"));
    }

    @Test
    @DisplayName("Должен искать зависимость в родительском скопе, если нет в текущем")
    void shouldSearchDependencyInParentScope() {
        Function<Object[], Object> func = args -> 1;
        Command register = IoC.resolve("IoC.Register", "someDependency", func);
        register.execute();

        Object iocScope = IoC.resolve("IoC.Scope.Create");
        Command set = IoC.resolve("IoC.Scope.Current.Set", iocScope);
        set.execute();

        assertEquals(iocScope, IoC.resolve("IoC.Scope.Current"));
        assertEquals(1, (Integer) IoC.resolve("someDependency"));
    }

    @Test
    @DisplayName("можно вручную задать родительский скоп")
    void parentScopeCanBeSetManuallyForCreatingScope() {
        Object scope1 = IoC.resolve("IoC.Scope.Create");
        Object scope2 = IoC.resolve("IoC.Scope.Create", scope1);

        ((Command) IoC.resolve("IoC.Scope.Current.Set", scope1)).execute();
        Function<Object[], Object> func = (args) -> (Object) 2;
        ((Command) IoC.resolve("IoC.Register", "someDependency", func)).execute();
        ((Command) IoC.resolve("IoC.Scope.Current.Set", scope2)).execute();

        assertEquals(2, (Integer) IoC.resolve("someDependency"));
    }
}