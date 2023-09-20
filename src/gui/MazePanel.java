package gui;

import mazeGenerator.Cell;
import mazeGenerator.Maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the panel that holds the maze
 */
public class MazePanel extends JPanel implements Serializable {

    private List<Cell> grid;
    private Maze maze;
    int rows;
    int cols;


    /**
     * @param grid list of Cells that make up the grid
     */
    public MazePanel(List<Cell> grid, int rows, int cols, Maze maze) {
        this.grid = grid;
        this.rows = rows;
        this.cols = cols;
        this.maze = maze;

        this.setSize(rows * maze.getPixelSize(), cols * maze.getPixelSize());
    }

    public void setGrid(List<Cell> grid) {
        this.grid = grid;
    }

    public List<Cell> getGrid() {
        return this.grid;
    }

    /**
     * @param g Graphics object that will be painted on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Cell c : grid) {
            c.draw(g);
        }
    }

    /**
     * @return Dimension to set appropriate size of panel
     */
    @Override
    public Dimension getPreferredSize() {
        // +1 pixel on width and height so bottom and right borders can be drawn.
        return new Dimension(Maze.WIDTH * 2, Maze.HEIGHT * 2);
    }

    public void takePicture(JPanel panel) {
        BufferedImage img = new BufferedImage(rows * maze.getPixelSize() + 1, cols * maze.getPixelSize() + 1, BufferedImage.TYPE_INT_RGB);
        panel.print(img.getGraphics()); // or: panel.printAll(...);
        try {
            ImageIO.write(img, "jpg", new File("panel.jpg"));
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
