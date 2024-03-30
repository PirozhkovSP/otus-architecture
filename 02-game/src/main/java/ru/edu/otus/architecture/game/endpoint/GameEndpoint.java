package ru.edu.otus.architecture.game.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.command.impl.CommandInterpret;
import ru.edu.otus.architecture.game.model.impl.AgentMessage;
import ru.edu.otus.architecture.game.model.impl.AgentResponse;

@Controller
@RequiredArgsConstructor
public class GameEndpoint {

    @MessageMapping("/action")
    @SendTo("/state")
    public AgentResponse onMessage(@Payload AgentMessage message) {
        Command interpretCommand = new CommandInterpret(message.gameId(), message.objectId(), message.operationId(),
                message.args());
        interpretCommand.execute();
        return new AgentResponse("Response with game state");
    }

}
