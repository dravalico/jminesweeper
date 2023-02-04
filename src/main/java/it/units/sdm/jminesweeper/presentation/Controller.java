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
import java.util.Objects;

public class Controller {
    private GameConfiguration gameConfiguration;
    private final MinesPlacer<Map<Point, Tile>, Point> minesPlacer;
    private GameManager model;
    private MenuView menuView;
    private BoardView boardView;
    private JFrame mainFrame;
    private boolean firstMove;

    public Controller(MinesPlacer<Map<Point, Tile>, Point> minesPlacer) {
        this.minesPlacer = minesPlacer;
    }

    public void startGame() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Minesweeper");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            initialiseMenuView();
            initialiseBoardView();
        });
    }

    public void onLeftClick(Cell cell) {
        if (cell.getGameSymbol() == GameSymbol.FLAG) {
            return;
        }
        if (firstMove) {
            menuView.getStopwatchLabel().start();
            firstMove = false;
        }
        model.actionAt(cell.getPosition());
        int flagNumber = (int) boardView.getCells()
                .stream()
                .filter(v -> v.getGameSymbol() == GameSymbol.FLAG)
                .count();
        menuView.getFlagCounterLabel().setText(String.valueOf(gameConfiguration.minesNumber() - flagNumber));
    }

    public void onRightClick(Cell cell) {
        int actualFlagNumber = Integer.parseInt(menuView.getFlagCounterLabel().getText());
        switch (cell.getGameSymbol()) {
            case COVERED -> {
                menuView.getFlagCounterLabel().setText(String.valueOf(actualFlagNumber - 1));
                cell.setGameSymbol(GameSymbol.FLAG);
            }
            case FLAG -> {
                menuView.getFlagCounterLabel().setText(String.valueOf(actualFlagNumber + 1));
                cell.setGameSymbol(GameSymbol.COVERED);
            }
        }
    }

    public void onSelectedComboBox() {
        newGame();
    }

    public void onNewGameClick() {
        newGame();
    }

    private void initialiseMenuView() {
        JPanel menuPanel = new JPanel();
        mainFrame.add(menuPanel, BorderLayout.NORTH);
        menuView = new MenuView(this, menuPanel);
        menuView.createAndShowGUI();
    }

    private void initialiseBoardView() {
        gameConfiguration = GameConfiguration.fromDifficulty((GameConfiguration.Difficulty) Objects
                .requireNonNull(menuView.getDifficultyComboBox().getSelectedItem()));
        menuView.getFlagCounterLabel().setText(String.valueOf(gameConfiguration.minesNumber()));
        model = new GameManager(gameConfiguration, minesPlacer);
        model.addListener(menuView, EventType.VICTORY, EventType.DEFEAT);
        JPanel boardPanel = new JPanel();
        mainFrame.add(boardPanel, BorderLayout.CENTER);
        boardView = new BoardView(this, model, boardPanel, gameConfiguration.dimension());
        model.addListener(boardView, EventType.values());
        boardView.createAndShowGUI();
        firstMove = true;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.pack();
        int width = mainFrame.getWidth();
        int height = mainFrame.getHeight();
        mainFrame.setLocation((screenSize.width / 2) - (width / 2), (screenSize.height / 2) - (height / 2));
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    private void newGame() {
        mainFrame.remove(((BorderLayout) mainFrame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER));
        if (menuView.getGameOutcomeDialog() != null) {
            menuView.getGameOutcomeDialog().dispose();
        }
        menuView.getStopwatchLabel().reset();
        initialiseBoardView();
    }

}
