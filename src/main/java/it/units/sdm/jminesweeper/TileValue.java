package it.units.sdm.jminesweeper;

import java.util.Objects;

public class TileValue {
    private GameSymbol value;

    public TileValue(GameSymbol value) {
        this.value = value;
    }

    public boolean isAMine() {
        return value.equals(GameSymbol.MINE);
    }

    public GameSymbol getValue() {
        return value;
    }

    public void setValue(GameSymbol value) {
        this.value = value;
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
