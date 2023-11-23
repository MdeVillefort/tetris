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

import static assets.Settings.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);

    private Timer timer;
    private Map<Integer, Boolean> keysPressed = new HashMap<>();
    private Tetromino tetromino = new Tetromino();
    private Block[] field = new Block[ROWS * COLUMNS];
    private long lastFrameTime = System.nanoTime();

    {
        // Initialize the keyPressed mapping
        keysPressed.put(KeyEvent.VK_A, false);
        keysPressed.put(KeyEvent.VK_S, false);
        keysPressed.put(KeyEvent.VK_D, false);
        keysPressed.put(KeyEvent.VK_SPACE, false);

        // Create some random tetrominos for testing
        // for (int i = 0; i < 10; i++) {
        //     tetrominos.add(Tetromino.randomTetromino());
        // }
    }

    public GamePanel() {

        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS,
                                       TILE_SIZE * ROWS));
        setBackground(new Color(100, 100, 100));

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void draw_grid(Graphics g) {

            g.setColor(Color.BLACK);

            for (int i = 0; i < Settings.COLUMNS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
    }

    public void update() {

        // Check if ready to update block positions
        long currentTime = System.nanoTime();
        if ((currentTime - lastFrameTime) > ANIM_TIME_INTERVAL) {
            tetromino.update();
            lastFrameTime = currentTime;
        }
        
    }

    public void check_events() {
        // check for events?
    }

    public void control(int keyPressed) {

        if (keysPressed.get(KeyEvent.VK_A)) {
            tetromino.move("left");
        }
        if (keysPressed.get(KeyEvent.VK_S)) {
            tetromino.move("right");
        }
        if (keysPressed.get(KeyEvent.VK_D)) {
            tetromino.move("down");
        }
        if (keysPressed.get(KeyEvent.VK_SPACE)) {
            // rotate tetromino
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
        tetromino.draw(g);
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
