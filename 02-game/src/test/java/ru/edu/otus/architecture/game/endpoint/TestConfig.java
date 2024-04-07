package ru.edu.otus.architecture.game.endpoint;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.edu.otus.architecture.repository.GameRepository;
import ru.edu.otus.architecture.service.AuthorizationService;
import ru.edu.otus.architecture.service.Authorizer;
import ru.edu.otus.architecture.service.impl.GamesManager;
import ru.edu.otus.architecture.service.impl.JwtService;

@TestConfiguration
public class TestConfig {
    @Bean
    public Authorizer register() {
        GameRepository gameRepository = mock(GameRepository.class);
        AuthorizationService authorizationService = new GamesManager(gameRepository);
        return new JwtService(authorizationService);
    }
}
