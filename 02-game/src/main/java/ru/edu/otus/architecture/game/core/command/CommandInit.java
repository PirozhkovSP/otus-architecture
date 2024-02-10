package ru.edu.otus.architecture.game.core.command;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.dependency.DependencyResolver;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.core.dependency.DependencyResolverImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
public class CommandInit implements Command {
    private static final ThreadLocal<Object> currentScope = new ThreadLocal<>();
    private static final Map<String, Function<Object[], Object>> ROOT_SCOPE = new ConcurrentHashMap<>();
    private static volatile boolean alreadyInitialized = false;

    @Override
    public void execute() {
        if (alreadyInitialized) {
            return;
        }
        synchronized (ROOT_SCOPE) {
            ROOT_SCOPE.put("IoC.Scope.Current.Set", (args) -> new CommandSetCurrentScope(args[0]));

            ROOT_SCOPE.put("IoC.Scope.Current.Clear", (args) -> new CommandClearCurrentScope());

            ROOT_SCOPE.put("IoC.Scope.Current", (args) -> currentScope.get() != null
                    ? currentScope.get()
                    : ROOT_SCOPE
            );

            ROOT_SCOPE.put("IoC.Scope.Parent",
                    (args) -> {throw new RuntimeException("The root scope has no a parent scope.");});

            ROOT_SCOPE.put("IoC.Scope.Create.Empty",
                    (args) -> new HashMap<String, Function<Object[], Object>>());

            ROOT_SCOPE.put("IoC.Scope.Create",
                    (args) -> {
                        Map<String, Function<Object[], Object>> creatingScope =
                                IoC.resolve("IoC.Scope.Create.Empty");

                        if (args.length > 0) {
                            Object parentScope = args[0];
                            creatingScope.put("IoC.Scope.Parent", (Object[] arg) -> parentScope);
                        } else {
                            Map<String, Function<Object[], Object>> parentScope =
                                    IoC.resolve("IoC.Scope.Current");
                            creatingScope.put("IoC.Scope.Parent", (Object[] arg) -> parentScope);
                        }
                        return creatingScope;
                    });

            ROOT_SCOPE.put("IoC.Register",
                    (args) -> new CommandRegisterDependency((String) args[0],
                            (Function<Object[], Object>) args[1]));

            BiFunction<String, Object[], Object> strategySupplier =
                    (String dependency, Object[] arg) -> {
                        var scope = currentScope.get() != null ? currentScope.get() : ROOT_SCOPE;
                        DependencyResolver dependencyResolver = new DependencyResolverImpl(scope);

                        return dependencyResolver.resolve(dependency, arg);
                    };
            Command command = IoC.resolve("Update Ioc Resolve Dependency Strategy", strategySupplier);
            command.execute();

            alreadyInitialized = true;
        }
    }

    static void setCurrentScope(Object scope) {
        currentScope.set(scope);
    }
}
