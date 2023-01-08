package it.units.sdm.jminesweeper.test.generation;

import it.units.sdm.jminesweeper.BoardBuilder;
import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.Tile;
import it.units.sdm.jminesweeper.generation.BoardInitializer;
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

class BoardNumbersComputationTest {
    private static final String ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS = "3x3boards";
    private static final String FILENAME_FOR_EXPECTED = "expected.csv";
    private static final String FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION = "actual_before_computation.csv";
    private Map<Point, Tile> expectedMapBoard;
    private Map<Point, Tile> actualMapBoard;
    private BoardInitializer boardInitializer;

    @BeforeEach
    void init() {
        expectedMapBoard = null;
        actualMapBoard = null;
        boardInitializer = null;
    }

    @Test
    void placeAOneIn2x1BoardWithOneMine() {
        Dimension dimension = new Dimension(2, 1);
        expectedMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new Tile(GameSymbol.ONE),
                new Point(0, 1), new Tile(GameSymbol.MINE)));

        boardInitializer = new BoardInitializer(new GameConfiguration(dimension, 1),
                (board, minesNumber, firstClick) -> board.replace(new Point(0, 1), new Tile(GameSymbol.MINE))
        );

        assertEquals(expectedMapBoard, boardInitializer.init(new Point(0, 0)));
    }

    @Test
    void computeNoChangeIn2x1BoardWithNoMine() {
        Dimension dimension = new Dimension(2, 1);
        expectedMapBoard = Map.of(new Point(0, 0), new Tile(GameSymbol.EMPTY),
                new Point(0, 1), new Tile(GameSymbol.EMPTY));

        boardInitializer = new BoardInitializer(new GameConfiguration(dimension, 0),
                (board, minesNumber, firstClick) -> {
                });

        assertEquals(expectedMapBoard, boardInitializer.init(new Point(0, 0)));
    }

    @Test
    void placeNumbersIn3x3BoardWithOneCentralMine() {
        Dimension dimension = new Dimension(3, 3);
        expectedMapBoard = csvParser(ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS
                + "/one_mine_central/" + FILENAME_FOR_EXPECTED);

        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS
                + "/one_mine_central/" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        it.units.sdm.jminesweeper.generation.MinesPlacer<Map<Point, Tile>, Point> minesPlacer
                = (board, minesNumber, firstClick) -> Objects.requireNonNull(csvParser(actualBeforeComputationPath))
                .forEach(board::replace);

        boardInitializer = new BoardInitializer(new GameConfiguration(dimension, 1), minesPlacer);

        assertEquals(expectedMapBoard, boardInitializer.init(new Point(0, 0)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"one_mines", "two_mines", "three_mines",
            "four_mines", "five_mines", "six_mines",
            "seven_mines", "eight_mines"})
    void placeNumbersIn3x3BoardWithIncrementalMinesNumber(String folderName) {
        Dimension dimension = new Dimension(3, 3);
        String rootFolderName = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS + "/incremental_pattern/" + folderName;
        expectedMapBoard = csvParser(rootFolderName + "/" + FILENAME_FOR_EXPECTED);

        String actualBeforeComputationPath = rootFolderName + "/" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        it.units.sdm.jminesweeper.generation.MinesPlacer<Map<Point, Tile>, Point> minesPlacer
                = (board, minesNumber, firstClick) -> Objects.requireNonNull(csvParser(actualBeforeComputationPath))
                .forEach(board::replace);

        boardInitializer = new BoardInitializer(new GameConfiguration(dimension, 1), minesPlacer);

        assertEquals(expectedMapBoard, boardInitializer.init(new Point(0, 0)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"pattern1", "pattern2", "pattern3", "pattern4"})
    void placeNumbersIn3x3BoardWithParticularPattern(String folderName) {
        String rootFolderName = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS + "/particular_pattern/" + folderName;
        expectedMapBoard = csvParser(rootFolderName + "/" + FILENAME_FOR_EXPECTED);
        actualMapBoard = csvParser(rootFolderName + "/" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION);
        BoardBuilder.computeNumberForCells(actualMapBoard);
        assertEquals(expectedMapBoard, actualMapBoard);
    }

    private Map<Point, Tile> csvParser(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String absolutePath = file.getAbsolutePath();
        try (Scanner scanner = new Scanner(new File(absolutePath))) {
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
            return mapBoard;
        } catch (FileNotFoundException e) {

        }
        return null;
    }

}
