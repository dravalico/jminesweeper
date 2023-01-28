package it.units.sdm.jminesweeper.core;

import it.units.sdm.jminesweeper.event.GameEventSource;

public interface NewActionHandler<A> extends GameEventSource {

    void newActionAt(A action);

}
