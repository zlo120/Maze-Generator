import gui.GUI;
import javax.swing.*;

/**
 * This class is to implement Maze Generator GUI and its functionalities
 */
public class Main {
    /**
     * Implement the MazeGenerator GUI
     * @param args
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new GUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}