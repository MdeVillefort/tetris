package assets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Arrays;
import java.util.logging.Logger;

import static assets.Settings.*;

public class Tetromino {

    private static final Logger logger = Logger.getLogger("assets.Tetromiono");
    private static final Color DEFAULT_COLOR = Color.RED;

    private GamePanel panel;
    private Shape shape;
    private BufferedImage sprite;
    private Block[] blocks = new Block[4];
    private boolean isLanded = false;

    public Tetromino(GamePanel panel, Shape shape, BufferedImage sprite) {
        this.panel = panel;
        this.shape = shape;
        this.sprite = sprite;
        int[][] coordinates = this.shape.getCoordinates();
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new Block(this, coordinates[i]);
        }
        logger.fine("New " + this.shape.name() +
                    " tetromino created at " + Arrays.deepToString(getBlockPositions()));
    }

    public Tetromino(GamePanel panel) {
        this(panel,
             Shape.randomShape(),
             panel.getSprites()[(int)System.currentTimeMillis() % panel.getSprites().length]);
    }

    public Shape getShape() {
        return shape;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public int[][] getBlockPositions() {
        int[][] blockPositions = new int[blocks.length][];
        for (int i = 0; i < blocks.length; i++) {
            blockPositions[i] = blocks[i].getPosition();
        }
        return blockPositions;
    }

    public GamePanel getPanel() {
        return panel;
    }

    public boolean landed() {
        return isLanded;
    }

    public void draw(Graphics g) {

        for (Block block : blocks) {
            block.draw(g);
        }

    }

    public void update() {
        move("down");
    }

    public void move(String direction) {
        direction = direction.toLowerCase();
        int[] moveDirection = MOVE_DIRECTIONS.get(direction);
        if (moveDirection == null) {
            logger.warning("Move direction " + direction + " is not valid.");
            return;
        }

        // Need new block positions calculated here and passed to fits.
        int[][] newBlockPositions = Arrays.stream(getBlockPositions())
                                          .map(pos -> new int[] {pos[0] + moveDirection[0],
                                                                 pos[1] + moveDirection[1]})
                                          .toArray(int[][]::new);

        if (fits(newBlockPositions)) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].setPosition(newBlockPositions[i]);
            }
        } else if (direction == "down") {
            isLanded = true;
        }
    }

    public void rotate() {

        // Need new block positions calcualted here and pass to fits.
        // Use simple rotation matrix to perform rotation.
        int[] pivotPoint = getBlockPositions()[0];
        int[][] newBlockPositions = Arrays.stream(getBlockPositions())
                                          .map(int[]::clone)
                                          .toArray(int[][]::new);

        for (int[] blockPosition : newBlockPositions) {

            blockPosition[0] -= pivotPoint[0];
            blockPosition[1] -= pivotPoint[1];
            
            int[] temp = Arrays.copyOf(blockPosition, blockPosition.length);

            blockPosition[0] = -temp[1];
            blockPosition[1] = temp[0];

            blockPosition[0] += pivotPoint[0];
            blockPosition[1] += pivotPoint[1];
        }

        if (fits(newBlockPositions)) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].setPosition(newBlockPositions[i]);
            }
        }

    }

    public boolean fits(final int[][] newBlockPositions) {
        for (int i = 0; i < blocks.length; i++) {
            if (!blocks[i].fits(newBlockPositions[i])) {
                return false;
            }
        }
        return true;
    }

    public enum Shape {

        I(new int[][] {{0,0}, {0,1}, {0,-1}, {0,-2}}),
        J(new int[][] {{0,0}, {-1,0}, {0,-1}, {0,-2}}),
        L(new int[][] {{0,0}, {1,0}, {0,-1}, {0,-2}}),
        O(new int[][] {{0,0}, {0,-1}, {1,0}, {1,-1}}),
        S(new int[][] {{0,0}, {-1,0}, {0,-1}, {1,-1}}),
        T(new int[][] {{0,0}, {-1,0}, {1,0}, {0,-1}}),
        Z(new int[][] {{0,0}, {1,0}, {0,-1}, {-1,-1}});

        public static final Random PRNG = new Random();

        public static Shape randomShape() {
            Shape[] shapes = values();
            return shapes[PRNG.nextInt(shapes.length)];
        }

        private final int[][] coordinates;

        Shape(int[][] coordinates) {
            this.coordinates = coordinates;
        }

        public int[][] getCoordinates() {
            return Arrays.stream(coordinates)
                         .map(int[]::clone)
                         .toArray(int[][]::new);
        }
    }
}
