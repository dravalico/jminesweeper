package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameSymbol;

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
    private static final Map<GameSymbol, BufferedImage> IMAGE_MAP;

    static {
        IMAGE_MAP = new EnumMap<>(GameSymbol.class);
        Arrays.stream(GameSymbol.values())
                .forEach(s -> {
                    StringBuilder filename = new StringBuilder("icons" + File.separatorChar);
                    if (s == GameSymbol.COVERED || s == GameSymbol.EMPTY) {
                        return;
                    }
                    switch (s) {
                        case MINE -> filename.append("mine.png");
                        case FLAG -> filename.append("flag.png");
                        case ONE -> filename.append("one.png");
                        case TWO -> filename.append("two.png");
                        case THREE -> filename.append("three.png");
                        case FOUR -> filename.append("four.png");
                        case FIVE -> filename.append("five.png");
                        case SIX -> filename.append("six.png");
                        case SEVEN -> filename.append("seven.png");
                        case EIGHT -> filename.append("eight.png");
                        default -> throw new IllegalStateException("Unexpected value");
                    }
                    try {
                        IMAGE_MAP.put(s, ImageIO.read(Objects.requireNonNull(Cell.class.getClassLoader()
                                .getResource(String.valueOf(filename)))));
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        System.exit(1);
                    }
                });
    }

    public Cell(int i, int j, int sideDimension, GameSymbol gameSymbol) {
        position = new Point(i, j);
        setPreferredSize(new Dimension(sideDimension, sideDimension));
        this.sideDimension = sideDimension;
        setBorder(BorderFactory.createEmptyBorder());
        setGameSymbol(gameSymbol);
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
            StringBuilder filename = new StringBuilder("icons" + File.separatorChar + "flower_");
            int whatFlower = new Random().nextInt(3);
            filename.append(whatFlower).append(".png");
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource(String.valueOf(filename))));
                ImageIcon imageIcon = new ImageIcon(image
                        .getScaledInstance(sideDimension / 2, sideDimension / 2, Image.SCALE_SMOOTH));
                setIcon(imageIcon);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else {
            setIcon(null);
            setBackground(Color.decode("#69BFF7"));
        }
    }

    private void setSymbolImage() {
        setProperBackground();
        if (gameSymbol == GameSymbol.COVERED || gameSymbol == GameSymbol.EMPTY) {
            setIcon(null);
            return;
        }
        ImageIcon imageIcon = new ImageIcon(IMAGE_MAP.get(gameSymbol)
                .getScaledInstance(sideDimension / 2, sideDimension / 2, Image.SCALE_SMOOTH));
        setIcon(imageIcon);
    }

}
