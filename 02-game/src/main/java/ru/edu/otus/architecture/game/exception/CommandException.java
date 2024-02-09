package ru.edu.otus.architecture.game.exception;

public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}
