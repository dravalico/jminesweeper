package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class BoardUtil {

    private BoardUtil() {
    }

    public static void fillBoard(Map<Point, Tile> mapBoard, Dimension boardDimension) {
        for (int i = 0; i < boardDimension.width; i++) {
            for (int j = 0; j < boardDimension.height; j++) {
                mapBoard.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
    }

    public static Dimension computeBoardDimension(Map<Point, Tile> mapBoard) {
        Optional<Point> furthestPoint = mapBoard.keySet()
                .stream()
                .max(Comparator.comparingDouble(p -> p.distance(new Point(0, 0))));
        return new Dimension(furthestPoint.get().x + 1, furthestPoint.get().y + 1);
    }

    public static void computeNumberForCells(Map<Point, Tile> mapBoard) {
        mapBoard.forEach((k, v) -> {
            if (!v.isAMine()) {
                v.setValue(GameSymbol.fromInt((int) mapBoard.entrySet()
                        .stream()
                        .filter(e ->
                                e.getKey().distance(k) <= Math.sqrt(2) && e.getValue().isAMine())
                        .count()));
            }
        });
    }

}
