package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.MinesPlacer;
import it.units.sdm.jminesweeper.Tile;
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

    @BeforeEach
    void init() {
        board = new LinkedHashMap<>();
    }

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16"})
    void placeAMineOnTheBoard(int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
        int expectedMinesNumber = 1;
        MinesPlacer.place(board, expectedMinesNumber, new Point(0, 0));
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));
        assertEquals(expectedMinesNumber, actualMinesNumber);
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void place10MinesOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        fillBoard();
        int expectedMinesNumber = 10;
        MinesPlacer.place(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));
        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point));
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void place32MinesOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        fillBoard();
        int expectedMinesNumber = 32;
        MinesPlacer.place(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));
        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point));
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void place99MinesOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        fillBoard();
        int expectedMinesNumber = 99;
        MinesPlacer.place(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new Tile(GameSymbol.MINE));
        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point));
    }

    @ParameterizedTest
    @MethodSource("generateEveryPointOnTheBoard")
    void throwExceptionIfNumberOfMinesIsGreaterThanMineableSpots(Point point) {
        fillBoard();
        int minesNumber = 480;
        assertThrows(IllegalArgumentException.class, () -> MinesPlacer.place(board, minesNumber, point));
    }

    private void fillBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
    }

    private boolean notMineInPointAndNeighborhood(Map<Point, Tile> board, Point point) {
        int iStart = (point.x == 0 ? 0 : -1);
        int iStop = (point.x == BOARD_WIDTH - 1 ? 0 : 1);
        int jStart = (point.y == 0 ? 0 : -1);
        int jStop = (point.y == BOARD_HEIGHT - 1 ? 0 : 1);
        for (int i = iStart; i <= iStop; i++) {
            for (int j = jStart; j <= jStop; j++) {
                Point temp = new Point(point.x + i, point.y + j);
                if (board.get(temp).isAMine()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Stream<Point> generateEveryPointOnTheBoard() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, BOARD_WIDTH)
                .forEach(i -> IntStream.range(0, BOARD_HEIGHT)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

}
