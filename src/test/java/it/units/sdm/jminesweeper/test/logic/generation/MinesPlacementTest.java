package it.units.sdm.jminesweeper.test.logic.generation;

import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.core.generation.MinesPlacer;
import it.units.sdm.jminesweeper.GameSymbol;
import org.junit.jupiter.api.BeforeEach;
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

class MinesPlacementTest {
    private final static int BOARD_WIDTH = 30;
    private final static int BOARD_HEIGHT = 16;
    private Map<Point, Tile> board;
    private MinesPlacer<Map<Point, Tile>, Point> minesPlacer;

    @BeforeEach
    void init() {
        board = new LinkedHashMap<>();
        minesPlacer = new GuassianMinesPlacer();
    }

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16"})
    void placeAMineOnTheBoard(int width, int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
        int expectedMinesNumber = 1;

        minesPlacer.place(board, expectedMinesNumber, new Point(0, 0));
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));

        assertEquals(expectedMinesNumber, actualMinesNumber);
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void place10MinesOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        fillExpectedBoard();
        int expectedMinesNumber = 10;

        minesPlacer.place(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));

        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point));
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void place32MinesOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        fillExpectedBoard();
        int expectedMinesNumber = 32;

        minesPlacer.place(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));

        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point));
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void place99MinesOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        fillExpectedBoard();
        int expectedMinesNumber = 99;

        minesPlacer.place(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));

        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point));
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void throwExceptionIfNumberOfMinesIsGreaterThanMineableSpots(Point point) {
        fillExpectedBoard();
        int minesNumber = 480;
        assertThrows(IllegalArgumentException.class, () -> minesPlacer.place(board, minesNumber, point));
    }

    private void fillExpectedBoard() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
    }

    private boolean notMineInPointAndNeighborhood(Map<Point, Tile> board, Point point) {
        int iStart = (point.x == 0 ? 0 : -1);
        int iStop = (point.x == BOARD_HEIGHT - 1 ? 0 : 1);
        int jStart = (point.y == 0 ? 0 : -1);
        int jStop = (point.y == BOARD_WIDTH - 1 ? 0 : 1);
        for (int i = iStart; i <= iStop; i++) {
            for (int j = jStart; j <= jStop; j++) {
                Point temp = new Point(point.x + i, point.y + j);
                if (board.get(temp).isMine()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Stream<Point> generateEveryPointOnTheBoard() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, BOARD_HEIGHT)
                .forEach(i -> IntStream.range(0, BOARD_WIDTH)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

}
