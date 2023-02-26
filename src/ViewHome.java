// SZUCS ERIC-ROLAND, 524, seim1964

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ViewHome extends JPanel {

    private BufferedImage img;

    public ViewHome(int width, int height) throws IOException {
        imageLoader();
        setPreferredSize(new Dimension(width, height));
    }

    private void imageLoader() throws IOException {
        img = ImageIO.read(new File("./Pictures/space3.jpg"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
}

