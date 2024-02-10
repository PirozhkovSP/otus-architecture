package ru.edu.otus.architecture.game.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.command.CommandInit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

class IoCTest {

    @Test
    @DisplayName("По скопу на поток")
    void eachThreadShouldHaveOunScope() throws InterruptedException, ExecutionException {
        var pool = Executors.newFixedThreadPool(2);
        Collection<Callable<Object>> callables = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            callables.add(() -> {
                new CommandInit().execute();
                Object scope = IoC.resolve("IoC.Scope.Create");
                ((Command) IoC.resolve("IoC.Scope.Current.Set", scope)).execute();
                Function<Object[], Object> func = (args) -> (Object) finalI;
                ((Command) IoC.resolve("IoC.Register", "someDependency" + finalI, func)).execute();
                return scope;
            });
        }
        Collection<Future<Object>> scopesFutures = pool.invokeAll(callables);
        List<Object> scopes = new ArrayList<>();
        for (Future<Object> scopesFuture : scopesFutures) {
            scopes.add(scopesFuture.get());
        }
        Object scope1 = scopes.get(0);
        Object scope2 = scopes.get(1);
        assertNotEquals(scope1, scope2);

        ((Command) IoC.resolve("IoC.Scope.Current.Set", scope1)).execute();
        assertThrows(RuntimeException.class, () -> IoC.resolve("someDependency1"));
        assertDoesNotThrow(() -> IoC.resolve("someDependency0"));

        ((Command) IoC.resolve("IoC.Scope.Current.Set", scope2)).execute();
        assertThrows(RuntimeException.class, () -> IoC.resolve("someDependency0"));
        assertDoesNotThrow(() -> IoC.resolve("someDependency1"));
    }

    @Nested
    class NestedTestBlock {
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
            Function<Object[], Object> func = args -> (Integer) args[0] + (Integer) args[1] + (Integer) args[2];
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
}