package it.units.sdm.jminesweeper.event;

public class DefeatEvent extends GameEvent {

    public DefeatEvent(Object source) {
        super(source, EventType.DEFEAT);
    }

}
