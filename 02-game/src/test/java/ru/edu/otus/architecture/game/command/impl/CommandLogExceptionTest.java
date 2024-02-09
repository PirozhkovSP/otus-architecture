package ru.edu.otus.architecture.game.command.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class CommandLogExceptionTest {

    private Appender<ILoggingEvent> mockedAppender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> loggingEventCaptor;

    @BeforeEach
    public void setup() {
        mockedAppender = mock(Appender.class);
        Logger root = (Logger) LoggerFactory.getLogger(CommandLogException.class);
        root.addAppender(mockedAppender);
        root.setLevel(Level.ERROR);
    }

    @Test
    void logCommandTest() {
        doNothing().when(mockedAppender).doAppend(loggingEventCaptor.capture());
        new CommandLogException(new RuntimeException("Test")).execute();

        ILoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
        // check the logged message
        assertEquals("Exception executing command", loggingEvent.getMessage());
        // check the log level
        assertEquals(Level.ERROR, loggingEvent.getLevel());
    }
}