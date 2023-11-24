package assets;

import java.awt.Graphics;

import static assets.Settings.*;

public class Block {
    
    private Tetromino tetromino;
    private int[] position = new int[2];
    private int[] topLeftCoordsPixels = new int[2];

    public Block(Tetromino tetromino, int[] position) {
        this.tetromino = tetromino;
        position[0] += INIT_POS_OFFSET[0];
        position[1] += INIT_POS_OFFSET[1];
        this.position = position;
        update();
    }

    public int[] getPosition() {
        return position;
    }

    public void update() {
        topLeftCoordsPixels[0] = position[0] * TILE_SIZE;
        topLeftCoordsPixels[1] = position[1] * TILE_SIZE;
    }
    

    public void move(int[] direction) {
        position[0] += direction[0];
        position[1] += direction[1];
        update();
    }

    public void draw(Graphics g) {
        g.fillRect(topLeftCoordsPixels[0], topLeftCoordsPixels[1],
                   TILE_SIZE, TILE_SIZE);
    }

    public boolean fits(int[] direction) {
        int x = position[0] + direction[0];
        int y = position[1] + direction[1];
        if ((0 <= x && x < COLUMNS && y < ROWS) &&
            (y < 0 || tetromino.getPanel().getField()[y][x] == null)) {
            return true;
        }
        return false;
    }

}
