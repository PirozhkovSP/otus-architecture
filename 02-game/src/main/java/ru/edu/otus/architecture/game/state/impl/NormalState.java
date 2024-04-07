package ru.edu.otus.architecture.game.state.impl;

import static java.util.Optional.ofNullable;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.state.State;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class NormalState implements State {
    private final BlockingQueue<Command> queue;

    @Override
    public State next() {
        CountDownLatch countDownLatch = IoC.resolve("Latch");
        countDownLatch.countDown();
        ofNullable(queue.poll()).ifPresent(Command::execute);
        return this;
    }
}
