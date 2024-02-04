package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.model.FuelConsumable;
import ru.edu.otus.architecture.game.model.Movable;
import ru.edu.otus.architecture.game.model.impl.Vector;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommandMacroTest {
    @Mock
    private Movable movable;
    @Mock
    private FuelConsumable fuelConsumable;

    private Command macroCommand;

    @BeforeEach
    void initTest() {
        macroCommand = new CommandMacro(List.of(
                new CommandCheckFuel(fuelConsumable), new CommandMove(movable), new CommandBurnFuel(fuelConsumable)));

        when(movable.getPosition()).thenReturn(new Vector(0, 0));
        when(movable.getVelocity()).thenReturn(new Vector(1, 0));
        doNothing().when(movable).setPosition(any());

        when(fuelConsumable.getConsumption()).thenReturn(1);
        doNothing().when(fuelConsumable).setRemaining(anyInt());
    }

    @Test
    @DisplayName("Enough fuel")
    public void testExecuteWhenFuelEnoughThenRunThreeCommand() {
        when(fuelConsumable.getRemaining()).thenReturn(10);
        macroCommand.execute();
    }

    @Test
    @DisplayName("Not enough fuel")
    public void testExecuteWhenFuelNotEnoughtThenException() {
        when(fuelConsumable.getRemaining()).thenReturn(0);

        assertThrows(IllegalStateException.class, macroCommand::execute);
    }
}