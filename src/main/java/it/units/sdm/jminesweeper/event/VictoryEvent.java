package it.units.sdm.jminesweeper.event;

public class VictoryEvent extends GameEvent {

    public VictoryEvent(Object source) {
        super(source, EventType.VICTORY);
    }

}
