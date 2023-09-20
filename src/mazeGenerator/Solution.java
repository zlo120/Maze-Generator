package mazeGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * This class produces a maze solution and check if a maze is solvable
 */
public class Solution implements Serializable {

    Maze maze;
    List<Cell> grid;
    Cell startCell;
    Cell destinationCell;

    List<Cell> visitedCells = new ArrayList<>();
    Stack<Cell> cells = new Stack<>();

    Stack<Cell> solutionTrail;

    List<List<Cell>> solutionsList;

    List<Cell> solutionCells;

    List<Cell> deadEnds;

    Cell currentCell;
    Cell nextCell;

    boolean deadEnd = false;
    boolean solutionReached = false;

    public Solution(Maze maze) {
        this.maze = maze;
        this.grid = new ArrayList<>(maze.getGrid());
        this.startCell = maze.getStartCell();
        this.destinationCell = maze.getDestinationCell();
        this.solutionTrail = new Stack<>();
        this.solutionsList = new ArrayList<>();
        this.deadEnds = new ArrayList<>();
        this.solutionCells = new ArrayList<>();

        for (Cell cell : grid) {
            cell = grid.get(grid.indexOf(cell));
            cell.setVisited(false);
        }

        for (Cell cell : grid){
            cell = grid.get(grid.indexOf(cell));
            cell.setTrailCell(false);
        }
        this.currentCell = grid.get(0);

        carve();

        int deadEndsCount = 0;
        int previousDeadEndSize = 0;

        while (true) {

            List<Cell> iteratedCells = new ArrayList<>(cells);

            for (Cell cell : iteratedCells){

                if (!cells.isEmpty()) {
                    int stackSize = cells.size();
                    System.out.println("Size of stackSize: " + stackSize);
                    for (int i = 0; i < stackSize ; i++) {

                        if (currentCell == null) {
                            break;
                        }

                        carve();
                    }
                }
            }

            if (cells.isEmpty() && solutionTrail.isEmpty()) {

                for (Cell cell : grid) {
                    cell = grid.get(grid.indexOf(cell));
                    cell.setVisited(false);
                }

                System.out.println("Dead ends size: " + deadEnds.size());
                System.out.println("Dead ends size:  " + deadEnds.size() + "  ::  " + "previousDeadEndSize: " + previousDeadEndSize);
                // Check if the deadEnds aren't remaining constant
                if (deadEnds.size() == previousDeadEndSize && deadEndsCount > maze.getGrid().size()) {
                    cells.clear();
                    maze.setSolvable(false);
                    maze.setDeadEnds();
                    return;
                } else if (deadEnds.size() == previousDeadEndSize && deadEnds.size() != 0)  {
                    System.out.println("Dead ends count: " + deadEndsCount);
                    deadEndsCount++;
                } else {
                    System.out.println("Dead ends count: " + deadEndsCount);
                    previousDeadEndSize = deadEnds.size();
                    deadEndsCount = 0;
                }

                // Restarting the solutions process
                currentCell = grid.get(0);
                carve();
            } else if (cells.isEmpty()){
                break;
            }

            System.out.println(cells);
        }

        maze.setDeadEnds();
        maze.setSolvable(true);
        maze.setSolutionTrail(solutionTrail);
    }

    public List<Cell> getSolutionTrail() {
        return solutionTrail;
    }

