package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.exception.CommandException;
import ru.edu.otus.architecture.game.model.FuelConsumable;

@ExtendWith(MockitoExtension.class)
class CommandCheckFuelTest {
    @Mock
    private FuelConsumable fuelExpending;

    private Command command;

    @BeforeEach
    void initTest() {
        command = new CommandCheckFuel(fuelExpending);
    }

    @Test
    @DisplayName("Not enough fuel")
    public void testExecuteWhenFuelNotEnoughThenException() {
        when(fuelExpending.getRemaining()).thenReturn(1);
        when(fuelExpending.getConsumption()).thenReturn(2);

        assertThrows(CommandException.class, () -> command.execute());
    }

    @Test
    @DisplayName("Enough fuel")
    public void testExecuteWhenFuelEnoughThenNoExceptionThrown() {
        when(fuelExpending.getRemaining()).thenReturn(10);
        when(fuelExpending.getConsumption()).thenReturn(2);

        assertDoesNotThrow(command::execute);
    }
}