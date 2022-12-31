package it.units.sdm.jminesweeper.test.util;

import it.units.sdm.jminesweeper.BoardUtil;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.Tile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardUtilityTest {

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16", "23,11", "50, 50", "4,29"})
    void givenABoardReturnsItsDimensions(int width, int height) {
        Map<Point, Tile> board = new LinkedHashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.COVERED));
            }
        }
        assertEquals(new Dimension(width, height), BoardUtil.computeBoardDimension(board));
    }

}
