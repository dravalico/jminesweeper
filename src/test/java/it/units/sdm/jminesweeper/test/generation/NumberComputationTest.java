package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.BoardUtil;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.TileValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberComputationTest {

    @Test
    void placeAOneIn2x1BoardWithOneMine() {
        Map<Point, TileValue> expectedMapBoard = Map.of(new Point(0, 0), new TileValue(GameSymbol.ONE),
                new Point(0, 1), new TileValue(GameSymbol.MINE));
        Map<Point, TileValue> actualMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new TileValue(GameSymbol.EMPTY),
                new Point(0, 1), new TileValue(GameSymbol.MINE)));
        BoardUtil.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @Test
    void computeNoChangeIn2x1BoardWithNoMine() {
        Map<Point, TileValue> expectedMapBoard = Map.of(new Point(0, 0), new TileValue(GameSymbol.EMPTY),
                new Point(0, 1), new TileValue(GameSymbol.EMPTY));
        Map<Point, TileValue> actualMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new TileValue(GameSymbol.EMPTY),
                new Point(0, 1), new TileValue(GameSymbol.EMPTY)));
        BoardUtil.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @Test
    void placeNumbersIn3x3BoardWithOneCentralMine() throws FileNotFoundException {
        Map<Point, TileValue> expectedMapBoard = csvParser("3x3boards/central_mine/expected.csv");
        Map<Point, TileValue> actualMapBoard = csvParser("3x3boards/central_mine/actual_before_computation.csv");
        BoardUtil.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @ParameterizedTest
    @ValueSource(strings = {"one_mines", "two_mines", "three_mines", "four_mines", "five_mines", "six_mines", "seven_mines", "eight_mines"})
    void placeNumbersIn3x3Board(String folderName) throws FileNotFoundException {
        String rootFolderName = "3x3boards/" + folderName;
        Map<Point, TileValue> expectedMapBoard = csvParser(rootFolderName + "/expected.csv");
        Map<Point, TileValue> actualMapBoard = csvParser(rootFolderName + "/actual_before_computation.csv");
        BoardUtil.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    private Map<Point, TileValue> csvParser(String resourceName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String absolutePath = file.getAbsolutePath();
        Scanner scanner = new Scanner(new File(absolutePath));
        Map<Point, TileValue> mapBoard = new LinkedHashMap<>();
        int rowCounter = 0;
        while (scanner.hasNext()) {
            String[] row = scanner.next().split(",");
            for (int i = 0; i < row.length; i++) {
                GameSymbol gameSymbol = null;
                switch (row[i]) {
                    case "1" -> gameSymbol = GameSymbol.ONE;
                    case "2" -> gameSymbol = GameSymbol.TWO;
                    case "3" -> gameSymbol = GameSymbol.THREE;
                    case "4" -> gameSymbol = GameSymbol.FOUR;
                    case "5" -> gameSymbol = GameSymbol.FIVE;
                    case "6" -> gameSymbol = GameSymbol.SIX;
                    case "7" -> gameSymbol = GameSymbol.SEVEN;
                    case "8" -> gameSymbol = GameSymbol.EIGHT;
                    case "*" -> gameSymbol = GameSymbol.MINE;
                    case "-" -> gameSymbol = GameSymbol.EMPTY;
                }
                mapBoard.put(new Point(rowCounter, i), new TileValue(gameSymbol));
            }
            rowCounter = rowCounter + 1;
        }
        scanner.close();
        return mapBoard;
    }

}
