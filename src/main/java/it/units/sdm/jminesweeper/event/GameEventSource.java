package it.units.sdm.jminesweeper.event;

public interface GameEventSource {

    void addListeners(GameEventListener... listeners);

    void notifyListeners(GameEvent event);

}
