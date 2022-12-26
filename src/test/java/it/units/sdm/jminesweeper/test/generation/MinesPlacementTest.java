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
        MinesPlacerUtil.placeMine(board, new Point(0, 0));
        int minesNumber = Collections.frequency(board.values(), new TileValue(GameSymbol.MINE));
        assertEquals(1, minesNumber);
    }

    @ParameterizedTest
    @MethodSource
    void placeAMineOnTheBeginnerBoardAvoidingAPointAndItsNeighborhood(Point point) {
        Map<Point, TileValue> board = new LinkedHashMap<>();
        int width = 9;
        int height = 9;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), new TileValue(GameSymbol.EMPTY));
            }
        }
        MinesPlacerUtil.placeMine(board, point);
        int minesNumber = Collections.frequency(board.values(), new TileValue(GameSymbol.MINE));
        assertEquals(1, minesNumber);
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
                if (board.get(temp).getValue().equals(GameSymbol.MINE)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Stream<Point> placeAMineOnTheBeginnerBoardAvoidingAPointAndItsNeighborhood() {
        int boardDimension = 9;
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, boardDimension)
                .forEach(i -> IntStream.range(0, boardDimension)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

}
