package assets;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.logging.Logger;
import java.util.Arrays;

import static assets.Settings.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final Logger logger = Logger.getLogger("assets.GamePanel");

    private Timer timer;
    private Tetromino tetromino = new Tetromino();
    private Block[][] field = new Block[ROWS][COLUMNS];
    private long lastFrameTime = System.nanoTime();

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

        // Check if ready to update tetromino vertical position
        long currentTime = System.nanoTime();
        if ((currentTime - lastFrameTime) > ANIM_TIME_INTERVAL) {
            tetromino.update();
            checkTetrominoLanding();
            lastFrameTime = currentTime;
        }

    }

    public void checkTetrominoLanding() {
        if (tetromino.landed()) {
            // Add blocks to field and create a new tetromino
            for (Block block : tetromino.getBlocks()) {
                int[] pos = block.getPosition();
                field[pos[1]][pos[0]] = block;
            }
            logger.fine(tetromino.getShape().name() + " tetromino landed at " +
                        Arrays.deepToString(tetromino.getBlockPositions()));
            tetromino = new Tetromino();
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
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Block block = field[i][j];
                if (block != null) {
                    block.draw(g);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            tetromino.move("left");
        }
        if (key == KeyEvent.VK_D) {
            tetromino.move("right");
        }
        if (key == KeyEvent.VK_SPACE) {
            // rotate tetromino
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }
}
