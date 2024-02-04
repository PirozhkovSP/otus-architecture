package ru.edu.otus.architecture.game.exception.handler;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.game.command.Command;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ExceptionHandlerChoose implements ExceptionHandler {
    private static final Map<Class<? extends Command>, Map<Class<? extends Exception>, ExceptionHandler>> HANDLERS =
            new HashMap<>();
    @Override
    public Command handle(Exception exception, Command command) {
        return HANDLERS.getOrDefault(command.getClass(), new HashMap<>())
                .getOrDefault(exception.getClass(), (exception1, command1) -> () -> {})
                .handle(exception, command);
    }

    public static void registerHandler(Class<? extends Command> commandClass,
                                        Class<? extends Exception> exceptionClass,
                                        ExceptionHandler exceptionHandler) {
        HANDLERS.computeIfAbsent(commandClass, (x) -> new HashMap<>()).put(exceptionClass, exceptionHandler);
    }
}
