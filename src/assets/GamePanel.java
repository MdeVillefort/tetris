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
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);

    public static final int TILE_SIZE = 50;
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    private static final int DELAY = 250;

    private Timer timer;
    private Map<Integer, Boolean> keysPressed = new HashMap<>();
    private Tetromino currentTetromino = new Tetromino(0, 0);
    private Tetromino testTetromino = new Tetromino(COLUMNS / 2, ROWS / 2);
    private List<Tetromino> placedTetrominos = new ArrayList<>();
    private int[] field = new int[ROWS * COLUMNS];

    {
        // Initialize the keyPressed mapping
        keysPressed.put(KeyEvent.VK_W, false);
        keysPressed.put(KeyEvent.VK_A, false);
        keysPressed.put(KeyEvent.VK_S, false);
        keysPressed.put(KeyEvent.VK_D, false);
        keysPressed.put(KeyEvent.VK_SPACE, false);

        placedTetrominos.add(testTetromino);

        updateField();

        // Create some random tetrominos for testing
        // for (int i = 0; i < 10; i++) {
        //     tetrominos.add(Tetromino.randomTetromino());
        // }
    }

    public GamePanel() {

        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        setBackground(new Color(100, 100, 100));

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public boolean doesPieceFit(int dx, int dy) {

        currentTetromino.translate(dx, dy);

        for (Tetromino tetromino : placedTetrominos) {
            if (currentTetromino.collidesWith(tetromino)) {
                currentTetromino.translate(-dx, -dy);
                return false;
            }
        }
        return true;
    }

    public Tetromino createTetromino() {
        return new Tetromino((int)(Math.random() * COLUMNS));
    }

    public void draw_grid(Graphics g) {

            g.setColor(Color.BLACK);

            for (int i = 0; i < COLUMNS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
    }

    public void updateField() {
        Arrays.fill(field, 0);
        List<Tetromino> allTetrominos = new ArrayList<>(placedTetrominos);
        allTetrominos.add(currentTetromino);
        for (Tetromino tetromino : allTetrominos) {
            Point pos = tetromino.getPosition();
            for (int x = 0; x < Tetromino.WIDTH; x++) {
                for (int y = 0; y < Tetromino.WIDTH; y++) {
                    int fi = (pos.y + y) * COLUMNS + (pos.x + x);
                    if ((pos.x + x) >=0 && (pos.x + x) < COLUMNS &&
                        (pos.y + y) >= 0 && (pos.y + y) < ROWS) {
                        field[fi] = tetromino.getValue(x, y);
                    }
                }
            }
        }
    }

    public void update() {

        if (keysPressed.get(KeyEvent.VK_W)) {
            if (doesPieceFit(0, -1))
                currentTetromino.translate(0, -1);
        }
        if (keysPressed.get(KeyEvent.VK_A)) {
            if (doesPieceFit(-1, 0))
                currentTetromino.translate(-1, 0);
        }
        if (keysPressed.get(KeyEvent.VK_S)) {
            if (doesPieceFit(0, 1))
                currentTetromino.translate(0, 1);
        }
        if (keysPressed.get(KeyEvent.VK_D)) {
            if (doesPieceFit(1, 0))
                currentTetromino.translate(1, 0);
        }
        if (keysPressed.get(KeyEvent.VK_SPACE)) {
            currentTetromino.setAngle(currentTetromino.getAngle() + 1);
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw_grid(g);

        currentTetromino.draw(g);

        for (Tetromino tetromino : placedTetrominos) {
            tetromino.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        keysPressed.replace(key, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keysPressed.replace(key, false);
    }
}
