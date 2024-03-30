package ru.edu.otus.architecture.service;

public interface Authorizer {
    boolean authorize(String token, String gameId, String neededRole);
}
