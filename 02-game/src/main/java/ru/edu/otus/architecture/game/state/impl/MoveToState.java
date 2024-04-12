package ru.edu.otus.architecture.game.state.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.state.State;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class MoveToState implements State {
    private final BlockingQueue<Command> queue;
    private final BlockingQueue<Command> otherQueue;

    @Override
    public State next() {
        Command command = queue.poll();
        while (command != null) {
            CountDownLatch countDownLatch = IoC.resolve("Latch");
            countDownLatch.countDown();
            otherQueue.add(command);
            command = queue.poll();
        }
        return null;
    }
}
