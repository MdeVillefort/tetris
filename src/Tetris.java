import assets.GamePanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tetris {

    private static final Logger logger = LoggerFactory.getLogger(Tetris.class);

    private static void initWindow() {
        
        JFrame window = new JFrame("Tetris");
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        window.add(panel);
        window.addKeyListener(panel);

        window.setResizable(false);
        window.pack();
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