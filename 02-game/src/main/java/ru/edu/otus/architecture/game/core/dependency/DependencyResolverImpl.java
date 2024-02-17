package ru.edu.otus.architecture.game.core.dependency;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class DependencyResolverImpl implements DependencyResolver {

    private final Object scope;

    @Override
    public <T> T resolve(String dependency, Object... args) {
        Map<String, Function<Object[], Object>> currentScope = (Map<String, Function<Object[], Object>>) scope;

        while (true) {
            Function<Object[], Object> dependencyResolverStrategy = currentScope.get(dependency);
            if (dependencyResolverStrategy != null) {
                return (T) dependencyResolverStrategy.apply(args);
            } else {
                currentScope =
                        (Map<String, Function<Object[], Object>>) currentScope.get("IoC.Scope.Parent").apply(args);
            }
        }
    }
}
