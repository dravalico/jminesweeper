package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.BoardBuilder;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.Tile;
import org.junit.jupiter.api.BeforeEach;
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
    private static final String ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS = "3x3boards";
    private static final String FILENAME_FOR_EXPECTED = "expected.csv";
    private static final String FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION = "actual_before_computation.csv";
    private Map<Point, Tile> expectedMapBoard;
    private Map<Point, Tile> actualMapBoard;

    @BeforeEach
    void init() {
        expectedMapBoard = null;
        actualMapBoard = null;
    }

    @Test
    void placeAOneIn2x1BoardWithOneMine() {
        expectedMapBoard = Map.of(new Point(0, 0), new Tile(GameSymbol.ONE),
                new Point(0, 1), new Tile(GameSymbol.MINE));
        actualMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new Tile(GameSymbol.EMPTY),
                new Point(0, 1), new Tile(GameSymbol.MINE)));
        BoardBuilder.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @Test
    void computeNoChangeIn2x1BoardWithNoMine() {
        expectedMapBoard = Map.of(new Point(0, 0), new Tile(GameSymbol.EMPTY),
                new Point(0, 1), new Tile(GameSymbol.EMPTY));
        actualMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new Tile(GameSymbol.EMPTY),
                new Point(0, 1), new Tile(GameSymbol.EMPTY)));
        BoardBuilder.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @Test
    void placeNumbersIn3x3BoardWithOneCentralMine() throws FileNotFoundException {
        expectedMapBoard = csvParser(ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS
                + "/one_mine_central/" + FILENAME_FOR_EXPECTED);
        actualMapBoard = csvParser(ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS
                + "/one_mine_central/" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION);
        BoardBuilder.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @ParameterizedTest
    @ValueSource(strings = {"one_mines", "two_mines", "three_mines",
            "four_mines", "five_mines", "six_mines",
            "seven_mines", "eight_mines"})
    void placeNumbersIn3x3BoardWithIncrementalMinesNumber(String folderName) throws FileNotFoundException {
        String rootFolderName = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS + "/incremental_pattern/" + folderName;
        expectedMapBoard = csvParser(rootFolderName
                + "/" + FILENAME_FOR_EXPECTED);
        actualMapBoard = csvParser(rootFolderName
                + "/" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION);
        BoardBuilder.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pattern1", "pattern2", "pattern3", "pattern4"})
    void placeNumbersIn3x3BoardWithParticularPattern(String folderName) throws FileNotFoundException {
        String rootFolderName = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS + "/particular_pattern/" + folderName;
        expectedMapBoard = csvParser(rootFolderName
                + "/" + FILENAME_FOR_EXPECTED);
        actualMapBoard = csvParser(rootFolderName
                + "/" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION);
        BoardBuilder.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    private Map<Point, Tile> csvParser(String resourceName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String absolutePath = file.getAbsolutePath();
        Scanner scanner = new Scanner(new File(absolutePath));
        Map<Point, Tile> mapBoard = new LinkedHashMap<>();
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
                mapBoard.put(new Point(rowCounter, i), new Tile(gameSymbol));
            }
            rowCounter = rowCounter + 1;
        }
        scanner.close();
        return mapBoard;
    }

}
