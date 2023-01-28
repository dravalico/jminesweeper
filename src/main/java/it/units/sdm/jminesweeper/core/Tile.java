package it.units.sdm.jminesweeper.core;

import it.units.sdm.jminesweeper.GameSymbol;

import java.util.Objects;

public class Tile {
    private GameSymbol value;
    private boolean isCovered;

    public Tile(GameSymbol value) {
        this.value = value;
        isCovered = true;
    }

    public boolean isMine() {
        return value == GameSymbol.MINE;
    }

    public boolean isNumber() {
        return !(value == GameSymbol.MINE || value == GameSymbol.EMPTY);
    }

    public GameSymbol getValue() {
        return value;
    }

    public void setValue(GameSymbol value) {
        this.value = value;
    }

    public void uncover() {
        isCovered = false;
    }

    public boolean isCovered() {
        return isCovered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tile tile = (Tile) o;
        return value == tile.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
