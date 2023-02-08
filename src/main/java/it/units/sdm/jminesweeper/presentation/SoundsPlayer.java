package it.units.sdm.jminesweeper.presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.*;

public class SoundsPlayer {
    private static final String BASE_FILEPATH = "sounds" + File.separatorChar;
    private static final String CLICK_SOUND1 = "click1.wav";
    private static final String CLICK_SOUND2 = "click2.wav";
    private static final String CLICK_SOUND3 = "click3.wav";
    private static final String PUT_FLAG_SOUND = "put_flag.wav";
    private static final String REMOVE_FLAG_SOUND = "remove_flag.wav";
    private static final String VICTORY_SOUND = "victory.wav";
    private static final String DEFEAT_SOUND = "defeat.wav";
    private static final String MENU_CLICK_SOUND = "menu_click.wav";
    private static final List<String> SOUNDS = List.of(CLICK_SOUND1, CLICK_SOUND2, CLICK_SOUND3, PUT_FLAG_SOUND,
            REMOVE_FLAG_SOUND, VICTORY_SOUND, DEFEAT_SOUND, MENU_CLICK_SOUND);
    private static final List<String> RANDOM_CLICK_SOUNDS = new ArrayList<>(List.of(CLICK_SOUND1, CLICK_SOUND2, CLICK_SOUND3));
    private static final Map<String, Clip> SOUNDS_MAP = new HashMap<>();
    private static final SoundsPlayer INSTANCE = new SoundsPlayer();

    private SoundsPlayer() {
        try {
            for (String sound : SOUNDS) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(Objects.requireNonNull(
                        SoundsPlayer.class.getClassLoader().getResource(BASE_FILEPATH + sound)).toURI()));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                SOUNDS_MAP.put(sound, clip);
            }
        } catch (Exception e) {
            System.err.println("Sound not found");
        }
    }

    public static SoundsPlayer getInstance() {
        return INSTANCE;
    }

    public void playClick() {
        Collections.shuffle(RANDOM_CLICK_SOUNDS);
        String clickSound = RANDOM_CLICK_SOUNDS.get(0);
        if (SOUNDS_MAP.get(clickSound) != null) {
            SOUNDS_MAP.get(clickSound).setFramePosition(0);
            new Thread(() -> SOUNDS_MAP.get(clickSound).start()).start();
        }
    }

    public void playPutFlag() {
        if (SOUNDS_MAP.get(PUT_FLAG_SOUND) != null) {
            SOUNDS_MAP.get(PUT_FLAG_SOUND).setFramePosition(0);
            new Thread(() -> SOUNDS_MAP.get(PUT_FLAG_SOUND).start()).start();
        }
    }

    public void playRemoveFlag() {
        if (SOUNDS_MAP.get(REMOVE_FLAG_SOUND) != null) {
            SOUNDS_MAP.get(REMOVE_FLAG_SOUND).setFramePosition(0);
            new Thread(() -> SOUNDS_MAP.get(REMOVE_FLAG_SOUND).start()).start();
        }
    }

    public void playVictory() {
        SOUNDS_MAP.get(VICTORY_SOUND).setFramePosition(0);
        new Thread(() -> SOUNDS_MAP.get(VICTORY_SOUND).start()).start();
    }

    public void playDefeat() {
        if (SOUNDS_MAP.get(DEFEAT_SOUND) != null) {
            SOUNDS_MAP.get(DEFEAT_SOUND).setFramePosition(0);
            new Thread(() -> SOUNDS_MAP.get(DEFEAT_SOUND).start()).start();
        }
    }

    public void playMenuClick() {
        if (SOUNDS_MAP.get(MENU_CLICK_SOUND) != null) {
            SOUNDS_MAP.get(MENU_CLICK_SOUND).setFramePosition(0);
            new Thread(() -> SOUNDS_MAP.get(MENU_CLICK_SOUND).start()).start();
        }
    }

}
