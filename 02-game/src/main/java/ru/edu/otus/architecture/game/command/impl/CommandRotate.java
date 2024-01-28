package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.model.Rotatable;

@RequiredArgsConstructor
public class CommandRotate implements Command {
    private final Rotatable rotatable;

    @Override
    public void execute() {
        rotatable.setDirection(
                (rotatable.getDirection() + rotatable.getAngularVelocity()) % rotatable.getDirectionNumbers()
        );
    }
}
