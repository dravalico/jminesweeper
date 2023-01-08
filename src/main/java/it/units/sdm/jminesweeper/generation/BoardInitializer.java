package it.units.sdm.jminesweeper.generation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.Tile;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BoardInitializer {
    private final GameConfiguration gameConfiguration;

    public BoardInitializer(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public Map<Point, Tile> init() {
        Map<Point, Tile> board = new LinkedHashMap<>();
        fillBoard(board);
        return board;
    }

    private void fillBoard(Map<Point, Tile> board) {
        for (int i = 0; i < gameConfiguration.dimension().width; i++) {
            for (int j = 0; j < gameConfiguration.dimension().height; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
    }

}
