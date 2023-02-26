// SZUCS ERIC-ROLAND, 524, seim1964

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio implements Runnable {

    private final String soundName;
    private final int loopNumber;

    public Audio(String soundName, int loop) {
        this.soundName = soundName;
        loopNumber = loop;
    }

    @Override
    public void run() {
        Clip sound = null;
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            sound = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            assert sound != null;
            sound.open(audioInputStream);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        if (loopNumber == 1)
            sound.start();
        else
            sound.loop(loopNumber);
    }
}
