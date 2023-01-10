package it.units.sdm.jminesweeper.test.game;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.core.GameManager;
import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.core.generation.MinesPlacer;
import it.units.sdm.jminesweeper.enumeration.ActionOutcome;
import it.units.sdm.jminesweeper.enumeration.GameSymbol;
import it.units.sdm.jminesweeper.test.CSVParserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardActionTest {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 16;
    private static final Dimension BOARD_DIMENSION = new Dimension(WIDTH, HEIGHT);
    private static final String ROOT_FOLDER_NAME_FOR_BOARDS = "board_actions";
    private static final String FILENAME_FOR_EXPECTED = "expected.csv";
    private static final String FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION = "actual_before_computation.csv";
    private GameManager gameManager;

    @ParameterizedTest
    @CsvSource({"0,-1", "-1,0", "0,0", "1, 1", "2, 3", "100, -100", "20,4", "-5,2", "-4,13"})
    void givenAPointOutOfTheBoardThrowAnException(int xShift, int yShift) {
        Point point = new Point(HEIGHT + xShift, WIDTH + yShift);
        gameManager = new GameManager(new GameConfiguration(BOARD_DIMENSION, 0), null);
        assertThrows(IllegalArgumentException.class, () -> gameManager.actionAt(point));
    }

    @Test
    void givenFreshGameReturnBoardWithOnlyCoveredSymbol() {
        gameManager = new GameManager(new GameConfiguration(BOARD_DIMENSION, 0), null);
        Map<Point, GameSymbol> expectedBoard = new LinkedHashMap<>();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                expectedBoard.put(new Point(i, j), GameSymbol.COVERED);
            }
        }
        assertEquals(expectedBoard, gameManager.getBoardStatus());
    }

    @ParameterizedTest
    @MethodSource("generatePointsRepresentingActionAt")
    void givenPointUncoverTile(Point pointToUncover) {
        gameManager = new GameManager(new GameConfiguration(BOARD_DIMENSION, 0), new GuassianMinesPlacer());
        gameManager.actionAt(pointToUncover);
        assertNotEquals(GameSymbol.COVERED, gameManager.getBoardStatus().get(pointToUncover));
    }

    @Test
    void onFirstClickUncoverSpotsCorrectlyWithRespectToCsvFile() {
        Dimension boardDimension = new Dimension(9, 9);
        Map<Point, GameSymbol> expectedMapBoard = CSVParserUtil.csvParseGameSymbols(ROOT_FOLDER_NAME_FOR_BOARDS
                + "/first_click_outcome1/" + FILENAME_FOR_EXPECTED);

        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "/first_click_outcome1/" +
                FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.csvParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);

        gameManager = new GameManager(new GameConfiguration(boardDimension, 10), minesPlacer);
        gameManager.actionAt(new Point(3, 3));
        assertEquals(expectedMapBoard, gameManager.getBoardStatus());
    }

    @Test
    void onFirstClickReturnProgress() {
        Dimension boardDimension = new Dimension(9, 9);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "/first_click_outcome1/" +
                FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.csvParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        gameManager = new GameManager(new GameConfiguration(boardDimension, 10), minesPlacer);

        assertEquals(ActionOutcome.PROGRESS, gameManager.actionAt(new Point(3, 3)));
    }

    @ParameterizedTest
    @CsvSource({"0,0,1", "8,5,2", "0,8,3", "5,1,1", "2,8,2", "7,5,3"})
    void onClickOnNumberUncoverOnlyTileInClickPosition(int x, int y, int tileValue) {
        Dimension boardDimension = new Dimension(9, 9);
        Map<Point, GameSymbol> expectedMapBoard = CSVParserUtil.csvParseGameSymbols(ROOT_FOLDER_NAME_FOR_BOARDS
                + "/first_click_outcome1/" + FILENAME_FOR_EXPECTED);

        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "/first_click_outcome1/" +
                FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.csvParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);

        gameManager = new GameManager(new GameConfiguration(boardDimension, 10), minesPlacer);
        gameManager.actionAt(new Point(3, 3));
        gameManager.actionAt(new Point(x, y));
        expectedMapBoard.replace(new Point(x, y), GameSymbol.fromInt(tileValue));
        assertEquals(expectedMapBoard, gameManager.getBoardStatus());
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,0", "0,7", "1,7", "1,8", "5,2", "6,3", "6,6", "7,4", "8,6"})
    void onClickOnAMineDeclareDefeat(int x, int y) {
        Dimension boardDimension = new Dimension(9, 9);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "/first_click_outcome1/" +
                FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.csvParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        gameManager = new GameManager(new GameConfiguration(boardDimension, 10), minesPlacer);

        gameManager.actionAt(new Point(3, 3));
        assertEquals(ActionOutcome.DEFEAT, gameManager.actionAt(new Point(x, y)));
    }

    @Test
    void onAllSpotsUncoveredButMinesDeclareVictory() {
        Dimension boardDimension = new Dimension(4, 4);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "/victory_example1/" +
                FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.csvParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        gameManager = new GameManager(new GameConfiguration(boardDimension, 2), minesPlacer);

        gameManager.actionAt(new Point(3, 0));
        gameManager.actionAt(new Point(0, 1));
        gameManager.actionAt(new Point(0, 2));
        assertEquals(ActionOutcome.VICTORY, gameManager.actionAt(new Point(0, 3)));
    }

    private static Stream<Point> generatePointsRepresentingActionAt() {
        java.util.List<Point> points = new ArrayList<>();
        IntStream.range(0, HEIGHT)
                .forEach(i -> IntStream.range(0, WIDTH)
                        .forEach(j -> points.add(new Point(i, j)))
                );
        return points.stream();
    }

}
