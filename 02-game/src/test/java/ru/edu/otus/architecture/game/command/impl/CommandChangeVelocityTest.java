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
import ru.edu.otus.architecture.game.model.VelocityChangeable;
import ru.edu.otus.architecture.game.model.impl.Vector;

@ExtendWith(MockitoExtension.class)
class CommandChangeVelocityTest {
    @Mock
    private VelocityChangeable velocityChangeable;

    @Captor
    ArgumentCaptor<Vector> interceptNewVelocity;

    private Command command;

    @BeforeEach
    void setUoTest(){
        command = new CommandChangeVelocity(velocityChangeable);
    }

    @Test
    @DisplayName("Тестирование изменения скорости, при повороте на 90 градусов, " +
            "объект со скоростью (0, 100) меняет значения вертикальной/горизонтальной скорости")
    void testExecuteWhenAngleNinetyDegreesThenChangeValueVectorVelocity() {
        when(velocityChangeable.getVelocity()).thenReturn(new Vector(100, 0));
        when(velocityChangeable.getDirection()).thenReturn(1);
        when(velocityChangeable.getDirectionNumbers()).thenReturn(4);
        doNothing().when(velocityChangeable).setVelocity(interceptNewVelocity.capture());

        command.execute();

        Vector expectedVelocity = new Vector(0, 100);
        Vector actualVelocity = interceptNewVelocity.getValue();

        assertEquals(expectedVelocity, actualVelocity);
    }
}