package assets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;

import java.util.logging.Logger;

import static assets.Settings.*;

public class GameMenu extends JPanel {
    
    private static final Logger logger = Logger.getLogger("assets.GameMenu");

    private JFrame window;

    public GameMenu(JFrame window) {
        setPreferredSize(new Dimension(TILE_SIZE * MENU_WIDTH,
                                       TILE_SIZE * ROWS));
        setBackground(new Color(100, 100, 100));
        this.window = window;
    }
}
