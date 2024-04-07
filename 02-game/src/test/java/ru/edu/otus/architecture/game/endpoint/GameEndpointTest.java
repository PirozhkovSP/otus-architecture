package ru.edu.otus.architecture.game.endpoint;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.edu.otus.architecture.game.command.Command;
import ru.edu.otus.architecture.game.core.IoC;
import ru.edu.otus.architecture.game.core.command.CommandInit;
import ru.edu.otus.architecture.game.model.Movable;
import ru.edu.otus.architecture.game.model.impl.AgentMessage;
import ru.edu.otus.architecture.game.model.impl.AgentResponse;
import ru.edu.otus.architecture.game.model.impl.Vector;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestConfig.class)
class GameEndpointTest {
    @LocalServerPort
    private int port;
    private WebSocketStompClient webSocketStompClient;
    private final BlockingQueue<Command> commandsQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<AgentResponse> answerQueue = new ArrayBlockingQueue<>(1);

    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        new CommandInit().execute();
        Function<Object[], Object> func = (args) -> "testGame";
        ((Command) IoC.resolve("IoC.Register", "IoC.Scope.Id", func)).execute();
        Function<Object[], Object> findGameFunc = args -> {
            String gameId = IoC.resolve("IoC.Scope.Id");
            if (!Objects.equals(gameId, args[0])) {
                return IoC.resolve("IoC.Scope.Current");
            } else {
                return null;
            }
        };
        ((Command) IoC.resolve("IoC.Register", "IoC.Scope.Get", findGameFunc)).execute();

        Movable gameObject = mock(Movable.class);
        Function<Object[], Object> getGameObjectFunc = args -> gameObject;
        ((Command) IoC.resolve("IoC.Register", "testObjectId", getGameObjectFunc)).execute();

        Command c = mock(Command.class);
        Function<Object[], Object> setVelocityFunc = args -> c;
        ((Command) IoC.resolve("IoC.Register", "testOperationId", setVelocityFunc)).execute();

        Function<Object[], Object> addCommandFunc = args -> commandsQueue.add((Command) args[0]);
        ((Command) IoC.resolve("IoC.Register", "CommandsQueue.add", addCommandFunc)).execute();
    }

    @Test
    void sendCommandTest() throws ExecutionException, InterruptedException, TimeoutException {
        Object[] args = {new Vector(0, 0)};
        AgentMessage agentMessage = new AgentMessage("testGame", "testObjectId", "testOperationId",
                args);

        StompSession session = webSocketStompClient.connectAsync(String.format("ws://localhost:%d/ws", port),
                        new StompSessionHandlerAdapter() {
                        })
                .get(1, SECONDS);


        session.subscribe("/state", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return AgentResponse.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                answerQueue.add((AgentResponse) payload);
            }
        });

        StompHeaders headers = new StompHeaders();
        headers.add(StompHeaders.DESTINATION, "/game/action");
        headers.add(AUTHORIZATION,
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                        ".eyJpc3MiOiJBdXRoIG1vZHVsZSIsInN1YiI6IlBsYXllciBkZXRhaWxzIiwidXNlcklkIjoidGVzdFVzZXIiLCJnYW1lSWQiOiJ0ZXN0R2FtZSIsInJvbGVzIjpbIlBMQVlFUiJdLCJpYXQiOjE3MTE4MTE1NTAsImp0aSI6IjYxY2M3YjhmLTFhNDUtNGFiNi1hMGIxLTJmNGYzYTkwZjQ4NCIsIm5iZiI6MTcxMTgxMTU1MX0" +
                        ".JW9joMDxWAFGZ4O5VQJLl3gSXUbC1rOVb_DV0lG7iHQ");
        session.send(headers, agentMessage);

        await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> assertFalse(answerQueue.isEmpty()));
    }
}