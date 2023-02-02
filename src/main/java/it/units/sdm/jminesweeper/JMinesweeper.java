package it.units.sdm.jminesweeper;

import it.units.sdm.jminesweeper.core.GameManager;
import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.presentation.BoardView;

import javax.swing.*;

public class JMinesweeper {

    public static void main(String[] args) {
        GameConfiguration gameConfiguration = GameConfiguration.fromDifficulty(GameConfiguration.Difficulty.BEGINNER);
        GameManager model = new GameManager(gameConfiguration, new GuassianMinesPlacer());
        SwingUtilities.invokeLater(new BoardView(model, gameConfiguration.dimension())::initBoard);
    }

}
