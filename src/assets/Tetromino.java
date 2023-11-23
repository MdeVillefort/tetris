package assets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import assets.Tetromino.Shape;

public class Tetromino {

    private static final Logger logger = LoggerFactory.getLogger(Tetromino.class);

    public static final int WIDTH = 4;

    public static Tetromino randomTetromino() {

        Random random = new Random();
        Point position = new Point(random.nextInt(GamePanel.COLUMNS),
                                   random.nextInt(GamePanel.ROWS));
        int angle = random.nextInt(4);
        Color color = new Color(random.nextInt(255),
                                random.nextInt(255),
                                random.nextInt(255));
        return new Tetromino(position, Shape.randomShape(), angle, color);
    }

    private Point position;
    private Shape shape;
    private int angle;
    private Color color;

    public Tetromino(Point position, Shape shape, int angle, Color color) {
        this.position = position;
        this.shape = shape;
        this.angle = angle;
        this.color = color;
    }

    public Tetromino(int x, int y) {
        this(new Point(x, y), Shape.randomShape(), 0, Color.RED);
    }

    public Tetromino(int x) {
        this(new Point(x, -WIDTH), Shape.randomShape(), 0, Color.RED);
    }

    public Point getPosition() {
        return position;
    }

    public Shape getShape() {
        return shape;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle % 4;
    }

    public Color getColor() {
        return color;
    }

    public int getValue(int x, int y) {
        int pi = rotate(x, y);
        return shape.value[pi];
    }

    public int rotate(int px, int py) {
        switch (angle % 4) {
            case 0: return py * WIDTH + px;             // 0 degrees
            case 1: return 12 + py - (px * WIDTH);      // 90 degrees
            case 2: return 15 - (py * WIDTH) - px;      // 180 degrees
            case 3: return 3 - py + (px * WIDTH);       // 270 degrees
        }
        return 0;
    }

    public void draw(Graphics g) {
        
        g.setColor(color);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < WIDTH; y++) {

                if (getValue(x, y) == 1) {
                    g.fillRect((position.x + x) * GamePanel.TILE_SIZE,
                               (position.y + y) * GamePanel.TILE_SIZE,
                               GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
                }

            }
        }
    }

    public void translate(int dx, int dy) {
        position.translate(dx, dy);
    }

    public boolean collidesWith(Tetromino tetromino) {

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < WIDTH; y++) {
                int fi_1 = (position.y + y) * GamePanel.COLUMNS + (position.x + x);
                int fi_2 = (tetromino.getPosition().y + y) * GamePanel.COLUMNS + (tetromino.getPosition().x + x);
                logger.info("fi_1 = " + fi_1 + ", fi_2 = " + fi_2);
                if ((fi_1 == fi_2) && (getValue(x, y) + tetromino.getValue(x, y)) > 1) return true;
            }
        }
        return false;
    }

    public enum Shape {

        I(new int[] {0, 0, 1, 0,
                     0, 0, 1, 0,
                     0, 0, 1, 0,
                     0, 0, 1, 0}),
        J(new int[] {0, 0, 0, 0,
                     0, 1, 1, 0,
                     0, 1, 0, 0,
                     0, 1, 0, 0}),
        L(new int[] {0, 0, 0, 0,
                     0, 1, 1, 0,
                     0, 0, 1, 0,
                     0, 0, 1, 0}),
        O(new int[] {0, 0, 0, 0,
                     0, 1, 1, 0,
                     0, 1, 1, 0,
                     0, 0, 0, 0}),
        S(new int[] {0, 1, 0, 0,
                     0, 1, 1, 0,
                     0, 0, 1, 0,
                     0, 0, 0, 0}),
        T(new int[] {0, 0, 1, 0,
                     0, 1, 1, 0,
                     0, 0, 1, 0,
                     0, 0, 0, 0}),
        Z(new int[] {0, 0, 1, 0,
                     0, 1, 1, 0,
                     0, 1, 0, 0,
                     0, 0, 0, 0});

        public static final Random PRNG = new Random();

        public static Shape randomShape() {
            Shape[] shapes = values();
            return shapes[PRNG.nextInt(shapes.length)];
        }

        private final int[] value;

        Shape(int[] value) {
            this.value = value;
        }
    }
}
