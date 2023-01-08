package it.units.sdm.jminesweeper.generation;

public interface MinesPlacer<B, C> {

    void place(B board, int minesNumber, C firstClick);

}
