package it.units.sdm.jminesweeper.core;

public abstract class AbstractBoard<T> {
    protected final T board;

    protected AbstractBoard(T board) {
        this.board = board;
    }

}
