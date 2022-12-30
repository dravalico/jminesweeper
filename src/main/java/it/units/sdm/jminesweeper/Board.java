package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Board {
    private final Map<Point, TileValue> mapBoard;
    private final GameConfiguration gameConfiguration;

    public Board(GameConfiguration gameConfiguration) {
        mapBoard = new LinkedHashMap<>();
        this.gameConfiguration = gameConfiguration;
        BoardUtil.fillBoard(mapBoard, gameConfiguration.dimension());
    }

    public Map<Point, TileValue> getMapBoard() {
        return mapBoard;
    }

    public void actionAt(Point point) {
        mapBoard.replace(point, new TileValue(GameSymbol.EMPTY));
    }

}
