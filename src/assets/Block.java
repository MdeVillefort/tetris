package assets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import static assets.Settings.*;

public class Block {
    
    private Tetromino tetromino;
    private int[] position = new int[2];
    private int[] topLeftCoordsPixels = new int[2];
    private boolean isAlive = true;

    public Block(Tetromino tetromino, int[] position) {
        this.tetromino = tetromino;
        if (tetromino.current()) {
            position[0] += INIT_POS_OFFSET[0];
            position[1] += INIT_POS_OFFSET[1];
        } else {
            position[0] += NEXT_POS_OFFSET[0];
            position[1] += NEXT_POS_OFFSET[1];
        }
        this.position = position;
        setRectPosition();
    }

    public int[] getPosition() {
        return Arrays.copyOf(position, position.length);
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

    public int[] move(int[] direction) {
        int[] newPosition = Arrays.copyOf(position, position.length);
        newPosition[0] += direction[0];
        newPosition[1] += direction[1];
        return newPosition;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(
            tetromino.getSprite(),
            topLeftCoordsPixels[0],
            topLeftCoordsPixels[1],
            tetromino.getGameField()
        );
        if (!isAlive) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(topLeftCoordsPixels[0], topLeftCoordsPixels[1],
                       topLeftCoordsPixels[0] + TILE_SIZE, topLeftCoordsPixels[1] + TILE_SIZE);
            g2.drawLine(topLeftCoordsPixels[0] + TILE_SIZE, topLeftCoordsPixels[1],
                       topLeftCoordsPixels[0], topLeftCoordsPixels[1] + TILE_SIZE);
        }
    }

    public boolean fits(int[] newPosition) {
        int x = newPosition[0];
        int y = newPosition[1];
        if ((0 <= x && x < COLUMNS && y < ROWS) &&
            (y < 0 || tetromino.getGameField().getField()[y][x] == null)) {
            return true;
        }
        return false;
    }

}
