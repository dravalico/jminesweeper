package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.enumeration.GameSymbol;
import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.BoardInitializer;
import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardFillingTest {
    private Map<Point, Tile> expectedBoard;
    private static final int MINES_NUMBER = 0;

    @BeforeEach
    void init() {
        expectedBoard = new LinkedHashMap<>();
    }

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16", "32,60"})
    void givenBoardSizeGenerateBoardWithEmptyTileAndCorrectDimension(int width, int height) {
        Dimension boardDimension = new Dimension(width, height);
        BoardInitializer boardInitializer = new BoardInitializer(new GameConfiguration(boardDimension, MINES_NUMBER),
                new GuassianMinesPlacer());
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                expectedBoard.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
        Map<Point, Tile> actualBoard = new LinkedHashMap<>();
        boardInitializer.fillBoard(actualBoard);
        assertEquals(expectedBoard, actualBoard);
    }

}
