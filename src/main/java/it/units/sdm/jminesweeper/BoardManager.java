package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardManager {
    private final Map<Point, Tile> mapBoard;
    private final GameConfiguration gameConfiguration;
    private int uncoveredTiles;

    public BoardManager(GameConfiguration gameConfiguration) {
        mapBoard = new LinkedHashMap<>();
        this.gameConfiguration = gameConfiguration;
        uncoveredTiles = 0;
        BoardBuilder.fillBoard(mapBoard, this.gameConfiguration.dimension());
    }

    public Map<Point, GameSymbol> getMapBoard() {
        return mapBoard.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().isCovered() ? GameSymbol.COVERED : e.getValue().getValue()));
    }

    public ActionOutcome actionAt(Point point) {
        verifyPointWithinBoardDimension(point);
        if (mapBoard.get(point).isAMine()) {
            return ActionOutcome.DEFEAT;
        }
        if (uncoveredTiles == 0) {
            init(point);
        }
        uncoverFreeSpotRecursively(point);
        return ActionOutcome.PROGRESS;
    }

    private void verifyPointWithinBoardDimension(Point point) {
        Dimension boardDimension = gameConfiguration.dimension();
        if (((point.x < 0) || (point.x >= boardDimension.width)) || ((point.y < 0) || (point.y >= boardDimension.height))) {
            throw new IllegalArgumentException("Coordinates not allowed!");
        }
    }

    private void init(Point point) {
        MinesPlacer.place(mapBoard, gameConfiguration.minesNumber(), point);
        BoardBuilder.computeNumberForCells(mapBoard);
    }

    private void uncoverFreeSpotRecursively(Point point) {
        uncoverTile(point);
        Dimension dimension = gameConfiguration.dimension();
        int iStart = (point.x == 0 ? 0 : -1);
        int iStop = (point.x == dimension.width - 1 ? 0 : 1);
        int jStart = (point.y == 0 ? 0 : -1);
        int jStop = (point.y == dimension.height - 1 ? 0 : 1);
        for (int i = iStart; i <= iStop; i++) {
            for (int j = jStart; j <= jStop; j++) {
                Point temp = new Point(point.x + i, point.y + j);
                if (mapBoard.get(temp).isCovered()) {
                    if (mapBoard.get(temp).isANumber()) {
                        uncoverTile(temp);
                    } else {
                        uncoverFreeSpotRecursively(temp);
                    }
                }
            }
        }
    }

    private void uncoverTile(Point point) {
        uncoveredTiles = uncoveredTiles + 1;
        mapBoard.get(point).uncover();
    }

}
