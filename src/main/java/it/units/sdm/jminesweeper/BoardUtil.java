package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class BoardUtil {

    private BoardUtil() {
    }

    public static Dimension computeBoardDimension(Map<Point, TileValue> mapBoard) {
        Optional<Point> furthestPoint = mapBoard.keySet().stream()
                .max(Comparator.comparingDouble(p -> p.distance(new Point(0, 0))));
        return new Dimension(furthestPoint.get().x + 1, furthestPoint.get().y + 1);
    }

    public static void computeNumberForCells(Map<Point, TileValue> mapBoard) {
        mapBoard.forEach((k, v) -> {
            if (!v.isAMine()) {
                v.setValue(GameSymbol.fromInt((int) mapBoard.entrySet().stream()
                        .filter(e ->
                                e.getKey().distance(k) <= Math.sqrt(2) && e.getValue().isAMine())
                        .count()));
            }
        });
    }

}
