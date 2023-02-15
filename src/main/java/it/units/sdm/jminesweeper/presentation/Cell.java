package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Cell extends JButton {
    private final Point position;
    private final int sideDimension;
    private GameSymbol gameSymbol;
    private static final Map<GameSymbol, Pair<String, Color>> GAME_NUMBERS_STYLE;
    private static final String BASE_FILEPATH_FOR_ICONS = "icons";
    private static BufferedImage mineImage;
    private static BufferedImage flagImage;
    private final Font fontForNumbers;

    static {
        GAME_NUMBERS_STYLE = new EnumMap<>(GameSymbol.class);
        Arrays.stream(GameSymbol.values())
                .filter(s -> s != GameSymbol.COVERED && s != GameSymbol.MINE &&
                        s != GameSymbol.EMPTY && s != GameSymbol.FLAG)
                .forEach(s -> {
                    switch (s) {
                        case ONE -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("1", Color.decode(GameStyle.SYMBOL_ONE_COLOR.getValue())));
                        case TWO -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("2", Color.decode(GameStyle.SYMBOL_TWO_COLOR.getValue())));
                        case THREE -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("3", Color.decode(GameStyle.SYMBOL_THREE_COLOR.getValue())));
                        case FOUR -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("4", Color.decode(GameStyle.SYMBOL_FOUR_COLOR.getValue())));
                        case FIVE -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("5", Color.decode(GameStyle.SYMBOL_FIVE_COLOR.getValue())));
                        case SIX -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("6", Color.decode(GameStyle.SYMBOL_SIX_COLOR.getValue())));
                        case SEVEN -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("7", Color.decode(GameStyle.SYMBOL_SEVEN_COLOR.getValue())));
                        case EIGHT -> GAME_NUMBERS_STYLE.put(s,
                                new Pair<>("8", Color.decode(GameStyle.SYMBOL_EIGHT_COLOR.getValue())));
                    }
                });
        try {
            mineImage = ImageIO.read(Objects.requireNonNull(Cell.class.getClassLoader()
                    .getResource(BASE_FILEPATH_FOR_ICONS + "/mine.png")));
            flagImage = ImageIO.read(Objects.requireNonNull(Cell.class.getClassLoader()
                    .getResource(BASE_FILEPATH_FOR_ICONS + "/flag.png")));
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            System.err.println("Images not found");
            System.exit(1);
        }
    }

    public Cell(int i, int j, int sideDimension, GameSymbol gameSymbol) {
        position = new Point(i, j);
        setPreferredSize(new Dimension(sideDimension, sideDimension));
        this.sideDimension = sideDimension;
        setBorder(BorderFactory.createEmptyBorder());
        setGameSymbol(gameSymbol);
        setFocusable(false);
        fontForNumbers = new Font(GameStyle.FONT.getValue(), Font.BOLD, (int) (sideDimension / 2.15));
    }

    public GameSymbol getGameSymbol() {
        return gameSymbol;
    }

    public void setGameSymbol(GameSymbol gameSymbol) {
        this.gameSymbol = gameSymbol;
        setSymbolImage();
    }

    public Point getPosition() {
        return position;
    }

    public void setProperBackground() {
        boolean isLight = (position.x + position.y) % 2 == 0;
        if (gameSymbol == GameSymbol.COVERED || gameSymbol == GameSymbol.FLAG) {
            if (isLight) {
                setBackground(Color.decode(GameStyle.COVERED_CELL_LIGHT_BACKGROUND_COLOR.getValue()));
            } else {
                setBackground(Color.decode(GameStyle.COVERED_CELL_DARK_BACKGROUND_COLOR.getValue()));
            }
        } else {
            if (isLight) {
                setBackground(Color.decode(GameStyle.UNCOVERED_CELL_LIGHT_BACKGROUND_COLOR.getValue()));
            } else {
                setBackground(Color.decode(GameStyle.UNCOVERED_CELL_DARK_BACKGROUND_COLOR.getValue()));
            }
        }
    }

    public void victoryStyle() {
        if (gameSymbol == GameSymbol.COVERED || gameSymbol == GameSymbol.FLAG) {
            StringBuilder filename = new StringBuilder(BASE_FILEPATH_FOR_ICONS + "/flower_");
            int whatFlower = new Random().nextInt(3);
            filename.append(whatFlower).append(".png");
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource(String.valueOf(filename))));
                ImageIcon imageIcon = new ImageIcon(image
                        .getScaledInstance(sideDimension / 2, sideDimension / 2, Image.SCALE_SMOOTH));
                setIcon(imageIcon);
            } catch (IOException | NullPointerException e) {
                System.err.println("Cannot load victory icons");
                reset();
            }
        } else {
            reset();
            setBackground(Color.decode(GameStyle.VICTORY_FREE_CELL_BACKGROUND.getValue()));
        }
    }

    private void setSymbolImage() {
        setProperBackground();
        if (gameSymbol == GameSymbol.COVERED || gameSymbol == GameSymbol.EMPTY) {
            reset();
            return;
        }
        if (gameSymbol == GameSymbol.MINE || gameSymbol == GameSymbol.FLAG) {
            BufferedImage image = gameSymbol == GameSymbol.MINE ? mineImage : flagImage;
            ImageIcon imageIcon = new ImageIcon(
                    image.getScaledInstance(sideDimension / 2, sideDimension / 2, Image.SCALE_SMOOTH));
            setIcon(imageIcon);
            return;
        }
        setIcon(null);
        setText(GAME_NUMBERS_STYLE.get(gameSymbol).first());
        setForeground(GAME_NUMBERS_STYLE.get(gameSymbol).second());
        setFont(fontForNumbers);
    }

    private void reset() {
        setIcon(null);
        setText("");
    }

}
