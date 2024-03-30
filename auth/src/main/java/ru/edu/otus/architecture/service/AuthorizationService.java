package ru.edu.otus.architecture.service;

import java.util.List;

public interface AuthorizationService {
    List<String> getRoles(String userId, String gameId);
}
