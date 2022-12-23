package it.units.sdm.jminesweeper;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class MinesPlacerUtil {

    private MinesPlacerUtil() {
    }

    public static void placeMine(Map<Point, TileValue> board, Point firstClickPosition) {
        Dimension dimension = BoardUtil.computeBoardDimension(board);
        Random random = new Random();
        Point minePosition = new Point(random.nextInt(dimension.width), random.nextInt(dimension.height));
        board.replace(minePosition, new TileValue(GameSymbol.MINE));
    }

}
