import it.units.sdm.jminesweeper.Board;
import it.units.sdm.jminesweeper.GameConfiguration;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    @Test
    void givenBeginnerBoardSizeGenerateBoardWithCoveredSymbol() {
        Map<Point, String> expected = new LinkedHashMap<>();
        int height = 9;
        int width = 9;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                expected.put(new Point(i, j), "o");
            }
        }
        Board board = new Board(new GameConfiguration(new Dimension(9, 9), 0));
        assertEquals(board.getGameBoard(), expected);
    }

}
