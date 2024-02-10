package ru.edu.otus.architecture.game.core.command;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class CommandRegisterDependency implements Command {
    private final String dependencyName;
    private final Function<Object[], Object> dependencyResolverStrategy;

    @Override
    public void execute() {
        Map<String, Function<Object[], Object>> currentScope = IoC.resolve("IoC.Scope.Current");
        currentScope.put(dependencyName, dependencyResolverStrategy);
    }
}
