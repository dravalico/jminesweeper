package it.units.sdm.jminesweeper.event;

import java.util.EventListener;

@FunctionalInterface
public interface GameEventListener extends EventListener {

    void onGameEvent(GameEvent event);

}
