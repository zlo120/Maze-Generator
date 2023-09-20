package mazeGenerator;

import gui.MazePanel;

import java.io.Serializable;

/**
 * This class is for a user who want to create a basic type of maze adding logo image
 * The class is child class of Maze abstract class
 * Implement abstract methods from Maze class
 */
public class BasicMaze extends Maze implements Serializable {
    // Fields
    /**
     * minimum cell number
     */
    private final int MIN_MAZE_CELL_NUM = 10;
    /**
     * maximum cell number
     */
    private final int MAX_MAZE_CELL_NUM = 100;
    /**
     * one logo image
     */
    private Images logo;

    /**
     * Instantiate a maze object using information typed by a user for Basic type maze
     * @param mazeName name of maze
     * @param firstName first name of author
     * @param lastName last name of author
     * @param rowCellNum total row of maze
     * @param columnCellNum total column of maze
     * @param isAuto set true if a user want an auto-generated maze, otherwise false
     */
    // Methods
    public BasicMaze(String mazeName, String firstName, String lastName, int rowCellNum,
                     int columnCellNum, boolean isAuto, String dateTimeCreated, String dateTimeEdited) throws MazeGeneratorException
    {
        super(mazeName, firstName, lastName, rowCellNum, columnCellNum, isAuto, dateTimeCreated, dateTimeEdited);
        this.logo = new Images();
    }

    @Override
    protected void autoGenerate(MazePanel mazeGrid, Maze maze) {
        //...
        // save map into mazeMap array
        new MazeGenerator(super.getGrid(), mazeGrid, maze);
    }

    @Override
    protected void manualGenerate(MazePanel mazeGrid, Maze maze) {
        //...
        // save map into mazeMap array
    }
}
