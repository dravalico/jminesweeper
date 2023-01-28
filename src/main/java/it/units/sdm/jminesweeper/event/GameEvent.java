package it.units.sdm.jminesweeper.event;

import java.util.EventObject;

public class GameEvent extends EventObject {
    private final EventType eventType;

    GameEvent(Object source, EventType eventType) {
        super(source);
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return this.eventType;
    }

}
