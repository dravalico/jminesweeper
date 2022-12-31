package it.units.sdm.jminesweeper;

import java.util.Objects;

public class Tile {
    private GameSymbol value;
    private boolean isCovered;

    public Tile(GameSymbol value) {
        this.value = value;
        isCovered = true;
    }

    public boolean isAMine() {
        return value.equals(GameSymbol.MINE);
    }

    public boolean isANumber() {
        return !(value.equals(GameSymbol.MINE) || value.equals(GameSymbol.EMPTY));
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
