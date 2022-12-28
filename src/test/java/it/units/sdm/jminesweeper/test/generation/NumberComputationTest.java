package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.BoardUtil;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.TileValue;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberComputationTest {

    @Test
    void placeAOneIn2x1BoardWithOneMine() {
        Map<Point, TileValue> expectedMapBoard = Map.of(new Point(0, 0), new TileValue(GameSymbol.ONE),
                new Point(0, 1), new TileValue(GameSymbol.MINE));
        Map<Point, TileValue> actualMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new TileValue(GameSymbol.EMPTY),
                new Point(0, 1), new TileValue(GameSymbol.MINE)));
        BoardUtil.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }
    
}
