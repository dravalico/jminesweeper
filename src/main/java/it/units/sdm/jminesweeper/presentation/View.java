package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.event.GameEventListener;

public interface View extends GameEventListener {

    void createAndShowGUI();

}
