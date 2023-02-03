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
    private final BoardController boardController;
    private JPanel boardPanel;
    private final GameManager model;
    private final Dimension boardDimension;

    public BoardView(BoardController boardController, GameManager model, Dimension boardDimension) {
        this.boardController = boardController;
        this.model = model;
        this.boardDimension = boardDimension;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void initBoard() {
        JFrame boardFrame = new JFrame("Minesweeper");
        boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        boardPanel = new JPanel(new GridLayout());
        boardFrame.add(boardPanel, BorderLayout.CENTER);
        boardPanel.setLayout(new GridLayout(boardDimension.height, boardDimension.width));
        int cellSideLength = computeCellSideLength(boardDimension.height);
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                Cell cell = new Cell(i, j, cellSideLength, model.getSymbolAt(new Point(i, j)));
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        int buttonEvent = mouseEvent.getButton();
                        if (buttonEvent == MouseEvent.BUTTON1) {
                            boardController.onLeftClick(cell);
                        }
                        if (buttonEvent == MouseEvent.BUTTON3) {
                            boardController.onRightClick(cell);
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
                boardPanel.add(cell);
            }
        }
        boardFrame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = boardFrame.getWidth();
        int height = boardFrame.getHeight();
        boardFrame.setLocation((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2));
        boardFrame.setResizable(false);
        boardFrame.setVisible(true);
    }

    @Override
    public void onGameEvent(GameEvent event) {
        refreshBoard();
    }

    private int computeCellSideLength(int height) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        return (int) (screenSize.height / (height + 8.0));
    }

    private void refreshBoard() {
        Arrays.stream(boardPanel.getComponents())
                .filter(Cell.class::isInstance)
                .forEach(c -> {
                    GameSymbol gameSymbol = model.getSymbolAt(((Cell) c).getPosition());
                    if (gameSymbol != GameSymbol.COVERED) {
                        ((Cell) c).setGameSymbol(gameSymbol);
                        removeAllMouseListener(((Cell) c));
                    }
                });
    }

    private static void removeAllMouseListener(JButton jButton) {
        for (MouseListener mouseListener : jButton.getMouseListeners()) {
            jButton.removeMouseListener(mouseListener);
        }
    }

}
