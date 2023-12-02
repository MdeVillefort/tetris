package assets;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.logging.Logger;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Comparator;

import static assets.Settings.*;

public class GameField extends JPanel implements ActionListener, KeyListener {

    private static final Logger logger = Logger.getLogger("assets.GameField");

    private JFrame window;
    private BufferedImage[] sprites;
    private Timer timer;
    private Tetromino tetromino;
    private Block[][] field = new Block[ROWS][COLUMNS];
    private ArrayDeque<Integer> lines = new ArrayDeque<>();
    private long lastFrameTime = System.nanoTime();
    private boolean isPaused = false;

    public GameField(JFrame window, BufferedImage[] sprites) {

        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS,
                                       TILE_SIZE * ROWS));
        setBackground(new Color(10, 10, 10));

        this.window = window;
        this.sprites = sprites;
        this.tetromino = new Tetromino(this);

        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    public Block[][] getField() {
        return field;
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public boolean paused() {
        return isPaused;
    }

    public void draw_grid(Graphics g) {

            g.setColor(Color.GRAY);

            for (int i = 0; i < COLUMNS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
    }

    public void update() {

        // Check if ready to update tetromino vertical position
        long currentTime = System.nanoTime();
        long extraTime = lines.isEmpty() ? 0 : 250_000_000;

        if (!isPaused && 
           (currentTime - lastFrameTime) > ANIM_TIME_INTERVAL + extraTime) {
            clearFullLines();
            tetromino.update();
            checkTetrominoLanding();
            lastFrameTime = currentTime;
        }

    }

    public void clearFullLines() {

        // Remove any full lines
        // Move blocks above down as needed

        boolean linesCleared = !lines.isEmpty();

        while (!lines.isEmpty()) {
            int line = lines.pop();
            logger.fine("Clearing row " + line);
            for (int x = 0; x < COLUMNS; x++) {
                field[line][x] = null;
            }
            for (int y = line - 1; y >= 0; y--) {
                for (int x = 0; x < COLUMNS; x++) {
                    Block block = field[y][x];
                    if (block != null) {
                        field[y][x] = null;
                        int[] newPosition = block.move(MOVE_DIRECTIONS.get("down"));
                        while (block.fits(newPosition)) {
                            block.setPosition(newPosition);
                            newPosition = block.move(MOVE_DIRECTIONS.get("down"));
                        }
                        field[block.getPosition()[1]][block.getPosition()[0]] = block;
                    }
                }
            }
        }

        logger.finer("Curent Field Array: \n" + Arrays.deepToString(field));

        if (linesCleared)
            tetromino = new Tetromino(this);

    }

    public boolean checkFullLines() {

        // Show blocks in field that complete a line
        // Only check lines occupied by current tetromino

        int[][] blockPositions = tetromino.getBlockPositions();
        Integer[] yValues = Arrays.stream(blockPositions)
                                  .map(pos -> pos[1])
                                  .filter(y -> y >= 0)
                                  .distinct()
                                  .sorted(Comparator.reverseOrder())
                                  .toArray(Integer[]::new);

        logger.fine("Sorted and unqiue y positions for tetromino: " + 
                    Arrays.toString(yValues));

        for (Integer yValue : yValues) {
            int y = yValue.intValue();
            boolean isLine = Arrays.stream(field[y])
                                   .allMatch(block -> block != null);

            if (isLine) {
                logger.fine("Row " + y + " is a full line.");
                lines.push(y);
                for (int x = 0; x < COLUMNS; x++) {
                    field[y][x].kill();
                }
            }
        }

        return !lines.isEmpty();

    }

    public void checkTetrominoLanding() {
        if (tetromino.landed()) {
            // Add blocks to field and create a new tetromino
            for (Block block : tetromino.getBlocks()) {
                int[] pos = block.getPosition();
                // Ignore blocks that fall outside field
                if (0 <= pos[0] && pos[0] < COLUMNS &&
                    0 <= pos[1] && pos[1] < ROWS) {
                    field[pos[1]][pos[0]] = block;
                }
            }

            logger.fine(tetromino.getShape().name() + " tetromino landed at " +
                        Arrays.deepToString(tetromino.getBlockPositions()));

            if(!checkFullLines()) {
                tetromino = new Tetromino(this);
            }
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
        if (key == KeyEvent.VK_S) {
            tetromino.move("down");
        }
        if (key == KeyEvent.VK_SPACE) {
            tetromino.rotate();
        }
        if (key == KeyEvent.VK_P) {
            isPaused = !isPaused;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            this.window.dispose();
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }
}
