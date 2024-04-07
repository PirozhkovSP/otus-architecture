package ru.edu.otus.architecture.game.endpoint;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandInterpret;
import ru.edu.otus.architecture.game.endpoint.exception.AuthorizationException;
import ru.edu.otus.architecture.game.model.impl.AgentMessage;
import ru.edu.otus.architecture.game.model.impl.AgentResponse;
import ru.edu.otus.architecture.service.Authorizer;

@Controller
@RequiredArgsConstructor
public class GameEndpoint {
    private final Authorizer authorizationService;

    @MessageMapping("/action")
    @SendTo("/state")
    public AgentResponse onMessage(@Header(AUTHORIZATION) String auth, @Payload AgentMessage message) {
        if (authorizationService.authorize(auth, message.gameId(), "PLAYER")) {
            Command interpretCommand = new CommandInterpret(message.gameId(), message.objectId(), message.operationId(),
                    message.args());
            interpretCommand.execute();
            return new AgentResponse("Response with game state");
        } else {
            throw new AuthorizationException();
        }
    }

}
