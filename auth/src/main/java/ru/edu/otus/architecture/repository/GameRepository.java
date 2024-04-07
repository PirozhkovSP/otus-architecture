package ru.edu.otus.architecture.repository;

import ru.edu.otus.architecture.model.Game;

public interface GameRepository {
    Game save(Game game);

    Game findById(String gameId);
}
