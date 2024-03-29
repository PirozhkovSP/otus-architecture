package ru.edu.otus.architecture.game.executor.impl;

import static java.util.Optional.ofNullable;

import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.handler.ExceptionHandler;
import ru.edu.otus.architecture.game.executor.CommandExecutor;

import java.util.concurrent.BlockingQueue;

public class CommandExecutorQueue implements CommandExecutor {
    private final BlockingQueue<Command> commands;
    private final ExceptionHandler exceptionHandler;
    private final Thread thread;
    private Runnable behaviour;
    private volatile boolean stopped = false;

    public CommandExecutorQueue(BlockingQueue<Command> commands, ExceptionHandler exceptionHandler) {
        this.commands = commands;
        this.exceptionHandler = exceptionHandler;
        behaviour = () -> {
            Command cmd = this.commands.poll();
            try {
                ofNullable(cmd).ifPresent(Command::execute);
            } catch (Exception e) {
                this.exceptionHandler.handle(e, cmd).execute();
            }
        };
        this.thread = new Thread(() -> {
            while (!stopped) {
                behaviour.run();
            }
        });
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public void updateBehaviour(Runnable action) {
        behaviour = action;
    }
}
