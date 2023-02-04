package it.units.sdm.jminesweeper.presentation;

import it.units.sdm.jminesweeper.GameConfiguration;
import it.units.sdm.jminesweeper.event.GameEvent;
import it.units.sdm.jminesweeper.event.GameEventListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MenuView implements GameEventListener {
    private final Controller controller;
    private final JPanel menuPanel;
    private JComboBox<GameConfiguration.Difficulty> difficultyComboBox;
    private StopwatchLabel stopwatchLabel;
    private JLabel flagCounterLabel;
    private JDialog gameOutcomeDialog;
    private static final String COLOR = "#547336";
    private static final String FONT = "Autumn";

    public MenuView(Controller controller, JPanel menuPanel) {
        this.controller = controller;
        this.menuPanel = menuPanel;
    }

    public void initMenu() {
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.decode(COLOR));

        difficultyComboBox = new JComboBox<>(GameConfiguration.Difficulty.values());
        difficultyComboBox.setSelectedIndex(0);
        difficultyComboBox.addItemListener(e -> controller.onSelectedComboBox());

        stopwatchLabel = new StopwatchLabel();
        stopwatchLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        flagCounterLabel = new JLabel();
        flagCounterLabel.setHorizontalAlignment(SwingConstants.LEFT);

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
        constraints.insets = new Insets(15, 0, 15, 10);
        menuPanel.add(stopwatchLabel, constraints);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(15, 10, 15, 0);
        menuPanel.add(flagCounterLabel, constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(15, 0, 15, 0);
        menuPanel.add(newGameButton, constraints);

        setStyle();

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("icons" + File.separatorChar + "flag.png")));
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(52 / 2, 52 / 2, Image.SCALE_SMOOTH));
            flagCounterLabel.setIcon(imageIcon);
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("icons" + File.separatorChar + "stopwatch.png")));
            imageIcon = new ImageIcon(image.getScaledInstance(52 / 2, 52 / 2, Image.SCALE_SMOOTH));
            stopwatchLabel.setIcon(imageIcon);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
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
            if (component instanceof JLabel) {
                component.setForeground(Color.WHITE);
                component.setPreferredSize(new Dimension(90, 25));
                component.setFont(new Font(FONT, Font.PLAIN, 20));
            }
        }
    }

    private void showGameOutcomeWindow(JLabel elapsedTimeLabel, String gameOutcome) {
        UIManager.put("OptionPane.background", Color.decode(COLOR));
        UIManager.put("Panel.background", Color.decode(COLOR));
        elapsedTimeLabel.setFont(new Font(FONT, Font.BOLD, 20));
        elapsedTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        elapsedTimeLabel.setForeground(Color.WHITE);
        JOptionPane jOptionPane = new JOptionPane(elapsedTimeLabel, JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        jOptionPane.setOpaque(true);
        jOptionPane.setBackground(Color.decode(COLOR));
        gameOutcomeDialog = jOptionPane.createDialog(gameOutcome);
        gameOutcomeDialog.setModal(false);
        gameOutcomeDialog.setVisible(true);
    }

}
