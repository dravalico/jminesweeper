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
import java.util.Arrays;

public class BoardView implements GameEventListener {
    private final Controller controller;
    private final JPanel boardPanel;
    private final GameManager model;
    private final Dimension boardDimension;

    public BoardView(Controller controller, GameManager model, JPanel boardPanel, Dimension boardDimension) {
        this.controller = controller;
        this.model = model;
        this.boardPanel = boardPanel;
        this.boardDimension = boardDimension;
    }

    public void initBoard() {
        boardPanel.setLayout(new GridLayout(boardDimension.height, boardDimension.width));
        int cellSideLength = computeCellSideLength(boardDimension.height);
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                Cell cell = new Cell(i, j, cellSideLength, model.getSymbolAt(new Point(i, j)));
                addMouseListeners(cell);
                boardPanel.add(cell);
            }
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        refreshBoard();
        switch (event.getEventType()) {
            case VICTORY -> {
                clearBoard();
                disableBoardView();
            }
            case DEFEAT -> disableBoardView();
        }
    }

    private int computeCellSideLength(int height) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        return (int) (screenSize.height / (height + 8.0));
    }

    private void addMouseListeners(Cell cell) {
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

    private void refreshBoard() {
        Arrays.stream(boardPanel.getComponents())
                .filter(Cell.class::isInstance)
                .map(c -> (Cell) c)
                .filter(c -> model.getSymbolAt(c.getPosition()) != GameSymbol.COVERED)
                .forEach(c -> {
                            c.setGameSymbol(model.getSymbolAt(c.getPosition()));
                            removeAllMouseListener(c);
                        }
                );
    }

    private static void removeAllMouseListener(JButton jButton) {
        for (MouseListener mouseListener : jButton.getMouseListeners()) {
            jButton.removeMouseListener(mouseListener);
        }
    }

    private void clearBoard() {
        Arrays.stream(boardPanel.getComponents())
                .filter(Cell.class::isInstance)
                .forEach(c -> ((Cell) c).victoryStyle());
    }

    private void disableBoardView() {
        Arrays.stream(boardPanel.getComponents())
                .filter(JButton.class::isInstance)
                .forEach(c -> removeAllMouseListener((JButton) c));
    }

}
