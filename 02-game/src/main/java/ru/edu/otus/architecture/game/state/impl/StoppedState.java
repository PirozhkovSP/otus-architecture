package ru.edu.otus.architecture.game.state.impl;


import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.state.State;

import java.util.concurrent.CountDownLatch;

public class StoppedState implements State {
    @Override
    public State next() {
        CountDownLatch countDownLatch = IoC.resolve("Latch");
        countDownLatch.countDown();
        return null;
    }
}
