package it.units.sdm.jminesweeper;

import java.awt.*;

public record GameConfiguration(Dimension dimension, int minesNumber) {
    public enum Difficulty {
        BEGINNER,
        INTERMEDIATE,
        EXPERT
    }

    public static GameConfiguration fromDifficulty(Difficulty gameDifficulty) {
        return switch (gameDifficulty) {
            case BEGINNER -> new GameConfiguration(new Dimension(9, 9), 10);
            case INTERMEDIATE -> new GameConfiguration(new Dimension(16, 16), 40);
            case EXPERT -> new GameConfiguration(new Dimension(30, 16), 99);
        };
    }

}
