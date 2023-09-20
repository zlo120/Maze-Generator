package gui;

import mazeGenerator.Cell;
import mazeGenerator.Maze;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class WallClickListener extends MouseAdapter {

    private MazePanel wallPanel;

    private final int lowerLim = 2;
    private final int upperLim = 8;
    private final int denominator = 10;

    private Maze maze;

    public WallClickListener(MazePanel wallPanel, Maze maze) {
        super();
        this.wallPanel = wallPanel;
        this.maze = maze;
    }

    private boolean withinRange(int coord) {
        return (coord > (7.5 * (maze.getPixelSize() / 10)));
    }

    @Override
    public void mouseClicked(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        List<Cell> grid = wallPanel.getGrid();
        int xcoord = (int)(Math.floorDiv(x, maze.getPixelSize()));
        int ycoord = (int)(Math.floorDiv(y, maze.getPixelSize()));
        int xRemainder = x % maze.getPixelSize();
        int yRemainder = y % maze.getPixelSize();

        boolean north = yRemainder < (lowerLim * maze.getPixelSize() / denominator) && (xRemainder > (lowerLim * maze.getPixelSize() / denominator)) && (xRemainder < (upperLim * maze.getPixelSize() / denominator));
        boolean east = xRemainder > (upperLim * maze.getPixelSize() / denominator) && (yRemainder > (lowerLim * maze.getPixelSize() / denominator)) && (yRemainder < (upperLim * maze.getPixelSize() / denominator));
        boolean south = yRemainder > (upperLim * maze.getPixelSize() / denominator) && (xRemainder > (lowerLim * maze.getPixelSize() / denominator)) && (xRemainder < (upperLim * maze.getPixelSize() / denominator));
        boolean west = xRemainder < (lowerLim * maze.getPixelSize() / denominator) && (yRemainder > (lowerLim * maze.getPixelSize() / denominator)) && (yRemainder < (upperLim * maze.getPixelSize() / denominator));

        // Check each cell
        for (int index = 0; index < grid.size(); index++) {
            Cell cell = grid.get(index);

            // Cell matches current click.
            if (cell.getX() == xcoord && cell.getY() == ycoord) {
//                System.out.println("Index: " + index);
//                System.out.println("Maze.Cols: " + Maze.getRowCellNum());
//                System.out.println("Remainder: " + index % Maze.getRowCellNum());
//
//                System.out.println("Index: " + index);
//                System.out.println("Maze.getRowCellNum(): " + Maze.getRowCellNum());
//                System.out.println("Maze.Cols: " + Maze.cols);
//                System.out.println("Remainder: " + (index + 10) % Maze.getRowCellNum());
//                System.out.println("Before: " + index % 10 + '\n');
                // North wall
                if (north) {
                    // It has an adjacent wall.
                    // Ignore upper-most walls.
                    if (((index % maze.getRowNum()) > 0)) {
                        // Either the current cell or adjacent cell has a wall.
                        if(grid.get(index - 1).getWalls()[2] || cell.getWalls()[0]) {
                            grid.get(index - 1).setWall(2, false);
                            grid.get(index).setWall(0, false);
                            maze.setEdited(true);
                        } else {
                            // No adjacent wall.
                            grid.get(index - 1).setWall(2, true);
                            grid.get(index).setWall(0, true);
                            maze.setEdited(true);
                        }
                    }
                }
                // East Walls
                if (east) {
                    // It has an adjacent wall.
                    // Ignore the right-most walls.
                    if ((index + 10) / maze.getRowNum() < maze.getColNum()) {
                        if(grid.get(index + maze.getRowNum()).getWalls()[3] || cell.getWalls()[1]) {
                            grid.get(index + maze.getRowNum()).setWall(3, false);
                            grid.get(index).setWall(1, false);
                            maze.setEdited(true);
                        } else {
                            // No adjacent wall.
                            grid.get(index + maze.getRowNum()).setWall(3, true);
                            grid.get(index).setWall(1, true);
                            maze.setEdited(true);
                        }
                    }
                }
                // South Walls
                if (south) {
                    // It has an adjacent wall.
                    // Ignore the bottom-most walls.
                    if (index % maze.getRowNum() < maze.getColNum() + 1) {
                        if(grid.get(index + 1).getWalls()[0] || cell.getWalls()[2]) {
                            grid.get(index + 1).setWall(0, false);
                            grid.get(index).setWall(2, false);
                            maze.setEdited(true);
                        } else {
                            // No adjacent wall.
                            grid.get(index + 1).setWall(0, true);
                            grid.get(index).setWall(2, true);
                            maze.setEdited(true);
                        }
                    }
                }
                // West Walls.
                if (west) {
                    // It has an adjacent wall.
                    // Ignore the left-most walls.
                    if (index >= maze.getRowNum()) {
                        if(grid.get(index - maze.getRowNum()).getWalls()[1] || cell.getWalls()[3]) {
                            grid.get(index - maze.getRowNum()).setWall(1, false);
                            grid.get(index).setWall(3, false);
                            maze.setEdited(true);
                        } else {
                            // No adjacent wall.
                            grid.get(index - maze.getRowNum()).setWall(1, true);
                            grid.get(index).setWall(3, true);
                            maze.setEdited(true);
                        }
                    }
                }


            }
        }
        wallPanel.repaint();
    }

}
