package ru.edu.otus.architecture.game.command.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.model.VelocityChangeable;
import ru.edu.otus.architecture.game.model.impl.Vector;

@RequiredArgsConstructor
public class CommandChangeVelocity implements Command {
    private final VelocityChangeable velocityChangeable;

    @Override
    public void execute() {
        Vector currentVelocity = velocityChangeable.getVelocity();
        int angleDegrees = (360 * velocityChangeable.getDirection() /
                velocityChangeable.getDirectionNumbers());
        double angle = Math.toRadians(angleDegrees);
        double angleVelocity = Math.sqrt(Math.pow(currentVelocity.x(),2) +
                Math.pow(currentVelocity.y(),2));

        Vector newVelocity = new Vector(
                (int)(angleVelocity * Math.cos(angle)),
                (int)(angleVelocity * Math.sin(angle))
        );

        velocityChangeable.setVelocity(newVelocity);
    }
}
