package assets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import java.util.Arrays;
import java.util.logging.Logger;

import static assets.Settings.*;

public class Tetromino {

    private static final Logger logger = Logger.getLogger("assets.Tetromiono");

    public static Tetromino randomTetromino() {

        Random random = new Random();
        Point position = new Point(random.nextInt(Settings.COLUMNS),
                                   random.nextInt(Settings.ROWS));
        int angle = random.nextInt(4);
        Color color = new Color(random.nextInt(255),
                                random.nextInt(255),
                                random.nextInt(255));
        return new Tetromino(Shape.randomShape());
    }

    private Shape shape;
    private Block[] blocks = new Block[4];
    private Color color = Color.RED;
    private boolean isLanded = false;

    public Tetromino(Shape shape) {
        this.shape = shape;
        int[][] coordinates = this.shape.getCoordinates();
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new Block(this, coordinates[i]);
        }
    }

    public Tetromino() {
        this(Shape.randomShape());
    }

    public Shape getShape() {
        return shape;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public boolean landed() {
        return isLanded;
    }

    public void draw(Graphics g) {
        
        g.setColor(color);

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
        if (fits(moveDirection)) {
            for (Block block : blocks) {
                block.move(moveDirection);
            }
        } else if (direction == "down") {
            isLanded = true;
        }
    }

    public boolean fits(final int[] moveDirection) {
        return Arrays.stream(blocks)
                     .allMatch(block -> block.fits(moveDirection));
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
            return coordinates;
        }
    }
}
