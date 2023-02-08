package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameSymbol;
import it.units.sdm.jminesweeper.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private final Font font;

    static {
        GAME_NUMBERS_STYLE = new EnumMap<>(GameSymbol.class);
        Arrays.stream(GameSymbol.values())
                .filter(s -> s != GameSymbol.COVERED && s != GameSymbol.MINE &&
                        s != GameSymbol.EMPTY && s != GameSymbol.FLAG)
                .forEach(s -> {
                    switch (s) {
                        case ONE -> GAME_NUMBERS_STYLE.put(s, new Pair<>("1", new Color(41, 103, 136)));
                        case TWO -> GAME_NUMBERS_STYLE.put(s, new Pair<>("2", new Color(109, 185, 74)));
                        case THREE -> GAME_NUMBERS_STYLE.put(s, new Pair<>("3", new Color(235, 63, 37)));
                        case FOUR -> GAME_NUMBERS_STYLE.put(s, new Pair<>("4", new Color(127, 29, 134)));
                        case FIVE -> GAME_NUMBERS_STYLE.put(s, new Pair<>("5", new Color(212, 142, 30)));
                        case SIX -> GAME_NUMBERS_STYLE.put(s, new Pair<>("6", new Color(74, 49, 77)));
                        case SEVEN -> GAME_NUMBERS_STYLE.put(s, new Pair<>("7", new Color(123, 249, 250)));
                        case EIGHT -> GAME_NUMBERS_STYLE.put(s, new Pair<>("8", new Color(191, 251, 91)));
                    }
                });
        try {
            mineImage = ImageIO.read(Objects.requireNonNull(Cell.class.getClassLoader()
                    .getResource(BASE_FILEPATH_FOR_ICONS + File.separatorChar + "mine.png")));
            flagImage = ImageIO.read(Objects.requireNonNull(Cell.class.getClassLoader()
                    .getResource(BASE_FILEPATH_FOR_ICONS + File.separatorChar + "flag.png")));
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
        font = new Font("Autumn", Font.BOLD, (int) (sideDimension / 2.15));
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
                setBackground(Color.decode("#ACCE5E"));
            } else {
                setBackground(Color.decode("#B4D565"));
            }
        } else {
            if (isLight) {
                setBackground(Color.decode("#D2B99D"));
            } else {
                setBackground(Color.decode("#E0C3A3"));
            }
        }
    }

    public void victoryStyle() {
        if (gameSymbol == GameSymbol.COVERED || gameSymbol == GameSymbol.FLAG) {
            StringBuilder filename = new StringBuilder(BASE_FILEPATH_FOR_ICONS + File.separatorChar + "flower_");
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
            setBackground(Color.decode("#69BFF7"));
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
        setFont(font);
    }

    private void reset() {
        setIcon(null);
        setText("");
    }

}
