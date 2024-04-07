package ru.edu.otus.architecture.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
public class Game {
    private final String id;
    private final List<String> players;
}
