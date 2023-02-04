package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.event.GameEvent;
import it.units.sdm.jminesweeper.event.GameEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuView implements GameEventListener {
    private final Controller controller;
    private final JPanel menuPanel;
    private JComboBox<GameConfiguration.Difficulty> difficultyComboBox;
    private static final String COLOR = "#547336";
    private static final String FONT = "Autumn";

    public MenuView(Controller controller, JPanel menuPanel) {
        this.controller = controller;
        this.menuPanel = menuPanel;
    }

    public void initMenu() {
        menuPanel.setBackground(Color.decode(COLOR));

        difficultyComboBox = new JComboBox<>(GameConfiguration.Difficulty.values());
        difficultyComboBox.setSelectedIndex(0);
        difficultyComboBox.addItemListener(e -> controller.onSelectedComboBox());

        JButton newGameButton = new JButton("New game");
        newGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                controller.onNewGameClick();
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(15, 0, 15, 0);
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        menuPanel.add(difficultyComboBox, constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(15, 0, 15, 0);
        menuPanel.add(newGameButton, constraints);

        setStyle();
    }

    @Override
    public void onGameEvent(GameEvent event) {
    }

    public JComboBox<GameConfiguration.Difficulty> getDifficultyComboBox() {
        return difficultyComboBox;
    }

    private void setStyle() {
        for (Component component : menuPanel.getComponents()) {
            if (component instanceof JButton || component instanceof JComboBox<?>) {
                component.setBackground(Color.WHITE);
                if (component instanceof JButton) {
                    component.setPreferredSize(new Dimension(115, 32));
                } else {
                    component.setPreferredSize(new Dimension(150, 32));
                }
                component.setFont(new Font(FONT, Font.PLAIN, 16));
            }
        }
    }

}
