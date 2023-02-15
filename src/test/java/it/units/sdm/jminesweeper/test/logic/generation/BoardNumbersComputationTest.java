package it.units.sdm.jminesweeper.test.logic.generation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.BoardInitialiser;
import it.units.sdm.jminesweeper.core.generation.MinesPlacer;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.test.CSVParserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardNumbersComputationTest {
    private static final String ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS = "board_number_computation/";
    private static final String FILENAME_FOR_EXPECTED = "/expected.csv";
    private static final String FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION = "/actual_before_computation.csv";
    private Map<Point, Tile> expectedMapBoard;
    private Map<Point, Tile> actualBoard;
    private BoardInitialiser boardInitializer;
    private Dimension dimension;

    @BeforeEach
    void initActualMap() {
        actualBoard = new LinkedHashMap<>();
    }

    @Test
    void placeAOneIn2x1BoardWithOneMine() {
        dimension = new Dimension(2, 1);
        expectedMapBoard = new LinkedHashMap<>(Map.of(new Point(0, 0), new Tile(GameSymbol.ONE),
                new Point(0, 1), new Tile(GameSymbol.MINE)));

        boardInitializer = new BoardInitialiser(new GameConfiguration(dimension, 1),
                (board, minesNumber, firstClick) -> board.replace(new Point(0, 1), new Tile(GameSymbol.MINE))
        );

        boardInitializer.fillBoard(actualBoard);
        boardInitializer.putMinesAndNumbers(actualBoard, new Point(0, 0));

        assertEquals(expectedMapBoard, actualBoard);
    }

    @Test
    void computeNoChangeIn2x1BoardWithNoMine() {
        dimension = new Dimension(2, 1);
        expectedMapBoard = Map.of(new Point(0, 0), new Tile(GameSymbol.EMPTY),
                new Point(0, 1), new Tile(GameSymbol.EMPTY));
        boardInitializer = new BoardInitialiser(new GameConfiguration(dimension, 0),
                (board, minesNumber, firstClick) -> {
                });

        boardInitializer.fillBoard(actualBoard);
        boardInitializer.putMinesAndNumbers(actualBoard, new Point(0, 0));

        assertEquals(expectedMapBoard, actualBoard);
    }

    @Test
    void placeNumbersIn3x3BoardWithOneCentralMine() {
        dimension = new Dimension(3, 3);
        expectedMapBoard = CSVParserUtil.ParseTiles(ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS
                + "one_mine_central" + FILENAME_FOR_EXPECTED);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS
                + "one_mine_central" + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardInitializer = new BoardInitialiser(new GameConfiguration(dimension, 1), minesPlacer);

        boardInitializer.fillBoard(actualBoard);
        boardInitializer.putMinesAndNumbers(actualBoard, new Point(0, 0));

        assertEquals(expectedMapBoard, actualBoard);
    }

    @ParameterizedTest
    @ValueSource(strings = {"one_mines", "two_mines", "three_mines",
            "four_mines", "five_mines", "six_mines",
            "seven_mines", "eight_mines"})
    void placeNumbersIn3x3BoardWithIncrementalMinesNumber(String folderName) {
        dimension = new Dimension(3, 3);
        String rootFolderName = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS + "incremental_pattern/" + folderName;
        expectedMapBoard = CSVParserUtil.ParseTiles(rootFolderName + FILENAME_FOR_EXPECTED);
        String actualBeforeComputationPath = rootFolderName + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath)).
                        forEach(board::replace);
        boardInitializer = new BoardInitialiser(new GameConfiguration(dimension, 1), minesPlacer);

        boardInitializer.fillBoard(actualBoard);
        boardInitializer.putMinesAndNumbers(actualBoard, new Point(0, 0));

        assertEquals(expectedMapBoard, actualBoard);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pattern1", "pattern2", "pattern3", "pattern4"})
    void placeNumbersIn3x3BoardWithParticularPattern(String folderName) {
        dimension = new Dimension(3, 3);
        String rootFolderName = ROOT_FOLDER_NAME_FOR_3_X_3_BOARDS + "particular_pattern" + "/" + folderName;
        expectedMapBoard = CSVParserUtil.ParseTiles(rootFolderName + FILENAME_FOR_EXPECTED);
        String actualBeforeComputationPath = rootFolderName + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardInitializer = new BoardInitialiser(new GameConfiguration(dimension, 1), minesPlacer);

        boardInitializer.fillBoard(actualBoard);
        boardInitializer.putMinesAndNumbers(actualBoard, new Point(0, 0));

        assertEquals(expectedMapBoard, actualBoard);
    }

}
