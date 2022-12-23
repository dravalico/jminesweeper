package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Board {
    private final Map<Point, TileValue> gameBoard;
    private final GameConfiguration gameConfiguration;

    public Board(GameConfiguration gameConfiguration) {
        gameBoard = new LinkedHashMap<>();
        this.gameConfiguration = gameConfiguration;
        fillBoard(gameConfiguration.dimension());
    }

    public Map<Point, TileValue> getGameBoard() {
        return gameBoard;
    }

    public void actionAt(Point point) {
        gameBoard.replace(point, new TileValue(GameSymbol.EMPTY));
    }

    private void fillBoard(Dimension boardDimensions) {
        for (int i = 0; i < boardDimensions.getHeight(); i++) {
            for (int j = 0; j < boardDimensions.getWidth(); j++) {
                gameBoard.put(new Point(i, j), new TileValue(GameSymbol.COVERED));
            }
        }
    }

}
