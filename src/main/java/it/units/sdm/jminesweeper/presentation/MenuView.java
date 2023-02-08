package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.event.GameEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class MenuView implements View {
    private final Controller controller;
    private final JPanel panel;
    private JComboBox<GameConfiguration.Difficulty> difficultyComboBox;
    private StopwatchLabel stopwatchLabel;
    private JLabel flagCounterLabel;
    private JButton newGameButton;
    private JDialog gameOutcomeDialog;
    private final SoundsPlayer soundsPlayer;

    public MenuView(Controller controller, JPanel menuPanel) {
        this.controller = controller;
        this.panel = menuPanel;
        soundsPlayer = SoundsPlayer.getInstance();
        UIManager.put("OptionPane.background", Color.decode(GameStyle.MENU_BACKGROUND_COLOR.getValue()));
        UIManager.put("Panel.background", Color.decode(GameStyle.MENU_BACKGROUND_COLOR.getValue()));
    }

    @Override
    public void createAndShowGUI() {
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode(GameStyle.MENU_BACKGROUND_COLOR.getValue()));
        difficultyComboBox = new JComboBox<>(GameConfiguration.Difficulty.values());
        difficultyComboBox.setSelectedIndex(0);
        difficultyComboBox.addItemListener(e -> controller.onSelectedComboBox());
        stopwatchLabel = new StopwatchLabel();
        stopwatchLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        flagCounterLabel = new JLabel();
        System.out.println(flagCounterLabel.getFont());
        flagCounterLabel.setHorizontalAlignment(SwingConstants.LEFT);
        newGameButton = new JButton("New game");
        newGameButton.setBorder(BorderFactory.createEmptyBorder());
        newGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                soundsPlayer.playMenuClick();
                controller.onNewGameClick();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                newGameButton.setBackground(Color.decode(GameStyle.BUTTON_HOVER_BACKGROUND_COLOR.getValue()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newGameButton.setBackground(Color.decode(GameStyle.MENU_COMPONENT_BACKGROUND_COLOR.getValue()));
            }
        });
        addConstraintsAndAddComponentToPanel();
        setComponentsStyle();
        addIconsToStopwatchAndFlagCounter();
    }

    @Override
    public void onGameEvent(GameEvent event) {
        stopwatchLabel.stop();
        String gameOutcome = "";
        JLabel elapsedTimeLabel = new JLabel("--:--");
        switch (event.getEventType()) {
            case VICTORY -> {
                gameOutcome = "You won!";
                elapsedTimeLabel.setText(stopwatchLabel.getText());
            }
            case DEFEAT -> gameOutcome = "You lost!";
        }
        showGameOutcomeWindow(elapsedTimeLabel, gameOutcome);
    }

    public JComboBox<GameConfiguration.Difficulty> getDifficultyComboBox() {
        return difficultyComboBox;
    }

    public StopwatchLabel getStopwatchLabel() {
        return stopwatchLabel;
    }

    public JLabel getFlagCounterLabel() {
        return flagCounterLabel;
    }

    public JDialog getGameOutcomeDialog() {
        return gameOutcomeDialog;
    }

    private void addConstraintsAndAddComponentToPanel() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15, 0, 15, 0);
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(difficultyComboBox, constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(15, 0, 15, 10);
        panel.add(stopwatchLabel, constraints);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(15, 10, 15, 0);
        panel.add(flagCounterLabel, constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(15, 0, 15, 0);
        panel.add(newGameButton, constraints);
    }

    private void setComponentsStyle() {
        newGameButton.setPreferredSize(new Dimension(115, 32));
        difficultyComboBox.setPreferredSize(new Dimension(150, 32));
        Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof JButton || c instanceof JComboBox<?>)
                .forEach(c -> {
                    c.setFont(new Font(GameStyle.FONT.getValue(), Font.PLAIN, 16));
                    c.setBackground(Color.decode(GameStyle.MENU_COMPONENT_BACKGROUND_COLOR.getValue()));
                    c.setFocusable(false);
                });
        Arrays.stream(panel.getComponents())
                .filter(JLabel.class::isInstance)
                .forEach(c -> {
                    c.setForeground(Color.decode(GameStyle.MENU_LABEL_FOREGROUND_COLOR.getValue()));
                    c.setPreferredSize(new Dimension(90, 25));
                    c.setFont(new Font(GameStyle.FONT.getValue(), Font.PLAIN, 20));
                });
    }

    private void addIconsToStopwatchAndFlagCounter() {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("icons" + File.separatorChar + "flag.png")));
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(52 / 2, 52 / 2, Image.SCALE_SMOOTH));
            flagCounterLabel.setIcon(imageIcon);
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("icons" + File.separatorChar + "stopwatch.png")));
            imageIcon = new ImageIcon(image.getScaledInstance(52 / 2, 52 / 2, Image.SCALE_SMOOTH));
            stopwatchLabel.setIcon(imageIcon);
        } catch (Exception e) {
            System.out.println("Menu icons not loaded");
        }
    }

    private void showGameOutcomeWindow(JLabel elapsedTimeLabel, String gameOutcome) {
        elapsedTimeLabel.setFont(new Font(GameStyle.FONT.getValue(), Font.BOLD, 20));
        elapsedTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        elapsedTimeLabel.setForeground(Color.decode(GameStyle.MENU_LABEL_FOREGROUND_COLOR.getValue()));
        JOptionPane jOptionPane = new JOptionPane(elapsedTimeLabel, JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        jOptionPane.setOpaque(true);
        jOptionPane.setBackground(Color.decode(GameStyle.MENU_BACKGROUND_COLOR.getValue()));
        gameOutcomeDialog = jOptionPane.createDialog(gameOutcome);
        gameOutcomeDialog.setModal(false);
        gameOutcomeDialog.setVisible(true);
    }

}
