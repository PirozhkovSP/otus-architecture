package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.model.FuelConsumable;

@ExtendWith(MockitoExtension.class)
class CommandBurnFuelTest {
    @Mock
    private FuelConsumable fuelConsumable;

    @Captor
    private ArgumentCaptor<Integer> interceptValueFuel;

    private Command command;

    @BeforeEach
    void initTest() {
        command = new CommandBurnFuel(fuelConsumable);
    }

    @Test
    @DisplayName("Fuel reduction")
    public void testExecuteVerifyExpendFuel() {
        when(fuelConsumable.getRemaining()).thenReturn(10);
        when(fuelConsumable.getConsumption()).thenReturn(2);
        doNothing().when(fuelConsumable).setRemaining(interceptValueFuel.capture());

        command.execute();

        assertEquals(8, interceptValueFuel.getValue());
    }
}