package ru.edu.otus.architecture.game.model.impl;


public record AgentMessage(String gameId, String objectId, String operationId, Object[] args) {
}
