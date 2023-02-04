package it.units.sdm.jminesweeper.core.generation;

@FunctionalInterface
public interface MinesPlacer<B, C> {

    void place(B board, int minesNumber, C firstClick);

}
