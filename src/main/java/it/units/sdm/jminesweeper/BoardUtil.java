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
        Dimension dimension = BoardUtil.computeBoardDimension(mapBoard);
        for (Map.Entry<Point, TileValue> entry : mapBoard.entrySet()) {
            if (entry.getValue().getValue().equals(GameSymbol.MINE)) {
                continue;
            }
            int iStart = (entry.getKey().x == 0 ? 0 : -1);
            int iStop = (entry.getKey().x == dimension.width - 1 ? 0 : 1);
            int jStart = (entry.getKey().y == 0 ? 0 : -1);
            int jStop = (entry.getKey().y == dimension.height - 1 ? 0 : 1);
            int minesCounter = 0;
            for (int i = iStart; i <= iStop; i++) {
                for (int j = jStart; j <= jStop; j++) {
                    if (mapBoard.get(new Point(entry.getKey().x + i, entry.getKey().y + j)).isAMine()) {
                        minesCounter = minesCounter + 1;
                    }
                }
            }
            if (minesCounter != 0) {
                mapBoard.replace(entry.getKey(), new TileValue(GameSymbol.fromInt(minesCounter)));
            }
        }
    }

}
