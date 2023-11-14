package assets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import java.util.Random;

public class Tetromino {

    public static final int WIDTH = 4;

    private Point position;
    private Shape shape;
    private int angle;
    private Color color = Color.green;

    public Tetromino(Point position, Shape shape, int angle, Color color) {
        this.position = position;
        this.shape = shape;
        this.angle = angle;
        this.color = color;
    }

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

    public int rotate(int px, int py, int r) {
        switch (r % 4) {
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

                int pi = rotate(x, y, angle);

                if (this.shape.value[pi] == 1) {
                    g.fillRect((position.x + x) * GamePanel.TILE_SIZE,
                               (position.y + y) * GamePanel.TILE_SIZE,
                               GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
                }

            }
        }

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

        private int[] value = new int[16];

        Shape(int[] value) {
            this.value = value;
        }
    }
}
