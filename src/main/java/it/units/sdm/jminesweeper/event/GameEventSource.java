package it.units.sdm.jminesweeper.event;

public interface GameEventSource {

    void addListener(GameEventListener listener, EventType... eventTypes);

    void notifyListeners(GameEvent event);

}
