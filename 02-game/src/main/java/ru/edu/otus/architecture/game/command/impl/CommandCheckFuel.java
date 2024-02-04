package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.CommandException;
import ru.edu.otus.architecture.game.model.FuelConsumable;

@RequiredArgsConstructor
public class CommandCheckFuel implements Command {
    private final FuelConsumable consumable;

    @Override
    public void execute() {
        if (consumable.getRemaining() < consumable.getConsumption())
            throw new CommandException("Not enough fuel");
    }
}
