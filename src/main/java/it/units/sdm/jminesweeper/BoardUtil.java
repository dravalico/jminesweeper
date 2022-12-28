package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class BoardUtil {

    private BoardUtil() {
    }

    public static Dimension computeBoardDimension(Map<Point, TileValue> board) {
        Optional<Point> furthestPoint = board.keySet().stream()
                .max(Comparator.comparingDouble(p -> p.distance(new Point(0, 0))));
        return new Dimension(furthestPoint.get().x + 1, furthestPoint.get().y + 1);
    }

    public static void computeNumberForCells(Map<Point, TileValue> board) {
        board.put(new Point(0, 0), new TileValue(GameSymbol.ONE));
    }
}
