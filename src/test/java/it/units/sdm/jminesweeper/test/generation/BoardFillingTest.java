package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.Board;
import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.TileValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardFillingTest {
    private Map<Point, GameSymbol> expectedBoard;
    private int minesNumber;

    @BeforeEach
    void init() {
        expectedBoard = new LinkedHashMap<>();
        minesNumber = 0;
    }

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16"})
    void givenBoardSizeGenerateBoardWithCoveredSymbol(int width, int height) {
        Dimension boardDimension = new Dimension(width, height);
        Board board = new Board(new GameConfiguration(boardDimension, minesNumber));
        for (int i = 0; i < boardDimension.width; i++) {
            for (int j = 0; j < boardDimension.height; j++) {
                expectedBoard.put(new Point(i, j), GameSymbol.COVERED);
            }
        }
        assertEquals(expectedBoard, board.getMapBoard());
    }

}
