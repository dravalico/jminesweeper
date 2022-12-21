import it.units.sdm.jminesweeper.Board;
import it.units.sdm.jminesweeper.GameConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpertModeBoardTest {
    private Board board;
    private Map<Point, String> expected;
    private Dimension boardDimension;
    private int minesNumber;

    @BeforeEach
    void init() {
        boardDimension = new Dimension(30, 16);
        minesNumber = 99;
        board = new Board(new GameConfiguration(boardDimension, minesNumber));
        expected = new LinkedHashMap<>();
    }

    @Test
    void givenBoardSizeGenerateBoardWithCoveredSymbol() {
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                expected.put(new Point(i, j), "o");
            }
        }
        assertEquals(board.getGameBoard(), expected);
    }

}
