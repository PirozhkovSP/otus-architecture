package ru.edu.otus.architecture.game.executor;

public interface CommandExecutor {
    void start();
    void stop();
    void updateBehaviour(Runnable action);
}
