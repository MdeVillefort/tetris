package assets;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int TILE_SIZE = 10;
    public static final int ROWS = 60;
    public static final int COLUMNS = 45;

    private final int DELAY = 25;

    private Timer timer;

    private ArrayList<Tetromino> tetrominos = new ArrayList<>();

    {
        // Create some random tetrominos for testing
        for (int i = 0; i < 10; i++) {
            tetrominos.add(Tetromino.randomTetromino());
        }
    }

    public GamePanel() {

        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        setBackground(new Color(100, 100, 100));

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public boolean doesPieceFit(Tetromino teromino, int nRotation, int nPosX, int nPoxY) {

        for (int px = 0; px < 4; px++) {
            for (int py = 0; py < 4; py++) {

                // Get index into piece
                int pi = teromino.rotate(px, py, nRotation);
            }
        }

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Tetromino tetromino : tetrominos) {
            tetromino.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }
}
