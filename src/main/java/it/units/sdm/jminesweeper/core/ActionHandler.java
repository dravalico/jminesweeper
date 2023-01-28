package it.units.sdm.jminesweeper.core;

import it.units.sdm.jminesweeper.event.GameEventSource;

public interface ActionHandler<A> extends GameEventSource {

    void actionAt(A action);

}
