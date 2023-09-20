package mazeGenerator;

import java.awt.*;
import java.io.Serializable;


/**
 * Represents a single cell inside a List of cells that make up the grid
 */
public class Cell implements Serializable {

    /**
     * coordinates of the cell
     */
    int x,y;

    private boolean isStartCell, isDestinationCell, isTrailcell = false;
    /**
     * placeholder variables for generating the maze
     */
    private boolean visited, deadEnd = false;
    /**
     * Maze object that contains all the relevant information to create the maze
     */
    private Maze maze;

    public int getX() {
        return x;
    }

    public  int getY() {
        return y;
    }

    // top 0, right 1, bottom 2, left 3
    private boolean[] walls = {true, true, true, true};

    private int pixelSize;

    /**
     * @return an array of booleans that represents if a wall exists in a certain direction or not
     */
    public boolean[] getWalls() {
        return walls;
    }

    public boolean adjacentWallExists(int side, int index) {
        if (side == 0) {
            if (index - maze.getRowNum() > 0) {
                return true;
            }
            return false;
        }
        if (side == 1) {
            if (index % maze.getRowNum() == maze.getRowNum() - 1) {
                return true;
            }
            return false;
        }
        if (side == 2) {
            if (index % maze.getRowNum() == 0) {
                return true;
            }
            return false;
        }
        if (side == 3) {
            if (index + maze.getRowNum() < maze.getRowNum() * maze.getColNum()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * @param x coordinate of maze
     * @param y coordinate of maze
     */
    public Cell(int x, int y, Maze maze) {
        this.x = x;
        this.y = y;
        this.maze = maze;
    }

    public boolean has3Walls() {
        boolean[] walls = getWalls();
        int wallCount = 0;
        if (walls[0]) {
            wallCount++;
        }

        if(walls[1]) {
            wallCount++;
        }

        if(walls[2]) {
            wallCount++;
        }

        if(walls[3]) {
            wallCount++;
        }

        return wallCount == 3;
    }

    public void setStartCell(boolean val) {
        this.isStartCell = true;
    }

    public void setDestinationCell(boolean val) {
        this.isDestinationCell = val;
    }

    public void setTrailCell(boolean val) { this.isTrailcell = val; }

    public boolean isTrailCell() { return isTrailcell; };

    public boolean isStartCell(){
        return isStartCell;
    }

    public boolean isDestinationCell(){
        return isDestinationCell;
    }

    /**
     * @return true if the current cell has been visited when generating the maze
     */
    public boolean isVisited(){
        return this.visited;
    }

    /**
     * @param visited boolean value if this cell has been visited when generating the maze
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Given the Graphics object the cell is drawn depending on which walls it possesses
     * @param g Graphics object
     */
    public void draw(Graphics g) {

        int x2 = x * maze.getPixelSize();
        int y2 = y * maze.getPixelSize();

        if (isTrailcell) {
            g.setColor(Color.RED);
            g.fillRect(x2, y2, maze.getPixelSize(), maze.getPixelSize());
        }

//        if (isStartCell) {
//            g.setColor(Color.YELLOW);
//            g.fillRect(x2, y2, Maze.getPixelSize(), Maze.getPixelSize());
//        }
//
//        if (isDestinationCell) {
//            g.setColor(Color.GREEN);
//            g.fillRect(x2, y2, Maze.getPixelSize(), Maze.getPixelSize());
//        }

        // top 0, right 1, bottom 2, left 3

        g.setColor(Color.BLACK);
        if (walls[0] && !isStartCell) {
            g.drawLine(x2, y2, x2+ maze.getPixelSize(), y2);
        }
        if (walls[1]) {
            g.drawLine(x2+ maze.getPixelSize(), y2, x2+ maze.getPixelSize(), y2+ maze.getPixelSize());
        }
        if (walls[2] && !isDestinationCell) {
            g.drawLine(x2+ maze.getPixelSize(), y2+ maze.getPixelSize(), x2, y2+ maze.getPixelSize());
        }
        if (walls[3]) {
            g.drawLine(x2, y2+ maze.getPixelSize(), x2, y2);
        }
    }

    /**
     * @param index of which wall of the cell
     * @param val boolean value if the wall exists or not
     */
    public void setWall(int index, boolean val) {
        this.walls[index] = val;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object obj){

        if (obj == null){
            return false;
        }

        Cell otherCell = (Cell) obj;

        if (otherCell.x != this.x){
            return false;
        }

        if (otherCell.y != this.y){
            return false;
        }

        return true;
    }

}