package ru.edu.otus.architecture.game.core.dependency;

public interface DependencyResolver {
    <T> T resolve(String key, Object... args);
}
