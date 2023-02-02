package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.core.GameManager;
import it.units.sdm.jminesweeper.event.GameEvent;
import it.units.sdm.jminesweeper.event.GameEventListener;

import javax.swing.*;
import java.awt.*;

public class BoardView implements GameEventListener {
    private JFrame boardFrame;
    private final GameManager model;
    private final Dimension boardDimension;

    public BoardView(GameManager model, Dimension boardDimension) {
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
        boardFrame = new JFrame("Minesweeper");
        boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel boardPanel = new JPanel(new GridLayout());
        boardFrame.add(boardPanel, BorderLayout.CENTER);
        boardPanel.setLayout(new GridLayout(boardDimension.height, boardDimension.width));
        int cellSideLength = computeCellSideLength(boardDimension.height);
        for (int i = 0; i < boardDimension.height; i++) {
            for (int j = 0; j < boardDimension.width; j++) {
                JButton jButton = new JButton();
                jButton.setPreferredSize(new Dimension(cellSideLength, cellSideLength));
                boardPanel.add(jButton);
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
    }

    private int computeCellSideLength(int height) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        return (int) (screenSize.height / (height + 8.0));
    }

}
