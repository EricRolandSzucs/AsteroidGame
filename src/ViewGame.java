// SZUCS ERIC-ROLAND, 524, seim1964

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ViewGame extends JPanel {

    private final Asteroid[] asteroidArray;
    private final JLabel time = new JLabel("");
    private BufferedImage img;

    private long startTime;
    private long elapsedMinutes;
    private long secondsDisplay;
    private long decisecondsDisplay;

    public ViewGame(Asteroid[] gameObjectsArray, int width, int height, long startTime) throws IOException {
        imageLoader();
        this.asteroidArray = gameObjectsArray;
        setPreferredSize(new Dimension(width, height));
        timeSettings(startTime);

    }

    private void imageLoader() throws IOException {
        img = ImageIO.read(new File("./Pictures/space3.jpg"));
    }

    private void timeSettings(long startTime) {
        add(time);
        time.setForeground(Color.WHITE);
        time.setFont(time.getFont().deriveFont(40.0f));
        this.startTime = startTime;
    }

    public String score() {
        return elapsedMinutes + "m " + secondsDisplay + "s " + decisecondsDisplay + "ds";
    }

    private void timeDisplay() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedDeciSeconds = elapsedTime / 100;
        decisecondsDisplay = elapsedDeciSeconds % 10;
        long elapsedSeconds = elapsedDeciSeconds / 10;
        secondsDisplay = elapsedSeconds % 60;
        elapsedMinutes = elapsedSeconds / 60;
        time.setText("<html><center>Goal: 20<br>" + elapsedMinutes + "m " + secondsDisplay + "s " + decisecondsDisplay + "ds</center></html>");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (startTime != 0) {
            timeDisplay();
        }
        g.drawImage(img, 0, 0, null);
        for (Asteroid asteroid : asteroidArray) {
            draw(g, asteroid);
        }

    }

    private void draw(Graphics g, Asteroid a) {
        g.setColor(Color.GRAY);
        g.drawImage(a.getImg(), (int) a.getX(), (int) a.getY(), a.getSizeH(), a.getSizeW(), null);
    }

}