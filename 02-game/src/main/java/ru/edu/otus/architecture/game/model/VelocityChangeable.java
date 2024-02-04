package ru.edu.otus.architecture.game.model;

import ru.edu.otus.architecture.game.model.impl.Vector;

public interface VelocityChangeable {
    Vector getVelocity();
    void setVelocity(Vector velocity);
    int getDirection();
    int getDirectionNumbers();
}
