package ru.edu.otus.architecture.service.impl;

import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.model.Game;
import ru.edu.otus.architecture.repository.GameRepository;
import ru.edu.otus.architecture.service.AuthorizationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GamesManager implements AuthorizationService {
    private final GameRepository gameRepository;
    private static final List<String> ROLES_PLAYER = List.of("PLAYER");
    @Override
    public List<String> getRoles(String userId, String gameId) {
        Game game = gameRepository.findById(gameId);
        return game.getPlayers().contains(userId) ? new ArrayList<>(ROLES_PLAYER) : List.of();
    }

    public String createGame(List<String> players) {
        Game game = Game.builder()
                .id(UUID.randomUUID().toString())
                .players(players)
                .build();
        return gameRepository.save(game).getId();
    }
}
