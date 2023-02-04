package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.core.GameManager;
import it.units.sdm.jminesweeper.event.GameEvent;
import it.units.sdm.jminesweeper.event.GameEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardView implements GameEventListener {
    private final Controller controller;
    private final JPanel panel;
    private final GameManager model;
    private final Dimension boardDimension;
    private final List<Cell> cells;

    public BoardView(Controller controller, GameManager model, JPanel boardPanel, Dimension boardDimension) {
        this.controller = controller;
        this.model = model;
        panel = boardPanel;
        this.boardDimension = boardDimension;
        cells = new ArrayList<>();
    }

    public void initBoard() {
        panel.setLayout(new GridLayout(boardDimension.height, boardDimension.width));
        int cellSideLength = computeCellSideLength();
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                Cell cell = new Cell(i, j, cellSideLength, model.getSymbolAt(new Point(i, j)));
                cells.add(cell);
                addMouseInteraction(cell);
                panel.add(cell);
            }
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        updateView();
        switch (event.getEventType()) {
            case VICTORY -> {
                setupWinningView();
                disableAllCells();
            }
            case DEFEAT -> disableAllCells();
        }
    }

    public List<Cell> getCells() {
        return cells;
    }

    private int computeCellSideLength() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int shortestScreenSide = Math.min(screenSize.height, screenSize.width);
        return (int) (shortestScreenSide / (boardDimension.height + 8.0));
    }

    private void addMouseInteraction(Cell cell) {
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                int buttonEvent = mouseEvent.getButton();
                if (buttonEvent == MouseEvent.BUTTON1) {
                    controller.onLeftClick(cell);
                }
                if (buttonEvent == MouseEvent.BUTTON3) {
                    controller.onRightClick(cell);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cell.setBackground(Color.decode("#dcf5b0"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.setProperBackground();
            }
        });
    }

    private void updateView() {
        Arrays.stream(panel.getComponents())
                .filter(Cell.class::isInstance)
                .map(c -> (Cell) c)
                .filter(c -> model.getSymbolAt(c.getPosition()) != GameSymbol.COVERED)
                .forEach(c -> {
                            c.setGameSymbol(model.getSymbolAt(c.getPosition()));
                            removeAllMouseListeners(c);
                        }
                );
    }

    private static void removeAllMouseListeners(JButton jButton) {
        for (MouseListener mouseListener : jButton.getMouseListeners()) {
            jButton.removeMouseListener(mouseListener);
        }
    }

    private void setupWinningView() {
        Arrays.stream(panel.getComponents())
                .filter(Cell.class::isInstance)
                .forEach(c -> ((Cell) c).victoryStyle());
    }

    private void disableAllCells() {
        Arrays.stream(panel.getComponents())
                .filter(JButton.class::isInstance)
                .forEach(c -> removeAllMouseListeners((JButton) c));
    }

}
