package mazeGenerator;

import gui.MazePanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * This class manages a list of maze by creating, searching, sorting displaying and deleting
 * Field ArrayList<maze> mazeList maze will be stored in it
 */
public class MazeGenerator implements Serializable {
    // Fields
    /**
     * Store populated maze into mazeList, it can be replaced by database when database is setup
     */
    private ArrayList<Maze> mazeList;
    /**
     * Placeholder list for generating the grid
     */
    private final Stack<Cell> cells = new Stack<Cell>();
    /**
     * Storing cells into list that comprises the maze
     */
    private final List<Cell> grid;
    /**
     * List containing all the dead ends in the grid
     */
    private final List<Cell> deadEnds;
    /**
     * Placeholder variable for generating the maze
     */
    boolean isDeadEnd = false;
    /**
     * Maze object that contains all the relevant information to create the maze
     */
    private Maze maze;

    /**
     * This constructor make a new mazeList to store populated mazes
     */
//    public MazeGenerator() {
//        this.mazeList = new ArrayList<Maze>();
//    }

    /**
     * This constructor use the existing mazeList to manipulate maze
     * @param grid ArrayList<Cell> list of cells that make up the grid
     * @param panel panel the Swing panel component
     * @param maze maze object
     */
    public MazeGenerator(List<Cell> grid, MazePanel panel, Maze maze) {
        this.grid = grid;
        this.maze = maze;
        deadEnds = new ArrayList<>();
        Cell currentCell;

        for (Cell cell : grid) {
            //check if any cells are in cells stack first,
            //   if there are any cells carve them first
            //   then next cell in iteration
            if (!cells.isEmpty()) {
                int stackSize = cells.size();
                for (int i = 0 ; i < stackSize ; i++) {
                    Cell __cell = cells.lastElement();
                    carve(__cell);
                    cells.pop();
                }
            }

            currentCell = grid.get(grid.indexOf(cell));
            if (!currentCell.isVisited()){
                carve(grid.get(grid.indexOf(cell)));
            } else {
                continue;
            }
        }

        panel.repaint();
    }

    /**
     * This constructor use the existing mazeList to manipulate maze
     * @param mazeList ArrayList<Maze>
     */
//    public MazeGenerator(ArrayList<Maze> mazeList) {
//        this.mazeList = mazeList;
//    }

    // Methods
    /**
     * Returns a mazeList
     * @return mazeList ArrayList<Maze>
     */
    public ArrayList<Maze> getMazeList() {
        return mazeList;
    }


    /**
     * Displays all existing members of each Maze
     */
    // should be sorted with default sorting method
    public void displayMazeList() {
        //...
        for (Maze maze : this.mazeList) {
            System.out.println("Display a maze");
        }
    }

    /**
     * Displays selected mazes by searching method
     * @param mazeList selected maze list by maze name or author search method
     */
    // should work with search methods
    public void displayMazeList(ArrayList<Maze> mazeList) {
        //...
        for (Maze maze : mazeList) {
            System.out.println("Display a maze");
        }
    }

    /**
     * Searches for a maze matched with given mazeName in mazeList
     * Return maze list if matched maze found add to maze list
     * @param mazeName selected maze name
     * @return array list of mazes which has the same maze name
     */
    // Think about maze which has the same name or if we not allow the same maze name
    public ArrayList<Maze> searchMazeByName(String mazeName) {
        //...
        ArrayList<Maze> searchResult = new ArrayList<>();
        /*
        if (this.mazeList == null) {
            return null;
        }
        for (Maze maze : mazeList) {
            if (maze.getMazeName() == mazeName){
                searchResult.add(maze);
            }
        }
        */
//        if(searchResult.size() == 0) {
//            return null;
//        }
        return null;

    }

    /**
     * Searches for a maze matched with firstName and lastName in mazeList
     * Return maze list if matched maze found otherwise return null
     * @param firstName author's firstName
     * @param lastName author's lastName
     * @return array list of created by the same author
     */
    // has to think about Author who has multiple maze
    public ArrayList<Maze> searchMazeByAuthor(String firstName, String lastName) {
        //...
        ArrayList<Maze> searchResult = new ArrayList<>();
        /*
        if (this.mazeList == null) {

            return null;
        }
        for (Maze maze : mazeList) {
            if (maze.getAuthor().getFirstName() == firstName) {
                if (maze.getAuthor().getLastName() == lastName) {
                    searchResult.add(maze);
                }
            }
        }
        */
//        if(searchResult.size() == 0) {
//            return null;
//        }
        return null;
    }

    /**
     * Delete a selected maze
     * @param maze maze that will be deleted
     */
    public void deleteMaze(Maze maze) {
        //...
        // how can we select a maze from mazeList
        // use maze name or maze Author?
        // need a help from SWING gui action
    }

