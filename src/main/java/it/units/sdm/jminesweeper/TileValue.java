package it.units.sdm.jminesweeper;

import java.util.Objects;

public class TileValue {
    private GameSymbol value;
    private boolean isCovered;

    public TileValue(GameSymbol value) {
        this.value = value;
        isCovered = true;
    }

    public boolean isAMine() {
        return value.equals(GameSymbol.MINE);
    }

    public boolean isValueANumber() {
        return !(value.equals(GameSymbol.MINE) || value.equals(GameSymbol.EMPTY) || value.equals(GameSymbol.COVERED));
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
        TileValue tileValue = (TileValue) o;
        return value == tileValue.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
