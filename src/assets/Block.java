package assets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

import static assets.Settings.*;

public class Block {
    
    private Tetromino tetromino;
    private int[] position = new int[2];
    private int[] topLeftCoordsPixels = new int[2];
    private boolean isAlive = true;

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

    public void setPosition(int[] newPosition) {
        for (int i = 0; i < position.length; i++) {
            position[i] = newPosition[i];
        }
        setRectPosition();
    }

    private void setRectPosition() {
        topLeftCoordsPixels[0] = position[0] * TILE_SIZE;
        topLeftCoordsPixels[1] = position[1] * TILE_SIZE;
    }

    public boolean alive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public void update() {
        setRectPosition();
    }

    public int[] move(int[] direction) {
        int[] newPosition = Arrays.copyOf(position, position.length);
        newPosition[0] += direction[0];
        newPosition[1] += direction[1];
        return newPosition;
    }

    public void draw(Graphics g) {
        g.drawImage(
            tetromino.getSprite(),
            topLeftCoordsPixels[0],
            topLeftCoordsPixels[1],
            tetromino.getPanel()
        );
        if (!isAlive) {
            g.setColor(Color.BLACK);
            g.drawLine(topLeftCoordsPixels[0], topLeftCoordsPixels[1],
                       topLeftCoordsPixels[0] + TILE_SIZE, topLeftCoordsPixels[1] + TILE_SIZE);
            g.drawLine(topLeftCoordsPixels[0] + TILE_SIZE, topLeftCoordsPixels[1],
                       topLeftCoordsPixels[0], topLeftCoordsPixels[1] + TILE_SIZE);
        }
    }

    public boolean fits(int[] newPosition) {
        int x = newPosition[0];
        int y = newPosition[1];
        if ((0 <= x && x < COLUMNS && y < ROWS) &&
            (y < 0 || tetromino.getPanel().getField()[y][x] == null)) {
            return true;
        }
        return false;
    }

}
