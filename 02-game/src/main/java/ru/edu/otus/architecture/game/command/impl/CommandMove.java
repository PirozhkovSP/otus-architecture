package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.model.Movable;
import ru.edu.otus.architecture.game.model.impl.Vector;

@RequiredArgsConstructor
public class CommandMove implements Command {
    private final Movable movable;

    @Override
    public void execute() {
        movable.setPosition(new Vector(
                movable.getPosition().x() + movable.getVelocity().x(),
                movable.getPosition().y() + movable.getVelocity().y()
        ));
    }
}
