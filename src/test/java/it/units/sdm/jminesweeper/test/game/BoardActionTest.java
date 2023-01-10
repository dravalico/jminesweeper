package it.units.sdm.jminesweeper.test.game;

import it.units.sdm.jminesweeper.BoardManager;
import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.core.GameManager;
import it.units.sdm.jminesweeper.enumeration.ActionOutcome;
import it.units.sdm.jminesweeper.enumeration.GameSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardActionTest {
    private BoardManager boardManager;
    private int minesNumber;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 16;

    @BeforeEach
    void init() {
        minesNumber = 99;
        boardManager = new BoardManager(new GameConfiguration(new Dimension(WIDTH, HEIGHT), minesNumber));
    }

    @ParameterizedTest
    @CsvSource({"0,-1", "-1,0", "0,0", "1, 1", "2, 3", "100, -100", "20,4", "-5,2", "-4,13"})
    void givenAPointOutOfTheBoardThrowAnException(int xShift, int yShift) {
        Point point = new Point(HEIGHT + xShift, WIDTH + yShift);
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        GameManager gameManager = new GameManager(new GameConfiguration(dimension, 0));
        assertThrows(IllegalArgumentException.class, () -> gameManager.actionAt(point));
    }

    @Disabled
    @Test
    void givenFreshGameReturnBoardWithOnlyCoveredSymbol() {
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        GameManager gameManager = new GameManager(new GameConfiguration(dimension, 0));
        Map<Point, GameSymbol> expectedBoard = new LinkedHashMap<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                expectedBoard.put(new Point(i, j), GameSymbol.COVERED);
            }
        }
        assertEquals(expectedBoard, gameManager.getMapBoard());
    }

    @ParameterizedTest
    @MethodSource("generatePointsRepresentingActionAt")
    void givenPointUncoverTile(Point pointToUncover) {
        boardManager.actionAt(pointToUncover);
        assertNotEquals(GameSymbol.COVERED, boardManager.getMapBoard().get(pointToUncover));
    }

    @ParameterizedTest
    @MethodSource
    void onFirstClickNotOnEdgeUncoverAtLeastNineSpots(Point pointOfAction) {
        boardManager.actionAt(pointOfAction);
        int uncoveredTiles = WIDTH * HEIGHT - Collections.frequency(boardManager.getMapBoard().values(), GameSymbol.COVERED);
        assertTrue(uncoveredTiles >= 9);
    }

    @ParameterizedTest
    @MethodSource
    void onFirstClickOnEdgeUncoverAtLeastFourSpots(Point pointOfAction) {
        boardManager.actionAt(pointOfAction);
        int uncoveredTiles = WIDTH * HEIGHT - Collections.frequency(boardManager.getMapBoard().values(), GameSymbol.COVERED);
        assertTrue(uncoveredTiles >= 4);
    }

    @ParameterizedTest
    @MethodSource("generatePointsRepresentingActionAt")
    void onFirstClickUncoverAtLeastNineSpotsButMines(Point pointOfAction) {
        boardManager.actionAt(pointOfAction);
        int coveredTiles = Collections.frequency(boardManager.getMapBoard().values(), GameSymbol.COVERED);
        assertTrue(coveredTiles >= minesNumber);
    }

    @Disabled
    @Test
    void whenClickOnAMineDeclareDefeat() {
        BoardManager boardManager = new BoardManager(new GameConfiguration(new Dimension(3, 3), 5));
        boardManager.actionAt(new Point(0, 0));
        assertEquals(ActionOutcome.DEFEAT, boardManager.actionAt(new Point(2, 2)));
    }

    private static Stream<Point> generatePointsRepresentingActionAt() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, HEIGHT)
                .forEach(i -> IntStream.range(0, WIDTH)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

    private static Stream<Point> onFirstClickNotOnEdgeUncoverAtLeastNineSpots() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(1, HEIGHT - 1)
                .forEach(i -> IntStream.range(1, WIDTH - 1)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

    private static Stream<Point> onFirstClickOnEdgeUncoverAtLeastFourSpots() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, HEIGHT)
                .forEach(i -> points.add(new Point(i, 0))
                );
        IntStream.range(0, WIDTH)
                .forEach(j -> points.add(new Point(0, j))
                );
        IntStream.range(0, HEIGHT)
                .forEach(i -> points.add(new Point(i, WIDTH - 1))
                );
        IntStream.range(0, WIDTH)
                .forEach(j -> points.add(new Point(HEIGHT - 1, j))
                );
        return points.stream();
    }

}
