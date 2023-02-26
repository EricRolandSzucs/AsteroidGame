// SZUCS ERIC-ROLAND, 524, seim1964

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Asteroid {

    private final int sizeH = 240;
    private final int sizeW = 134;
    private final int drawingWidth;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private BufferedImage img;

    public Asteroid(int drawingWidth) throws IOException {
        imageLoader();
        this.drawingWidth = drawingWidth;
        this.coordinates();
    }

    private void imageLoader() throws IOException {
        img = ImageIO.read(new File("./Pictures/asteroids.png"));
    }

    public void move() {
        x -= dx;
        y -= dy;
        if (x < 1 - sizeW || y < 1 - sizeH) {
            this.coordinates();
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    public int getSizeW() {
        return sizeW;
    }

    public int getSizeH() {
        return sizeH;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void coordinates() {
        double min = 0.2;
        double max = 0.6;
        x = drawingWidth + 240;
        y = Math.random() * drawingWidth;
        dx = Math.random() * 4;
        dy = Math.random() * (max - min) + min;
    }

}
