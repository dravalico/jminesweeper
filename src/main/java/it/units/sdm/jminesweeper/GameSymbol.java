package it.units.sdm.jminesweeper;

public enum GameSymbol {
    COVERED,
    EMPTY,
    MINE,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT;

    public static GameSymbol fromInt(int value) throws IllegalStateException {
        return switch (value) {
            case 1 -> GameSymbol.ONE;
            case 2 -> GameSymbol.TWO;
            case 3 -> GameSymbol.THREE;
            case 4 -> GameSymbol.FOUR;
            case 5 -> GameSymbol.FIVE;
            case 6 -> GameSymbol.SIX;
            case 7 -> GameSymbol.SEVEN;
            case 8 -> GameSymbol.EIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

}
