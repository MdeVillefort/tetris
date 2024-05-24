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
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
        String[] spriteFiles = Stream.of(new File(Tetris.class.getResource("resources/sprites").getPath()).listFiles())
                                     .filter(file -> !file.isDirectory())
                                     .map(File::getAbsolutePath)
                                     .toArray(String[]::new);
        BufferedImage[] sprites = new BufferedImage[spriteFiles.length];
        if (spriteFiles != null) {
            for (int i = 0; i < spriteFiles.length; i++) {
                try {
                    // I have no idea what I'm doing.
                    // https://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
                    BufferedImage image = ImageIO.read(new File(spriteFiles[i]));
                    BufferedImage resized = new BufferedImage(TILE_SIZE, TILE_SIZE, image.getType());
                    Graphics2D g = resized.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(image, 0, 0, TILE_SIZE, TILE_SIZE, 0, 0, image.getWidth(), image.getHeight(), null);
                    g.dispose();
                    sprites[i] = resized;
                } catch (IOException e) {
                    logger.warning("Error opening image file: " + e.getMessage());
                }
            }
        }
        return sprites;
    }

    private static Font loadCustomFonts() {
        File fontFile = new File(Tetris.class.getResource("resources/font/FREAKSOFNATUREMASSIVE.ttf").getPath());
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
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
