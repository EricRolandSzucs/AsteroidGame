// SZUCS ERIC-ROLAND, 524, seim1964

import javax.swing.SwingUtilities;

public class Controller implements Runnable {

    private volatile boolean running;

    private final Asteroid[] asteroidsArray;

    private final Main mainView;

    private int count = 0;

    private static final int goal = 20;

    public Controller(Main movingSquares, Asteroid[] gameObjectsArray) {
        this.mainView = movingSquares;
        this.asteroidsArray = gameObjectsArray;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            updateObjects();
            invokeLater();
            sleep();
        }
    }

    private void updateObjects() {
        for (Asteroid asteroid : asteroidsArray) {
            asteroid.move();
        }
    }

    private void invokeLater() {
        SwingUtilities.invokeLater(mainView::repaintMovingPanel);

    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {
        }
    }

    public void collision(int mx, int my) {
        for (Asteroid asteroid : asteroidsArray) {
            if (mx > asteroid.getX() && mx < asteroid.getX() + asteroid.getSizeH() && my > asteroid.getY() && my < asteroid.getY() + asteroid.getSizeW()) {
                hit(asteroid);
                break;
            }
        }
    }

    private void hit(Asteroid a) {
        a.coordinates();
        new Thread(new Audio("./Sounds/boom.wav", 1)).start();
        count++;
        if (count == goal) {
            mainView.gameEnd();
        }
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

}