package it.units.sdm.jminesweeper.test.game;

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

public class BoardActionTest {
    private Map<Point, TileValue> expectedBoard;
    private int minesNumber;

    @BeforeEach
    void init() {
        expectedBoard = new LinkedHashMap<>();
        minesNumber = 0;
    }

    @ParameterizedTest
    @CsvSource({"0,0", "8,8", "4,4", "2,2", "1,1", "3,4", "7,5", "8,0", "6,1"})
    void givenCoordinatesUncoverTileInBeginnerMode(int x, int y) {
        int width = 9;
        int height = 9;
        Dimension boardDimension = new Dimension(width, height);
        Board board = new Board(new GameConfiguration(boardDimension, minesNumber));
        generateExpectedMap(x, y, boardDimension);
        board.actionAt(new Point(x, y));
        assertEquals(expectedBoard, board.getGameBoard());
    }

    @ParameterizedTest
    @CsvSource({"0,0", "15,15", "7,7", "12,4", "4,14", "9, 10", "2,8", "1,9", "10,10"})
    void givenCoordinatesUncoverTileInIntermediateMode(int x, int y) {
        int width = 16;
        int height = 16;
        Dimension boardDimension = new Dimension(width, height);
        Board board = new Board(new GameConfiguration(boardDimension, minesNumber));
        generateExpectedMap(x, y, boardDimension);
        board.actionAt(new Point(x, y));
        assertEquals(expectedBoard, board.getGameBoard());
    }

    @ParameterizedTest
    @CsvSource({"0,0", "29,15", "14,7", "22,14", "19, 3", "6, 24", "8,2", "9,1", "23,5"})
    void givenCoordinatesUncoverTileInExpertMode(int x, int y) {
        int width = 30;
        int height = 16;
        Dimension boardDimension = new Dimension(width, height);
        Board board = new Board(new GameConfiguration(boardDimension, minesNumber));
        generateExpectedMap(x, y, boardDimension);
        board.actionAt(new Point(x, y));
        assertEquals(expectedBoard, board.getGameBoard());
    }

    private void generateExpectedMap(int x, int y, Dimension boardDimension) {
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                if ((i == x) && (j == y)) {
                    expectedBoard.put(new Point(i, j), new TileValue(GameSymbol.EMPTY));
                    continue;
                }
                expectedBoard.put(new Point(i, j), new TileValue(GameSymbol.COVERED));
            }
        }
    }

}
