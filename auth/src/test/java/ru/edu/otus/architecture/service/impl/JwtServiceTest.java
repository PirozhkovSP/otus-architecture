package ru.edu.otus.architecture.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import ru.edu.otus.architecture.model.Game;
import ru.edu.otus.architecture.repository.GameRepository;
import ru.edu.otus.architecture.service.AuthorizationService;

import java.util.List;
import java.util.UUID;

class JwtServiceTest {
    private static GameRepository gameRepository;
    private static JwtService jwtService;

    @BeforeAll
    public static void init() {
        gameRepository = mock(GameRepository.class);
        AuthorizationService authorizationService = new GamesManager(gameRepository);
        jwtService = new JwtService(authorizationService);
    }

    @Test
    void createTokenTest() {
        when(gameRepository.findById("testGame"))
                .thenReturn(Game.builder()
                        .id(UUID.randomUUID().toString())
                        .players(List.of("testUser"))
                        .build());
        assertTrue(StringUtils.isNotBlank(jwtService.create("testUser", "testGame")));
    }
}