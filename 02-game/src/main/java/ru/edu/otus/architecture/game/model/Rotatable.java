package ru.edu.otus.architecture.game.model;

public interface Rotatable {
    int getDirection();
    int getAngularVelocity();
    int getDirectionNumbers();
    void setDirection(int direction);
}
