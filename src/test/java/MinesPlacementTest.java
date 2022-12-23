import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.MinesPlacerUtil;
import it.units.sdm.jminesweeper.TileValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinesPlacementTest {

    @ParameterizedTest
    @CsvSource({"9,9", "16,16", "30,16"})
    void placeAMineOnTheBoard(int width, int height) {
        Map<Point, TileValue> board = new LinkedHashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), new TileValue(GameSymbol.EMPTY));
            }
        }
        MinesPlacerUtil.placeMine(board);
        int minesNumber = Collections.frequency(board.values(), new TileValue(GameSymbol.MINE));
        assertEquals(1, minesNumber);
    }

}
