package it.units.sdm.jminesweeper.core;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.enumeration.ActionOutcome;

import java.awt.*;
import java.util.Map;

public class GameManager extends AbstractBoard<Map<Point, Tile>> implements ActionHandler<Point, ActionOutcome> {
    private final GameConfiguration gameConfiguration;

    public GameManager(GameConfiguration gameConfiguration) {
        super(null);
        this.gameConfiguration = gameConfiguration;
    }

    @Override
    public ActionOutcome actionAt(Point point) {
        verifyPointWithinBoardDimension(point);
        return null;
    }

    private void verifyPointWithinBoardDimension(Point point) {
        Dimension boardDimension = gameConfiguration.dimension();
        if (((point.x < 0) || (point.x >= boardDimension.height)) || ((point.y < 0) || (point.y >= boardDimension.width))) {
            throw new IllegalArgumentException("Coordinates not allowed!");
        }
    }

}
