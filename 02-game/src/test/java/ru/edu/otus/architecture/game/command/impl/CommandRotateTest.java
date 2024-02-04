package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edu.otus.architecture.game.model.Rotatable;

@ExtendWith(MockitoExtension.class)
class CommandRotateTest {
    @Mock
    private Rotatable rotatable;

    @Captor
    private ArgumentCaptor<Integer> interceptNewDirection;

    private CommandRotate rotateCommand;
    private int startDirection;
    private int angularVelocity;
    private int directionNumbers;

    @BeforeEach
    public void setUpTest() {
        rotateCommand = new CommandRotate(rotatable);

        startDirection = 0;
        angularVelocity = (int) (360 * 1.5);
        directionNumbers = 360;
    }

    @Test
    @DisplayName("Для положения в 0 градусов, полуторный поворот вокруг оси приводит к положению в 180 градусов. " +
            "Иметь возможность изменять положение с точностью до градуса.")
    public void executeWhenGivenParametersThenReturnNewDirection() {
        int expectedEndDirection = 180;

        when(rotatable.getDirection()).thenReturn(startDirection);
        when(rotatable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotatable.getDirectionNumbers()).thenReturn(directionNumbers);
        doNothing().when(rotatable).setDirection(interceptNewDirection.capture());

        rotateCommand.execute();

        int actualEndDirection = interceptNewDirection.getValue();

        assertEquals(expectedEndDirection, actualEndDirection);
    }

    @Test
    @DisplayName("При невозможности получения положения выдача исключения")
    public void executeWhenExceptionInGetDirectinThenException() {
        when(rotatable.getDirection()).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> rotateCommand.execute());
    }

    @Test
    @DisplayName("При невозможности получения угловой скорости выдача исключения")
    public void executeWhenExceptionInAngularVelocityThenException() {
        when(rotatable.getDirection()).thenReturn(startDirection);
        when(rotatable.getAngularVelocity()).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> rotateCommand.execute());
    }

    @Test
    @DisplayName("При невозможности получения точности поворота выдача исключения")
    public void executeWhenExceptionInDirectionNumbersThenException() {
        when(rotatable.getDirection()).thenReturn(startDirection);
        when(rotatable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotatable.getDirectionNumbers()).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> rotateCommand.execute());
    }

    @Test
    @DisplayName("При невозможности задания положения выдача исключения")
    public void executeWhenExceptionInSetDirectionThenException() {
        when(rotatable.getDirection()).thenReturn(startDirection);
        when(rotatable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotatable.getDirectionNumbers()).thenReturn(directionNumbers);
        doThrow(IllegalArgumentException.class).when(rotatable).setDirection(interceptNewDirection.capture());

        assertThrows(IllegalArgumentException.class, () -> rotateCommand.execute());
    }
}