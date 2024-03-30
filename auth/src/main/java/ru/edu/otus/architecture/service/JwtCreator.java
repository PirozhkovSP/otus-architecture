package ru.edu.otus.architecture.service;

public interface JwtCreator {
    String create(String userId, String gameId);
}
