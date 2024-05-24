package assets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;


import static assets.Settings.*;

public class Block {
    

    private GameField gameField;
    private BufferedImage sprite;
    private int[] position = new int[2];            // block position in game field cell coordinates
    private int[] topLeftCoordsPixels = new int[2]; // block position in game field pixel coordinates
    private boolean isAlive = true;

    public Block(GameField gameField, int[] position, BufferedImage sprite) {
        this.gameField = gameField;
        this.position = position;
        this.sprite = sprite;
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
            sprite,
            topLeftCoordsPixels[0],
            topLeftCoordsPixels[1],
            gameField
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

        // Must be within game field or above it AND no blocks exist in new position
        if ((0 <= x && x < COLUMNS && y < ROWS) &&
            (y < 0 || gameField.getField()[y][x] == null)) {
            return true;
        }

        return false;
    }

}