    /**
     * Sorts out existing mazeList by mazeName (default)
     * compare eachMaze's name and sort them alphabetical order
     * @param mazeList existing mazeList
     * pre-condition mazeList should not be empty
     */
    public void sortMazeListByMazeName(ArrayList<Maze> mazeList) {
        //...
        /* for (int i = 0; 0 < mazeList.size(); i++) {
            compare all maze using some sorting algorithm
            mazeList[i].getMazeName();
        }
        */
        // compare eachMaze name and sort them alphabetical order
    }

    /**
     * Sorts out existing mazeList by Author's first name and last name
     * apply first name then check last name
     * @param mazeList existing mazeList
     * pre-condition mazeList should not be empty
     */
    // Think about the same author has multiple mazes
    public void sortMazeListByAuthor(ArrayList<Maze> mazeList) {
        //...
    }

    /**
     * Sorts out existing mazeList by Date and time
     * apply date and time respectively
     * @param mazeList existing mazeList
     * pre-condition mazeList should not be empty
     */
    public void SortMazeListByDateTime(ArrayList<Maze> mazeList) {
        //...
    }

    /**
     * @param currentCell in the current step of the DFS maze generation
     * @param nextCell in the current step of the DFS maze generation
     */
    public void removeWall(Cell currentCell, Cell nextCell){
        if (currentCell.x - nextCell.x == -1) {
            // cell is to the right
            currentCell.setWall(1, false);
            nextCell.setWall(3, false);
        }

        if (currentCell.x - nextCell.x == 1) {
            // cell is to the left
            currentCell.setWall(3, false);
            nextCell.setWall(1, false);
        }

        if (currentCell.y - nextCell.y == -1) {
            // cell is to above
            currentCell.setWall(2, false);
            nextCell.setWall(0, false);
        }

        if (currentCell.y - nextCell.y == 1) {
            // cell is to above
            currentCell.setWall(0, false);
            nextCell.setWall(2, false);
        }
    }

    /**
     * @param currentCell current Cell in the current step of the DFS maze generation
     */
    private void carve(Cell currentCell) {
        Random rand = new Random();
        List<Cell> unvisitedNeighbours = getUnvisitedNeighbour(currentCell);
        currentCell = grid.get(grid.indexOf(currentCell));
        currentCell.setVisited(true);

        if (!unvisitedNeighbours.isEmpty()) {

            if (isDeadEnd) {
                isDeadEnd = false;
            }

            Cell nextCell = unvisitedNeighbours.get(rand.nextInt(unvisitedNeighbours.size()));
            nextCell = grid.get(grid.indexOf(nextCell));

            removeWall(currentCell, nextCell);
            currentCell = nextCell;

            // if currentCell has more than 1 unvisited neighbour
            //    if it does, add that unvisited neighbour that isn't nextCell into cells stack
            cells.add(currentCell);

            carve(currentCell);

        } else {
            // haven't implemented this yet, but everytime the code reaches here it is a dead end or the solution??
            if (!isDeadEnd){
                isDeadEnd = true;
                deadEnds.add(currentCell);
            }
        }

        return;
    }

    /**
     * @param cell the cell for which the unvisited neighbours will be searched for
     * @return Cell chosen by random if there are multiple, that has been unvisited
     */
    public List<Cell> getUnvisitedNeighbour(Cell cell) {

        List<Cell> unvisitedNeighbours = new ArrayList<Cell>();

        Cell topCell = new Cell(cell.x, cell.y - 1, maze);
        // Check if cell exists on top of current cell
        if (grid.contains(topCell)) {
            if (!grid.get(grid.indexOf(topCell)).isVisited()) {
                unvisitedNeighbours.add(topCell);
            }
        }

        Cell bottomCell = new Cell(cell.x, cell.y + 1, maze);
        // Check if cell exists on bottom of current cell
        if (grid.contains(bottomCell)) {
            if (!grid.get(grid.indexOf(bottomCell)).isVisited()) {
                unvisitedNeighbours.add(bottomCell);
            }
        }

        Cell leftCell = new Cell(cell.x - 1, cell.y, maze);
        // Check if cell exists on left of current cell
        if (grid.contains(leftCell)) {
            if (!grid.get(grid.indexOf(leftCell)).isVisited()) {
                unvisitedNeighbours.add(leftCell);
            }
        }

        Cell rightCell = new Cell(cell.x + 1, cell.y, maze);
        // Check if cell exists on right of current cell
        if (grid.contains(rightCell)) {
            if (!grid.get(grid.indexOf(rightCell)).isVisited()) {
                unvisitedNeighbours.add(rightCell);
            }
        }

        return unvisitedNeighbours;

    }
}