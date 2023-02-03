package it.units.sdm.jminesweeper.presentation;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {
    private final Point position;
    private final int sideDimension;

    public Cell(int i, int j, int sideDimension) {
        position = new Point(i, j);
        setPreferredSize(new Dimension(sideDimension, sideDimension));
        this.sideDimension = sideDimension;
        setBorder(BorderFactory.createEmptyBorder());
        setProperBackground();
    }

    public Point getPosition() {
        return position;
    }

    public void setProperBackground() {
        boolean light = (position.x + position.y) % 2 == 0;
        if (light) {
            setBackground(Color.decode("#ACCE5E"));
        } else {
            setBackground(Color.decode("#B4D565"));
        }
    }

}
