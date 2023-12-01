import assets.GamePanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.FlowLayout;

import java.util.logging.Logger;

import static assets.Settings.*;

public class Tetris {

    private static final Logger logger = Logger.getLogger("assets.Tetris");

    private static void initWindow() {
        
        JFrame window = new JFrame("Tetris");
        GamePanel panel = new GamePanel(window);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // window.setLayout(new FlowLayout());

        window.add(panel);
        window.addKeyListener(panel);

        window.pack();

        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

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
