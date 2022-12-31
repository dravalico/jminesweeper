package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {
    private final Map<Point, TileValue> mapBoard;
    private final GameConfiguration gameConfiguration;

    public Board(GameConfiguration gameConfiguration) {
        mapBoard = new LinkedHashMap<>();
        this.gameConfiguration = gameConfiguration;
        BoardUtil.fillBoard(mapBoard, this.gameConfiguration.dimension());
    }

    public Map<Point, GameSymbol> getMapBoard() {
        return mapBoard.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().isCovered() ? GameSymbol.COVERED : e.getValue().getValue()));
    }

    public void actionAt(Point point) {
        uncoverFreeSpotRecursively(point);
    }

    private void uncoverFreeSpotRecursively(Point point) {
        mapBoard.get(point).uncover();
        Dimension dimension = BoardUtil.computeBoardDimension(mapBoard);
        int iStart = (point.x == 0 ? 0 : -1);
        int iStop = (point.x == dimension.width - 1 ? 0 : 1);
        int jStart = (point.y == 0 ? 0 : -1);
        int jStop = (point.y == dimension.height - 1 ? 0 : 1);
        for (int i = iStart; i <= iStop; i++) {
            for (int j = jStart; j <= jStop; j++) {
                Point temp = new Point(point.x + i, point.y + j);
                if (mapBoard.get(temp).isCovered()) {
                    uncoverFreeSpotRecursively(temp);
                }
            }
        }
    }

}