    public List<Cell> getUnvisitedNeighbours(Cell cell) {

        List<Cell> unvisitedNeighbours = new ArrayList<Cell>();

        // top 0, right 1, bottom 2, left 3

        Cell currentCell = grid.get(grid.indexOf(cell));
        boolean[] walls = currentCell.getWalls();

        Cell topCell = new Cell(cell.x, cell.y - 1, maze);
        try {
            topCell = grid.get(grid.indexOf(topCell));
            // Check if cell exists on top of current cell
            if (grid.contains(topCell)) {
                if (!topCell.isVisited()) {
                    if (!walls[0]){

                        unvisitedNeighbours.add(topCell);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            Cell bottomCell = new Cell(cell.x, cell.y + 1, maze);
            bottomCell = grid.get(grid.indexOf(bottomCell));
            // Check if cell exists on bottom of current cell
            if (grid.contains(bottomCell)) {
                currentCell = grid.get(grid.indexOf(bottomCell));
                if (!bottomCell.isVisited()) {
                    if (!walls[2]){

                        unvisitedNeighbours.add(bottomCell);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            Cell leftCell = new Cell(cell.x - 1, cell.y, maze);
            leftCell = grid.get(grid.indexOf(leftCell));
            // Check if cell exists on left of current cell
            if (grid.contains(leftCell)) {
                currentCell = grid.get(grid.indexOf(leftCell));
                if (!leftCell.isVisited()) {
                    if (!walls[3]){

                        unvisitedNeighbours.add(leftCell);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            Cell rightCell = new Cell(cell.x + 1, cell.y, maze);
            rightCell = grid.get(grid.indexOf(rightCell));
            // Check if cell exists on right of current cell
            if (grid.contains(rightCell)) {
                currentCell = grid.get(grid.indexOf(rightCell));
                if (!rightCell.isVisited()) {
                    if (!walls[1]){
                        unvisitedNeighbours.add(rightCell);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

//        System.out.println("\nRESULTS OF THIS FUNCTION\n");
//
//        if (unvisitedNeighbours.isEmpty()){
//            System.out.println("No unvisited neighbours");
//        } else {
//            System.out.println("Current cell: " + cell);
//            for (Cell _cell : unvisitedNeighbours){
//                System.out.println("Legal unvisited neighbour" + _cell);
//            }
//        }

        return unvisitedNeighbours;
    }

    /**
     * Generates a maze solution
     */
    private void carve() {
        Random rand = new Random();
        List<Cell> unvisitedNeighbours = getUnvisitedNeighbours(currentCell);
        currentCell = grid.get(grid.indexOf(currentCell));
        currentCell.setVisited(true);
        nextCell = null;

        if (!unvisitedNeighbours.isEmpty()) {
            if (currentCell == maze.getDestinationCell()) {

                System.out.println("Lets look at the trail!");
                solutionTrail.add(currentCell);
                for (Cell cell : solutionTrail) {
                    System.out.println(cell);
                }

                System.out.println("Reached the solution!");
                solutionsList.add(solutionTrail);

                cells.clear();
                return;
            }

            cells.add(currentCell);

            nextCell = unvisitedNeighbours.get(rand.nextInt(unvisitedNeighbours.size()));
            nextCell = grid.get(grid.indexOf(nextCell));

            solutionTrail.add(currentCell);

            currentCell = nextCell;

        } else {
            // IF THERE ARE NO UNVISITED NEIGHBOUR CELLS

            if (currentCell == maze.getDestinationCell()){

                System.out.println("Lets look at the trail!");
                solutionTrail.add(currentCell);
                for (Cell cell : solutionTrail) {
                    System.out.println(cell);
                }

                System.out.println("Reached the solution!");
                solutionsList.add(solutionTrail);

                cells.clear();

            } else {
                if (!solutionTrail.isEmpty()) {
                    solutionTrail.pop();
                }

                if (!cells.isEmpty()) {
                    cells.pop();
                }


                try {
                    Cell cellInDeadEnds = deadEnds.get(deadEnds.indexOf(currentCell));
                } catch (Exception e) {
                    System.out.println("\n\nIndex out of bounds\n\n");
                    deadEnds.add(currentCell);
                }

                if (!cells.isEmpty()){
                    currentCell = cells.lastElement();
                } else {
                    currentCell = null;
                }
            }
        }
    }

    /**
     * Returns number of cell paths for optimal solution
     * @param maze a maze needed for solution difficulty
     * @return the number of cells passed for solution
     */
    public int calculateNumCellForSolution(Maze maze) {
        //...
        return 0;
    }

    /**
     * Returns number of cell dead end in a maze
     * @param maze a maze needed for solution difficulty
     * @return the number of dead end in a maze
     */
    public int calculateNumCellForDeadEnd(Maze maze) {
        //...
        return 0;
    }

    /**
     * Validates if the maze is solvable
     * @param maze a maze needed for check
     * @return true if a maze is solvable, otherwise false
     */
    public boolean isSolvable(Maze maze) {
        //...
        return true;
    }

    /**
     * Displays the maze solution
     * @param maze a maze needed for display
     * pre-condition maze should be solvable
     */
    public void displaySolution(Maze maze) {
        //...
    }
}