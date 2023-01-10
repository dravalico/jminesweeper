package it.units.sdm.jminesweeper.test;

import it.units.sdm.jminesweeper.core.Tile;
import it.units.sdm.jminesweeper.enumeration.GameSymbol;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class CSVParserUtil {

    public static Map<Point, Tile> csvParseTiles(String resourceName) {
        ClassLoader classLoader = CSVParserUtil.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String absolutePath = file.getAbsolutePath();
        try (Scanner scanner = new Scanner(new File(absolutePath))) {
            Map<Point, Tile> mapBoard = new LinkedHashMap<>();
            int rowCounter = 0;
            while (scanner.hasNext()) {
                String[] row = scanner.next().split(",");
                for (int i = 0; i < row.length; i++) {
                    GameSymbol gameSymbol = null;
                    switch (row[i]) {
                        case "1" -> gameSymbol = GameSymbol.ONE;
                        case "2" -> gameSymbol = GameSymbol.TWO;
                        case "3" -> gameSymbol = GameSymbol.THREE;
                        case "4" -> gameSymbol = GameSymbol.FOUR;
                        case "5" -> gameSymbol = GameSymbol.FIVE;
                        case "6" -> gameSymbol = GameSymbol.SIX;
                        case "7" -> gameSymbol = GameSymbol.SEVEN;
                        case "8" -> gameSymbol = GameSymbol.EIGHT;
                        case "*" -> gameSymbol = GameSymbol.MINE;
                        case "-" -> gameSymbol = GameSymbol.EMPTY;
                    }
                    mapBoard.put(new Point(rowCounter, i), new Tile(gameSymbol));
                }
                rowCounter = rowCounter + 1;
            }
            return mapBoard;
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    public static Map<Point, GameSymbol> csvParseGameSymbols(String resourceName) {
        ClassLoader classLoader = CSVParserUtil.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
        String absolutePath = file.getAbsolutePath();
        try (Scanner scanner = new Scanner(new File(absolutePath))) {
            Map<Point, GameSymbol> mapBoard = new LinkedHashMap<>();
            int rowCounter = 0;
            while (scanner.hasNext()) {
                String[] row = scanner.next().split(",");
                for (int i = 0; i < row.length; i++) {
                    GameSymbol gameSymbol = null;
                    switch (row[i]) {
                        case "1" -> gameSymbol = GameSymbol.ONE;
                        case "2" -> gameSymbol = GameSymbol.TWO;
                        case "3" -> gameSymbol = GameSymbol.THREE;
                        case "4" -> gameSymbol = GameSymbol.FOUR;
                        case "5" -> gameSymbol = GameSymbol.FIVE;
                        case "6" -> gameSymbol = GameSymbol.SIX;
                        case "7" -> gameSymbol = GameSymbol.SEVEN;
                        case "8" -> gameSymbol = GameSymbol.EIGHT;
                        case "*" -> gameSymbol = GameSymbol.MINE;
                        case "-" -> gameSymbol = GameSymbol.EMPTY;
                        case "o" -> gameSymbol = GameSymbol.COVERED;
                    }
                    mapBoard.put(new Point(rowCounter, i), gameSymbol);
                }
                rowCounter = rowCounter + 1;
            }
            return mapBoard;
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

}
