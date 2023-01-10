package it.units.sdm.jminesweeper.core;

public abstract class AbstractBoard<T> {
    protected T board;

    public AbstractBoard(T board) {
        this.board = board;
    }

}
