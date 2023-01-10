package it.units.sdm.jminesweeper.core.generation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.enumeration.GameSymbol;
import it.units.sdm.jminesweeper.core.Tile;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BoardInitializer {
    private final GameConfiguration gameConfiguration;
    private final MinesPlacer<Map<Point, Tile>, Point> minesPlacer;

    public BoardInitializer(GameConfiguration gameConfiguration, MinesPlacer<Map<Point, Tile>, Point> minesPlacer) {
        this.gameConfiguration = gameConfiguration;
        this.minesPlacer = minesPlacer;
    }

    public void fillBoard(Map<Point, Tile> board) {
        for (int i = 0; i < gameConfiguration.dimension().height; i++) {
            for (int j = 0; j < gameConfiguration.dimension().width; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
    }

    public void init(Map<Point, Tile> board, Point firstClickPosition) {
        minesPlacer.place(board, gameConfiguration.minesNumber(), firstClickPosition);
        computeNumberForCells(board);
    }

    private void computeNumberForCells(Map<Point, Tile> board) {
        board.forEach((k, v) -> {
            if (!v.isAMine()) {
                v.setValue(GameSymbol.fromInt((int) board.entrySet()
                        .stream()
                        .filter(e -> e.getKey().distance(k) <= Math.sqrt(2) && e.getValue().isAMine())
                        .count()));
            }
        });
    }

}
