package ru.edu.otus.architecture.game.model;

public interface FuelConsumable {
    int getRemaining();
    void setRemaining(int value);
    int getConsumption();
}
