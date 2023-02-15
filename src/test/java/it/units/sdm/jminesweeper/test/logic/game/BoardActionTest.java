package it.units.sdm.jminesweeper.test.logic.game;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.core.BoardManager;
import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.core.generation.MinesPlacer;
import it.units.sdm.jminesweeper.event.EventType;
import it.units.sdm.jminesweeper.event.GameEvent;
import it.units.sdm.jminesweeper.event.GameEventListener;
import it.units.sdm.jminesweeper.test.CSVParserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardActionTest {
    private static final Dimension BOARD_DIMENSION = new Dimension(30, 16);
    private static final GameConfiguration BEGINNER_CONFIGURATION = GameConfiguration.fromDifficulty(GameConfiguration.Difficulty.BEGINNER);
    private static final String ROOT_FOLDER_NAME_FOR_BOARDS = "board_actions/";
    private static final String FILENAME_FOR_EXPECTED = "/expected.csv";
    private static final String FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION = "/actual_before_computation.csv";
    private BoardManager boardManager;

    @ParameterizedTest
    @CsvSource({"0,-1", "-1,0", "0,0", "1, 1", "2, 3", "100, -100", "20,4", "-5,2", "-4,13"})
    void givenAPointOutOfTheBoardThrowAnException(int xShift, int yShift) {
        Point point = new Point(BOARD_DIMENSION.height + xShift, BOARD_DIMENSION.width + yShift);
        boardManager = new BoardManager(new GameConfiguration(BOARD_DIMENSION, 0), null);
        assertThrows(IllegalArgumentException.class, () -> boardManager.actionAt(point));
    }

    @Test
    void givenFreshGameReturnBoardWithOnlyCoveredSymbol() {
        boardManager = new BoardManager(new GameConfiguration(BOARD_DIMENSION, 0), null);
        Map<Point, GameSymbol> expectedBoard = new LinkedHashMap<>();
        for (int i = 0; i < BOARD_DIMENSION.height; i++) {
            for (int j = 0; j < BOARD_DIMENSION.width; j++) {
                expectedBoard.put(new Point(i, j), GameSymbol.COVERED);
            }
        }
        assertEquals(expectedBoard, boardManager.getBoardStatus());
    }

    @ParameterizedTest
    @MethodSource("generatePointsRepresentingActionAt")
    void givenPointUncoverTile(Point pointToUncover) {
        boardManager = new BoardManager(new GameConfiguration(BOARD_DIMENSION, 0), new GuassianMinesPlacer());
        boardManager.actionAt(pointToUncover);
        assertNotEquals(GameSymbol.COVERED, boardManager.getBoardStatus().get(pointToUncover));
    }

    @Test
    void onFirstClickUncoverSpotsCorrectlyWithRespectToPredefinedBoard() {
        Map<Point, GameSymbol> expectedMapBoard = CSVParserUtil.ParseGameSymbols(ROOT_FOLDER_NAME_FOR_BOARDS
                + "first_click_outcome1" + FILENAME_FOR_EXPECTED);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);

        boardManager.actionAt(new Point(3, 3));

        assertEquals(expectedMapBoard, boardManager.getBoardStatus());
    }

    static private class TestListener implements GameEventListener {
        private EventType eventTypeReceived;

        @Override
        public void onGameEvent(GameEvent event) {
            eventTypeReceived = event.getEventType();
        }

    }

    @Test
    void onFirstClickNotifyProgressEvent() {
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);
        TestListener testListener = new TestListener();
        boardManager.addListener(testListener, EventType.PROGRESS);

        boardManager.actionAt(new Point(3, 3));

        assertEquals(EventType.PROGRESS, testListener.eventTypeReceived);
    }

    @ParameterizedTest
    @CsvSource({"0,0,1", "8,5,2", "0,8,3", "5,1,1", "2,8,2", "7,5,3"})
    void onClickOnNumberUncoverOnlyTileInClickPosition(int x, int y, int tileValue) {
        Map<Point, GameSymbol> expectedMapBoard = CSVParserUtil.ParseGameSymbols(ROOT_FOLDER_NAME_FOR_BOARDS
                + "first_click_outcome1" + FILENAME_FOR_EXPECTED);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);

        boardManager.actionAt(new Point(3, 3));
        boardManager.actionAt(new Point(x, y));
        assert expectedMapBoard != null;
        expectedMapBoard.replace(new Point(x, y), GameSymbol.fromInt(tileValue));

        assertEquals(expectedMapBoard, boardManager.getBoardStatus());
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,0", "0,7", "1,7", "1,8", "5,2", "6,3", "6,6", "7,4", "8,6"})
    void onClickOnAMineNotifyDefeatEvent(int x, int y) {
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);
        TestListener testListener = new TestListener();
        boardManager.addListener(testListener, EventType.DEFEAT);

        boardManager.actionAt(new Point(3, 3));
        boardManager.actionAt(new Point(x, y));

        assertEquals(EventType.DEFEAT, testListener.eventTypeReceived);
    }

    @Test
    void onAllSpotsUncoveredButMinesNotifyVictoryEvent() {
        Dimension boardDimension = new Dimension(4, 4);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "victory_example1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(new GameConfiguration(boardDimension, 2), minesPlacer);
        TestListener testListener = new TestListener();
        boardManager.addListener(testListener, EventType.VICTORY);

        boardManager.actionAt(new Point(3, 0));
        boardManager.actionAt(new Point(0, 1));
        boardManager.actionAt(new Point(0, 2));
        boardManager.actionAt(new Point(0, 3));

        assertEquals(EventType.VICTORY, testListener.eventTypeReceived);
    }

    @Test
    void onClickOnUncoveredSpotDoesNotAlterTheGameStatus() {
        Dimension boardDimension = new Dimension(4, 4);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "victory_example1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(new GameConfiguration(boardDimension, 2), minesPlacer);
        TestListener testListener = new TestListener();
        boardManager.addListener(testListener, EventType.VICTORY);

        boardManager.actionAt(new Point(3, 0));
        boardManager.actionAt(new Point(0, 1));
        boardManager.actionAt(new Point(3, 2));
        boardManager.actionAt(new Point(0, 1));
        boardManager.actionAt(new Point(0, 2));
        boardManager.actionAt(new Point(0, 2));
        boardManager.actionAt(new Point(0, 3));

        assertEquals(EventType.VICTORY, testListener.eventTypeReceived);
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,0", "0,7", "1,7", "1,8", "5,2", "6,3", "6,6", "7,4", "8,6"})
    void afterDefeatNotifyOnlyDefeatEvent(int x, int y) {
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);
        TestListener testListener = new TestListener();
        boardManager.addListener(testListener, EventType.values());

        boardManager.actionAt(new Point(3, 3));
        boardManager.actionAt(new Point(x, y));
        boardManager.actionAt(new Point(1, 1));

        assertEquals(EventType.DEFEAT, testListener.eventTypeReceived);
    }

    @ParameterizedTest
    @MethodSource
    void givenPointIn9x9BoardReturnGameSymbolAtThatPoint(Point point) {
        Map<Point, GameSymbol> expectedMapBoard = CSVParserUtil.ParseGameSymbols(ROOT_FOLDER_NAME_FOR_BOARDS
                + "first_click_outcome1" + FILENAME_FOR_EXPECTED);
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);

        boardManager.actionAt(new Point(3, 3));

        assert expectedMapBoard != null;
        assertEquals(expectedMapBoard.get(point), boardManager.getSymbolAt(point));
    }

    @ParameterizedTest
    @CsvSource({"0,-1", "-1,0", "0,0", "1, 1", "2, 3", "100, -100", "20,4", "-5,2", "-4,13"})
    void whenGetASymbolAtPointOutsideBoardThrowAnException(int xShift, int yShift) {
        Point point = new Point(BOARD_DIMENSION.height + xShift, BOARD_DIMENSION.width + yShift);
        boardManager = new BoardManager(new GameConfiguration(BOARD_DIMENSION, 0), null);
        assertThrows(IllegalArgumentException.class, () -> boardManager.getSymbolAt(point));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,0", "0,7", "1,7", "1,8", "5,2", "6,3", "6,6", "7,4", "8,6"})
    void onDefeatUncoverAllMines(int x, int y) {
        String actualBeforeComputationPath = ROOT_FOLDER_NAME_FOR_BOARDS + "first_click_outcome1"
                + FILENAME_FOR_ACTUAL_BEFORE_COMPUTATION;
        MinesPlacer<Map<Point, Tile>, Point> minesPlacer = (board, minesNumber, firstClick) ->
                Objects.requireNonNull(CSVParserUtil.ParseTiles(actualBeforeComputationPath))
                        .forEach(board::replace);
        boardManager = new BoardManager(BEGINNER_CONFIGURATION, minesPlacer);

        boardManager.actionAt(new Point(3, 3));
        boardManager.actionAt(new Point(x, y));

        int uncoveredMines = (int) boardManager.getBoardStatus().values()
                .stream()
                .filter(v -> v == GameSymbol.MINE)
                .count();

        assertEquals(BEGINNER_CONFIGURATION.minesNumber(), uncoveredMines);
    }

    private static Stream<Point> generatePointsRepresentingActionAt() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < BOARD_DIMENSION.height; i++) {
            for (int j = 0; j < BOARD_DIMENSION.width; j++) {
                points.add(new Point(i, j));
            }
        }
        return points.stream();
    }

    private static Stream<Point> givenPointIn9x9BoardReturnGameSymbolAtThatPoint() {
        Dimension dimension = new Dimension(9, 9);
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < dimension.height; i++) {
            for (int j = 0; j < dimension.width; j++) {
                points.add(new Point(i, j));
            }
        }
        return points.stream();
    }

}
