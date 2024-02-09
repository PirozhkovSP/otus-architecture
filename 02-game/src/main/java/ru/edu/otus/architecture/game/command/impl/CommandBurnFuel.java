package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.model.FuelConsumable;

@RequiredArgsConstructor
public class CommandBurnFuel implements Command {
    private final FuelConsumable consumable;

    @Override
    public void execute() {
        consumable.setRemaining(consumable.getRemaining() - consumable.getConsumption());
    }
}
