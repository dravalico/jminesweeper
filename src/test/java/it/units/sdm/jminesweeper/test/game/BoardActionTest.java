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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardActionTest {
    private Board board;
    private Map<Point, TileValue> expectedBoard;
    private int minesNumber;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 16;

    @BeforeEach
    void init() {
        expectedBoard = new LinkedHashMap<>();
        minesNumber = 99;
        board = new Board(new GameConfiguration(new Dimension(WIDTH, HEIGHT), minesNumber));
    }

    @ParameterizedTest
    @MethodSource("generatePointsRepresentingActionAt")
    void givenPointUncoverTile(Point pointToUncover) {
        board.actionAt(pointToUncover);
        assertNotEquals(GameSymbol.COVERED, board.getMapBoard().get(pointToUncover));
    }

    @ParameterizedTest
    @MethodSource
    void onFirstClickNotOnEdgeUncoverAtLeastNineSpots(Point pointOfAction) {
        board.actionAt(pointOfAction);
        int uncoveredTiles = WIDTH * HEIGHT - Collections.frequency(board.getMapBoard().values(), GameSymbol.COVERED);
        assertTrue(uncoveredTiles >= 9);
    }

    @ParameterizedTest
    @MethodSource
    void onFirstClickOnEdgeUncoverAtLeastFourSpots(Point pointOfAction) {
        board.actionAt(pointOfAction);
        int uncoveredTiles = WIDTH * HEIGHT - Collections.frequency(board.getMapBoard().values(), GameSymbol.COVERED);
        assertTrue(uncoveredTiles >= 4);
    }

    @ParameterizedTest
    @MethodSource("generatePointsRepresentingActionAt")
    void onFirstClickUncoverAtLeastNineSpotsButMines(Point pointOfAction) {
        board.actionAt(pointOfAction);
        int coveredTiles = Collections.frequency(board.getMapBoard().values(), GameSymbol.COVERED);
        assertTrue(coveredTiles >= minesNumber);
    }

    private static Stream<Point> generatePointsRepresentingActionAt() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, WIDTH)
                .forEach(i -> IntStream.range(0, HEIGHT)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

    private static Stream<Point> onFirstClickNotOnEdgeUncoverAtLeastNineSpots() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(1, WIDTH - 1)
                .forEach(i -> IntStream.range(1, HEIGHT - 1)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

    private static Stream<Point> onFirstClickOnEdgeUncoverAtLeastFourSpots() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, WIDTH)
                .forEach(i -> points.add(new Point(i, 0))
                );
        IntStream.range(0, HEIGHT)
                .forEach(j -> points.add(new Point(0, j))
                );
        IntStream.range(0, WIDTH)
                .forEach(i -> points.add(new Point(i, HEIGHT - 1))
                );
        IntStream.range(0, HEIGHT)
                .forEach(j -> points.add(new Point(WIDTH - 1, j))
                );
        return points.stream();
    }

}
