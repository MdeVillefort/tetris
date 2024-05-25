import assets.GameField;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import static assets.Settings.*;

public class Tetris {

    private static final Logger logger = Logger.getLogger("assets.Tetris");

    private static void initWindow() {
        
        JFrame window = new JFrame("Tetris");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        BufferedImage[] sprites = loadSprites();
        Font customFont = loadCustomFonts();
        GameField field = new GameField(window, sprites, customFont);

        window.addKeyListener(field);

        window.add(field);

        window.pack();

        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }

    private static BufferedImage[] loadSprites() {

        /* 
         * I hate this!  No easy way to make loading sprite files without some amount of
         * hard coding so that code runs in both IDE and jar file.
         * https://stackoverflow.com/questions/1429172/how-to-list-the-files-inside-a-jar-file
         */
        BufferedImage[] sprites = new BufferedImage[6];
        try {
            for (int i = 0; i <= 5; i++) {
                // I have no idea what I'm doing.
                // https://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
                BufferedImage image = ImageIO.read(Tetris.class.getResourceAsStream("/resources/sprites/" + i + ".png"));
                BufferedImage resized = new BufferedImage(TILE_SIZE, TILE_SIZE, image.getType());
                Graphics2D g = resized.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(image, 0, 0, TILE_SIZE, TILE_SIZE, 0, 0, image.getWidth(), image.getHeight(), null);
                g.dispose();
                sprites[i] = resized;
            }
        } catch (IOException e) {
            logger.warning("Error opening image file: " + e.getMessage());
        }
        return sprites;
    }

    private static Font loadCustomFonts() {
        InputStream fontInputStream = Tetris.class.getResourceAsStream("/resources/font/FREAKSOFNATUREMASSIVE.ttf");
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
        } catch (FontFormatException | IOException e) {
            logger.warning("Error opening font file: " + e.getMessage());
        }
        return customFont;
    }

    public static void main(String[] args) throws Exception {

        // invokeLater() is used here to prevent our graphics processing from
        // blocking the GUI. https://stackoverflow.com/a/22534931/4655368
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow();
            }
        });

    }
}
