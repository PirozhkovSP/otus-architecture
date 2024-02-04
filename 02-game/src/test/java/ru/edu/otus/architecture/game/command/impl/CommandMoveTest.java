package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edu.otus.architecture.game.model.Movable;
import ru.edu.otus.architecture.game.model.impl.Vector;

@ExtendWith(MockitoExtension.class)
class CommandMoveTest {
    @Mock
    private Movable movable;

    @Captor
    private ArgumentCaptor<Vector> interceptNewPosition;

    private CommandMove moveCommand;
    private Vector startPosition;
    private Vector startVelocity;

    @BeforeEach
    public void initTest() {
        moveCommand = new CommandMove(movable);
        startPosition = new Vector(12, 5);
        startVelocity = new Vector(-7, 3);
    }

    @Test
    @DisplayName("Для объекта, находящегося в точке (12, 5) и движущегося со скоростью (-7, 3) движение меняет положение объекта на (5, 8)")
    public void executeWhenGivenPositionAndVelocityThenNewPosition() {
        when(movable.getPosition()).thenReturn(startPosition);
        when(movable.getVelocity()).thenReturn(startVelocity);
        doNothing().when(movable).setPosition(interceptNewPosition.capture());

        moveCommand.execute();

        Vector newPosition = interceptNewPosition.getValue();

        assertEquals(new Vector(5, 8), newPosition);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке")
    public void executeWhenExceptionGetPositionThenException() {
        when(movable.getPosition()).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> moveCommand.execute());
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке")
    public void executeWhenExceptionMoveThenException() {
        when(movable.getPosition()).thenReturn(startPosition);
        when(movable.getVelocity()).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> moveCommand.execute());
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке")
    public void executeWhenExceptionSetPositionThenException() {
        when(movable.getPosition()).thenReturn(startPosition);
        when(movable.getVelocity()).thenReturn(startVelocity);
        doThrow(IllegalArgumentException.class).when(movable).setPosition(any());

        assertThrows(IllegalArgumentException.class, () -> moveCommand.execute());
    }
}