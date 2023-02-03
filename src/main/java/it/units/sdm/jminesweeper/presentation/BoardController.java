package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.core.GameManager;
import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.core.generation.MinesPlacer;
import it.units.sdm.jminesweeper.event.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BoardController {
    private GameConfiguration gameConfiguration;
    private final MinesPlacer<Map<Point, Tile>, Point> minesPlacer;
    private GameManager model;

    public BoardController(MinesPlacer<Map<Point, Tile>, Point> minesPlacer) {
        this.minesPlacer = minesPlacer;
        gameConfiguration = GameConfiguration.fromDifficulty(GameConfiguration.Difficulty.BEGINNER);
        model = new GameManager(gameConfiguration, minesPlacer);
    }

    public void startGame() {
        BoardView boardView = new BoardView(this, model, gameConfiguration.dimension());
        model.addListener(boardView, EventType.values());
        SwingUtilities.invokeLater(boardView::initBoard);
    }

    public void onLeftClick(Cell cell) {
        if (cell.getGameSymbol() == GameSymbol.FLAG) {
            return;
        }
        model.actionAt(cell.getPosition());
    }

    public void onRightClick(Cell cell) {
        switch (cell.getGameSymbol()) {
            case COVERED -> cell.setGameSymbol(GameSymbol.FLAG);
            case FLAG -> cell.setGameSymbol(GameSymbol.COVERED);
        }
    }

}
