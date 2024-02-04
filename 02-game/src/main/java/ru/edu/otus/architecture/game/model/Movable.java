package ru.edu.otus.architecture.game.model;


import ru.edu.otus.architecture.game.model.impl.Vector;

public interface Movable {
    Vector getPosition();
    void setPosition(Vector position);
    Vector getVelocity();
}
