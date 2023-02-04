package it.units.sdm.jminesweeper.presentation;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StopwatchLabel extends JLabel {
    private final Timer timer;
    private int seconds;

    public StopwatchLabel() {
        resetText();
        int millisDelay = 1000;
        seconds = 0;
        ActionListener task = e -> {
            seconds = seconds + 1;
            long elapsedMilliseconds = seconds * 1000L;
            DateFormat formatter = new SimpleDateFormat("mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(elapsedMilliseconds);
            setText(formatter.format(calendar.getTime()));
        };
        timer = new Timer(millisDelay, task);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void reset() {
        timer.stop();
        resetText();
        seconds = 0;
    }

    private void resetText() {
        setText("00:00");
    }

}
