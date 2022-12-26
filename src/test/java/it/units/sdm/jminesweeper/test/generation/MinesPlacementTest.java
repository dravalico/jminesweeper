package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.MinesPlacerUtil;
import it.units.sdm.jminesweeper.TileValue;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinesPlacementTest {

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16"})
    void placeAMineOnTheBoard(int width, int height) {
        Map<Point, TileValue> board = new LinkedHashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), new TileValue(GameSymbol.EMPTY));
            }
        }
        int expectedMinesNumber = 1;
        MinesPlacerUtil.placeMines(board, expectedMinesNumber, new Point(0, 0));
        int actualMinesNumber = Collections.frequency(board.values(), new TileValue(GameSymbol.MINE));
        assertEquals(expectedMinesNumber, actualMinesNumber);
    }

    @ParameterizedTest
    @MethodSource
    void placeAMineOnTheBoardAvoidingAPointAndItsNeighborhood(Point point) {
        Map<Point, TileValue> board = new LinkedHashMap<>();
        int width = 30;
        int height = 16;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), new TileValue(GameSymbol.EMPTY));
            }
        }
        int expectedMinesNumber = 1;
        MinesPlacerUtil.placeMines(board, expectedMinesNumber, point);
        int actualMinesNumber = Collections.frequency(board.values(), new TileValue(GameSymbol.MINE));
        assertEquals(expectedMinesNumber, actualMinesNumber);
        assertTrue(notMineInPointAndNeighborhood(board, point, width, height));
    }

    private boolean notMineInPointAndNeighborhood(Map<Point, TileValue> board, Point point, int width, int height) {
        int iStart = (point.x == 0 ? 0 : -1);
        int iStop = (point.x == width - 1 ? 0 : 1);
        int jStart = (point.y == 0 ? 0 : -1);
        int jStop = (point.y == height - 1 ? 0 : 1);
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

    private static Stream<Point> placeAMineOnTheBoardAvoidingAPointAndItsNeighborhood() {
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
