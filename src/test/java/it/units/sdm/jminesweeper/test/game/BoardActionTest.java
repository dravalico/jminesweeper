package it.units.sdm.jminesweeper.test.game;

import it.units.sdm.jminesweeper.Board;
import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.TileValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BoardActionTest {
    private Map<Point, TileValue> expectedBoard;
    private int minesNumber;

    @BeforeEach
    void init() {
        expectedBoard = new LinkedHashMap<>();
        minesNumber = 0;
    }

    @ParameterizedTest
    @MethodSource
    void givenPointUncoverTile(Point pointToUncover) {
        int width = 30;
        int height = 16;
        Dimension boardDimension = new Dimension(width, height);
        Board board = new Board(new GameConfiguration(boardDimension, minesNumber));
        board.actionAt(pointToUncover);
        assertNotEquals(GameSymbol.COVERED, board.getMapBoard().get(pointToUncover).getValue());
    }

    private void generateExpectedMap(Point point, Dimension boardDimension) {
        for (int i = 0; i < boardDimension.width; i++) {
            for (int j = 0; j < boardDimension.height; j++) {
                if ((i == point.x) && (j == point.y)) {
                    expectedBoard.put(new Point(i, j), new TileValue(GameSymbol.EMPTY));
                    continue;
                }
                expectedBoard.put(new Point(i, j), new TileValue(GameSymbol.COVERED));
            }
        }
    }

    private static Stream<Point> givenPointUncoverTile() {
        int width = 30;
        int height = 16;
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, width)
                .forEach(i -> IntStream.range(0, height)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

}
