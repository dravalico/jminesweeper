package it.units.sdm.jminesweeper;

import it.units.sdm.jminesweeper.core.generation.GuassianMinesPlacer;
import it.units.sdm.jminesweeper.presentation.Controller;

public class JMinesweeper {

    public static void main(String[] args) {
        new Controller(new GuassianMinesPlacer()).startGame();
    }

}
