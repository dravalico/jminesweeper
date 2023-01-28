package it.units.sdm.jminesweeper.event;

public class ProgressEvent extends GameEvent {

    public ProgressEvent(Object source) {
        super(source, EventType.PROGRESS);
    }

}
