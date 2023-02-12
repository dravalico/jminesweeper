package it.units.sdm.jminesweeper.test.logic.generation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.BoardInitialiser;
import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.GameSymbol;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardFillingTest {

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16", "32,60"})
    void givenBoardSizeGenerateBoardWithOnlyEmptySymbolAndCorrectDimension(int width, int height) {
        Dimension boardDimension = new Dimension(width, height);
        BoardInitialiser boardInitializer = new BoardInitialiser(new GameConfiguration(boardDimension, 0),
                new GuassianMinesPlacer());
        Map<Point, Tile> expectedBoard = new LinkedHashMap<>();
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                expectedBoard.put(new Point(i, j), new Tile(GameSymbol.EMPTY));
            }
        }
        Map<Point, Tile> actualBoard = new LinkedHashMap<>();

        boardInitializer.fillBoard(actualBoard);

        assertEquals(expectedBoard, actualBoard);
    }

}
