import it.units.sdm.jminesweeper.Board;
import it.units.sdm.jminesweeper.GameConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeginnerModeBoardTest {
    private Board board;
    private Map<Point, String> expected;
    private Dimension boardDimension;
    private int minesNumber;

    @BeforeEach
    void init() {
        boardDimension = new Dimension(9, 9);
        minesNumber = 10;
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

    @ParameterizedTest
    @CsvSource({"0,0", "8,8", "4,4", "2,2", "1,1", "3,4", "7,5", "8,0", "6,1"})
    void givenCoordinatesUncoverTile(int x, int y) {
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                if ((i == x) && (j == y)) {
                    expected.put(new Point(i, j), "-");
                    continue;
                }
                expected.put(new Point(i, j), "o");
            }
        }
        board.actionAt(new Point(x, y));
        assertEquals(board.getGameBoard(), expected);
    }

}
