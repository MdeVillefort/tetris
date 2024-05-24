package assets;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Comparator;

import static assets.Settings.*;

public class GameField extends JPanel implements ActionListener, KeyListener {


    private static final Logger logger = Logger.getLogger("assets.GameField");

    private JFrame window;
    private BufferedImage[] sprites;
    private Font font;
    private Timer timer;
    private Tetromino tetromino;
    private Tetromino nextTetromino;
    private Block[][] field = new Block[ROWS][COLUMNS];
    private ArrayDeque<Integer> lines = new ArrayDeque<>();
    private long lastFrameTime = System.nanoTime();
    private boolean isPaused = false;
    private int score = 0;

    public GameField(JFrame window, BufferedImage[] sprites, Font font) {

        setPreferredSize(new Dimension(TILE_SIZE * (COLUMNS + MENU_WIDTH),
                                       TILE_SIZE * ROWS + 1));
        setBackground(new Color(10, 10, 10));

        this.window = window;
        this.sprites = sprites;
        this.font = font;
        this.tetromino = new Tetromino(this, true);
        this.nextTetromino = new Tetromino(this, false);

        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    public Block[][] getField() {
        return field;
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public BufferedImage getRandomSprite() {
        return getSprites()[(int)(System.currentTimeMillis() % getSprites().length)];
    }

    public boolean paused() {
        return isPaused;
    }

    public int getScore() {
        return score;
    }

    public void drawGrid(Graphics g) {

        g.setColor(Color.GRAY);

        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public void drawMenu(Graphics g) {
        g.setFont(font.deriveFont((float)(TILE_SIZE * 2)));
        g.setColor(Color.WHITE);
        g.drawString("TETRIS", GAME_LABEL_POSITION[0] * TILE_SIZE, GAME_LABEL_POSITION[1] * TILE_SIZE);
        g.setFont(font.deriveFont((float)(TILE_SIZE * 1.5)));
        g.setColor(Color.ORANGE);
        g.drawString("NEXT", NEXT_LABEL_POSITION[0] * TILE_SIZE, NEXT_LABEL_POSITION[1] * TILE_SIZE);
        g.drawString("SCORE", SCORE_LABEL_POSITION[0] * TILE_SIZE, SCORE_LABEL_POSITION[1] * TILE_SIZE);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(score), SCORE_POSITION[0] * TILE_SIZE, SCORE_POSITION[1] * TILE_SIZE);
    }

    public void update() {

        // Check if ready to update tetromino vertical position
        long currentTime = System.nanoTime();
        long extraTime = lines.isEmpty() ? 0 : 250_000_000;

        if (!isPaused && 
           (currentTime - lastFrameTime) > ANIM_TIME_INTERVAL + extraTime) {
            tetromino.update();
            checkTetrominoLanding();
            incrementScore();
            clearFullLines();
            checkFullLines(true);
            lastFrameTime = currentTime;
        }

    }

    public boolean isGameOver() {
        return tetromino.landed() &&
               tetromino.getBlockPositions()[0][1] == INIT_POS_OFFSET[1];
    }

    public void clearField() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                field[i][j] = null;
            }
        }
        lines.clear();
    }

    public void createNewTetromino() {
        nextTetromino.setCurrent(true);
        tetromino = nextTetromino;
        nextTetromino = new Tetromino(this, false);
    }

    public void clearFullLines() {

        while (!lines.isEmpty()) {
            // Remove any full lines
            int line = lines.pop();
            logger.fine("Clearing row " + line);
            for (int x = 0; x < COLUMNS; x++) {
                field[line][x] = null;
            }
            // Move blocks above cleared line down as much as possible
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

    }

    public void checkFullLines(boolean checkAllLines) {

        Integer[] yValues;

        if (checkAllLines) {
            yValues = IntStream.range(0, ROWS)
                               .boxed()
                               .sorted(Comparator.reverseOrder())
                               .toArray(Integer[]::new);
        } else {
            int[][] blockPositions = tetromino.getBlockPositions();
            yValues = Arrays.stream(blockPositions)
                            .map(pos -> pos[1])
                            .filter(y -> y >= 0)
                            .distinct()
                            .sorted(Comparator.reverseOrder())
                            .toArray(Integer[]::new);
            logger.fine("Sorted and unqiue y positions for tetromino: " + 
                         Arrays.toString(yValues));
        }

        for (Integer yValue : yValues) {
            int y = yValue.intValue();
            boolean isLine = Arrays.stream(field[y])
                                   .allMatch(block -> block != null);

            if (isLine) {
                logger.fine("Row " + y + " is a full line.");
                lines.push(y);
            }
        }
    }

    public void incrementScore() {
        int numFullLines = lines.size();
        if (numFullLines == 0) return;
        switch (numFullLines){
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 700;
                break;
            default:
                score += 1500;
                break;
        }
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
            if (isGameOver()) {
                clearField();
                score = 0;
                tetromino = new Tetromino(this, true);
                nextTetromino = new Tetromino(this, false);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
            checkFullLines(false);
            createNewTetromino();
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

        drawGrid(g);
        drawMenu(g);
        tetromino.draw(g);
        nextTetromino.draw(g);

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
        if (key == KeyEvent.VK_A && !isPaused) {
            tetromino.move("left");
        }
        if (key == KeyEvent.VK_D && !isPaused) {
            tetromino.move("right");
        }
        if (key == KeyEvent.VK_S && !isPaused) {
            tetromino.move("down");
        }
        if (key == KeyEvent.VK_SPACE && !isPaused) {
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
