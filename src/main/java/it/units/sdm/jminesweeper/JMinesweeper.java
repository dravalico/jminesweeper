package it.units.sdm.jminesweeper;

import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.presentation.BoardController;

public class JMinesweeper {

    public static void main(String[] args) {
        new BoardController(new GuassianMinesPlacer()).createAndShowGUI();
    }

}
