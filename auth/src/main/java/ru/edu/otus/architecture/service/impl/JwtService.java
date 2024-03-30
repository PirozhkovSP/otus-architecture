package ru.edu.otus.architecture.service.impl;

import static java.util.Optional.ofNullable;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import ru.edu.otus.architecture.service.AuthorizationService;
import ru.edu.otus.architecture.service.Authorizer;
import ru.edu.otus.architecture.service.JwtCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtService implements JwtCreator, Authorizer {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("arch");
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();
    private final AuthorizationService authorizationService;

    public String create(String userId, String gameId){
        return JWT.create()
                .withIssuer("Auth module")
                .withSubject("Player details")
                .withClaim("userId", userId)
                .withClaim("gameId", gameId)
                .withClaim("roles", authorizationService.getRoles(userId, gameId))
                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 5000L))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                .sign(ALGORITHM);
    }

    public boolean authorize(String token, String gameId, String neededRole) {
        DecodedJWT decodedJWT = VERIFIER.verify(token);
        Claim claimRoles = decodedJWT.getClaim("roles");
        List<String> roles = claimRoles.asList(String.class);
        Claim claimGameId = decodedJWT.getClaim("gameId");
        String authGameId = claimGameId.asString();
        return ofNullable(roles).orElse(new ArrayList<>()).contains(neededRole) && Objects.equals(authGameId, gameId);
    }
}
