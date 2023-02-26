// SZUCS ERIC-ROLAND, 524, seim1964

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Main implements Runnable {
    private static final int DRAWING_WIDTH = 1280;
    private static final int DRAWING_HEIGHT = 720;
    private static final int ASTEROIDNUMBER = 10;

    private static Font customFontBig = null;
    private static Font customFont = null;
    private static final Border emptyBorder = BorderFactory.createEmptyBorder();

    private final Asteroid[] asteroidsArray = new Asteroid[ASTEROIDNUMBER];

    private JFrame frame;

    private ViewGame movingPanel;
    private ViewHome home;

    private Controller objectsRunnable;
    private JPanel pan;

    private boolean loadOrSave = true;
    private boolean starting = false;

    public Main() throws IOException {

        for (int i = 0; i < asteroidsArray.length; i++) {
            asteroidsArray[i] = new Asteroid(DRAWING_WIDTH);
        }
    }

    @Override
    public void run() {

        new Thread(new Audio("./Sounds/asteroidjam4.wav", 10)).start();

        customFontMaker();

        JButton start = buttonMaker("START", customFont);

        JButton stop = buttonMaker("EXIT", customFont);

        JLabel title = labelMaker("<html><center>AMONG<br>ASTEROIDS</center></html>", customFontBig);

        home = viewMaker(start, stop, title);

        frame = frameMaker();

        start.addActionListener(e -> start_game());
        stop.addActionListener(e -> exitProcedure());
    }

    private void start_game() {

        frameSettingsGame();

        objectsRunnable = new Controller(this, asteroidsArray);
        new Thread(objectsRunnable).start();
    }

    private void frameSettingsGame() {
        frame.remove(home);
        try {
            movingPanel = new ViewGame(asteroidsArray, DRAWING_WIDTH, DRAWING_HEIGHT, System.currentTimeMillis());
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        frame.add(movingPanel);
        frame.pack();

        frame.setVisible(true);
        starting = true;
        movingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ee) {
                firedShot(ee);
            }
        });
    }

    private void firedShot(MouseEvent e) {
        new Thread(new Audio("./Sounds/sound.wav", 1)).start();
        int mx = e.getX();
        int my = e.getY();
        objectsRunnable.collision(mx, my);
    }

    private void exitProcedure() {
        if (starting) {
            objectsRunnable.setRunning(false);
        }
        frame.dispose();
        System.exit(0);
    }

    public void gameEnd() {

        String score = movingPanel.score();

        JButton again = buttonMaker("<html>" + score + "<br>Try again?</html>", customFontBig);
        JButton toltes = buttonMaker("LOAD", customFont);
        JButton mentes = buttonMaker("SAVE", customFont);
        JButton stop = buttonMaker("LEAVE", customFont);

        frame.remove(movingPanel);

        home = viewMakerEnd(again, toltes, mentes, stop);

        frame.add(home);
        frame.pack();
        frame.setVisible(true);

        again.addActionListener(e -> {
            for (int i = 0; i < asteroidsArray.length; i++) {
                try {
                    asteroidsArray[i] = new Asteroid(DRAWING_WIDTH);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            objectsRunnable.setRunning(false);
            start_game();

        });

        mentes.addActionListener(e -> {
            existingLSCheck();

            JButton ok = okbutton();
            JTextArea tf = textAreaMaker();
            mentesView(ok, tf);

            ok.addActionListener(ee -> {
                String userName = tf.getText();
                scoreSaving(userName, score);

            });
        });

        stop.addActionListener(e -> exitProcedure());

        toltes.addActionListener(e -> {
            existingLSCheck();
            scoreLoading();
        });
    }

    private void scoreSaving(String userName, String score) {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                BufferedWriter o = new BufferedWriter(new FileWriter(file));
                o.write(score + ": " + userName);
                o.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void scoreLoading() {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            BufferedReader in;
            try {

                in = new BufferedReader(new FileReader(file));
                pan = new JPanel(new FlowLayout());
                String line = in.readLine();
                JLabel lb = labelMaker("Score:   " + line, customFont);

                pan.add(lb);
                pan.setBackground(Color.BLACK);
                home.add(pan, BorderLayout.SOUTH);
                frame.pack();
                frame.setVisible(true);

            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

        }
    }

    public void repaintMovingPanel() {
        movingPanel.repaint();
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Main());
    }

    private void existingLSCheck() {
        if (!loadOrSave) {
            home.remove(pan);
            frame.pack();
            frame.setVisible(true);
        }
        loadOrSave = false;
    }

    private JFrame frameMaker() {
        frame = new JFrame();
        frame.setTitle("Among Asteroids");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });
        frame.setPreferredSize(new Dimension(DRAWING_WIDTH, DRAWING_HEIGHT));
        frame.setLayout(new BorderLayout());

        frame.add(home);
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    private ViewHome viewMaker(JButton start, JButton stop, JLabel title) {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        for (int i = 0; i < 2; i++)
            panel.add(new JLabel());
        panel.add(start);
        panel.add(stop);
        panel.setBackground(Color.BLACK);

        try {
            home = new ViewHome(DRAWING_WIDTH, DRAWING_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        home.setLayout(new BorderLayout());

        home.add(title);
        home.add(panel, BorderLayout.WEST);
        return home;
    }

    private ViewHome viewMakerEnd(JButton again, JButton toltes, JButton mentes, JButton stop) {
        try {
            home = new ViewHome(DRAWING_WIDTH, DRAWING_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        home.setLayout(new BorderLayout());

        home.add(again, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(7, 1));
        panel.setBackground(Color.BLACK);
        for (int i = 0; i < 2; i++)
            panel.add(new JLabel());

        panel.add(toltes);
        panel.add(mentes);
        panel.add(stop);

        home.add(panel, BorderLayout.WEST);
        return home;
    }

    private void mentesView(JButton ok, JTextArea tf) {
        pan = new JPanel(new FlowLayout());
        JLabel lb = new JLabel("USER: ");
        lb.setFont(lb.getFont().deriveFont(18.0f));
        lb.setForeground(Color.WHITE);

        pan.add(lb);
        pan.add(tf);
        pan.add(ok);
        pan.setBackground(Color.BLACK);
        home.add(pan, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private JButton okbutton() {
        JButton ok = new JButton("DONE!");
        ok.setFont(ok.getFont().deriveFont(18.0f));
        ok.setBackground(Color.WHITE);
        ok.setForeground(Color.BLACK);
        ok.setFocusPainted(false);
        return ok;
    }

    private JTextArea textAreaMaker() {
        JTextArea tf = new JTextArea(1, 20);
        tf.setFont(tf.getFont().deriveFont(18.0f));
        return tf;
    }

    private JButton buttonMaker(String text, Font font) {
        JButton b = new JButton(text);
        b.setFont(font);
        b.setBackground(Color.MAGENTA);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(emptyBorder);

        b.setOpaque(false);
        return b;
    }

    private JLabel labelMaker(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setBackground(Color.MAGENTA);
        label.setForeground(Color.WHITE);
        label.setBorder(emptyBorder);
        return label;
    }

    private void customFontMaker() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./Fonts/font4.otf")).deriveFont(70f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        try {
            customFontBig = Font.createFont(Font.TRUETYPE_FONT, new File("./Fonts/font4.otf")).deriveFont(120f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //register the font
        ge.registerFont(customFont);
    }

}