import it.units.sdm.jminesweeper.MinesPlacerUtil;
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
        Map<Point, String> board = new LinkedHashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.put(new Point(i, j), "-");
            }
        }
        MinesPlacerUtil.placeMine(board);
        int minesNumber = Collections.frequency(board.values(), "*");
        assertEquals(1, minesNumber);
    }

}
