package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.core.BoardManager;
import it.units.sdm.jminesweeper.event.GameEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BoardView implements View {
    private final Controller controller;
    private final JPanel panel;
    private final BoardManager model;
    private final Dimension boardDimension;
    private final List<Cell> cells;
    private final SoundsPlayer soundsPlayer;

    public BoardView(Controller controller, BoardManager model, JPanel boardPanel, Dimension boardDimension) {
        this.controller = controller;
        this.model = model;
        panel = boardPanel;
        this.boardDimension = boardDimension;
        cells = new ArrayList<>();
        soundsPlayer = SoundsPlayer.getInstance();
    }

    @Override
    public void createAndShowGUI() {
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
        switch (event.getEventType()) {
            case PROGRESS -> {
                soundsPlayer.playClick();
                updateView();
            }
            case VICTORY -> {
                soundsPlayer.playVictory();
                disableAllCells();
                updateView();
                setupWinningView();
            }
            case DEFEAT -> {
                soundsPlayer.playDefeat();
                disableAllCells();
                updateView();
            }
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
                    switch (cell.getGameSymbol()) {
                        case COVERED -> soundsPlayer.playPutFlag();
                        case FLAG -> soundsPlayer.playRemoveFlag();
                    }
                    controller.onRightClick(cell);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cell.setBackground(Color.decode(GameStyle.CELL_HOVER_COLOR.getValue()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.setProperBackground();
            }
        });
    }

    private void updateView() {
        Map<Point, GameSymbol> actualBoard = model.getBoardStatus();
        Arrays.stream(panel.getComponents())
                .filter(Cell.class::isInstance)
                .map(c -> (Cell) c)
                .filter(c -> (actualBoard.get(c.getPosition()) != GameSymbol.COVERED) &&
                        (c.getGameSymbol() != actualBoard.get(c.getPosition())))
                .forEach(c -> {
                            c.setGameSymbol(actualBoard.get(c.getPosition()));
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
