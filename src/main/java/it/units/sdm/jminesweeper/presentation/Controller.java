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

    public Controller(MinesPlacer<Map<Point, Tile>, Point> minesPlacer) {
        this.minesPlacer = minesPlacer;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        gameConfiguration = GameConfiguration.fromDifficulty(GameConfiguration.Difficulty.BEGINNER);
        model = new GameManager(gameConfiguration, minesPlacer);
    }

    public void startGame() {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Minesweeper");
            createMenuView();
            createBoardView();
        });
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

    public void onSelectedComboBox() {
        newGame();
    }

    public void onNewGameClick() {
        newGame();
    }

    private void createMenuView() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel menuPanel = new JPanel(new GridBagLayout());
        mainFrame.add(menuPanel, BorderLayout.NORTH);
        menuView = new MenuView(this, menuPanel);
        menuView.initMenu();
    }

    private void createBoardView() {
        gameConfiguration = GameConfiguration.fromDifficulty((GameConfiguration.Difficulty) Objects
                .requireNonNull(menuView.getDifficultyComboBox().getSelectedItem()));
        model = new GameManager(gameConfiguration, minesPlacer);
        model.addListener(menuView, EventType.VICTORY, EventType.DEFEAT);
        JPanel boardPanel = new JPanel(new GridLayout());
        mainFrame.add(boardPanel, BorderLayout.CENTER);
        boardView = new BoardView(this, model, boardPanel, gameConfiguration.dimension());
        model.addListener(boardView, EventType.values());
        boardView.initBoard();
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
        createBoardView();
    }

}
