package mazeGenerator;

import gui.MazePanel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class contains information about the maze fields(mazeName, author, creationDate, lastEditDate,
 * rows and columns of maze, mazeMap(2 dimensions array), solution and difficulty)
 * This class contains method such as auto generating maze, manual generating maze and manipulate maze
 * by removing and adding wall as well as adding and relocating images
 */
public abstract class Maze implements Serializable {
    // Fields
    /**
     * mazeName
     */
    private String mazeName;
    /**
     * total row of maze
     */
    private int rowCellNum;
    /**
     * total column of maze
     */
    private int columnCellNum;
    /**
     * container of all values for 2 dimension maze creation
     */
    private int[][] mazeMap;
    /**
     * check if a user want to auto-generate a maze
     */
    private boolean isAuto;
    /**
     * width data that will always determine the size of the maze in the GUI panel
     */
    public static int WIDTH = 400;
    /**
     * height data that will always determine the size of the maze in the GUI panel
     */
    public static int HEIGHT = WIDTH;
    /**
     * list containing all the cells that make up the grid
     */
    private List<Cell> grid;

    private Cell start;

    private Cell destination;

    private List<Cell> solutionTrail;

    private boolean isEdited;

    private boolean isSolvable;

    private int deadEndsCount;

    private Set<Cell> deadEnds;

    private String authorFirstName;

    private String authorLastName;

    private MazePanel mazePanel;

    private Solution solution;

    private String dateTimeCreated;

    private String dateTimeEdited;

    // Methods
    /**
     * Instantiate a maze object using information typed by a user
     * @param mazeName name of maze
     * @param firstName first name of author
     * @param lastName last name of author
     * @param rowCellNum total row of maze
     * @param columnCellNum total column of maze
     * @param isAuto set true if a user want an auto-generated maze, otherwise false
     */
    public Maze(String mazeName, String firstName, String lastName, int rowCellNum, int columnCellNum,
                boolean isAuto, String dateTimeCreated, String dateTimeEdited) throws MazeGeneratorException{
        if (rowCellNum < 3 ) {
            throw new MazeGeneratorException("The number of row cannot be less than 3");
        }

        if (columnCellNum < 3 ) {
            throw new MazeGeneratorException("The number of column cannot be less than 3");
        }

        mazeName = mazeName.replace('\'', ' ');

        this.mazeName = mazeName;
        this.rowCellNum = rowCellNum;
        this.columnCellNum = columnCellNum;
        this.mazeMap = new int[rowCellNum][columnCellNum];
        this.isAuto = isAuto;
        this.isEdited = false;
        this.isSolvable = false;
        this.solutionTrail = new ArrayList<>();
        this.grid = new ArrayList<>();
        this.deadEnds = new HashSet<>();
        this.deadEndsCount = 0;
        this.authorFirstName = firstName;
        this.authorLastName = lastName;
        this.dateTimeCreated = dateTimeCreated;
        this.dateTimeEdited = dateTimeEdited;

        for (int x = 0; x < columnCellNum; x++) {
            for (int y = 0; y < rowCellNum; y++) {
                grid.add(new Cell(x, y, this));
            }
        }

        this.mazePanel = new MazePanel(grid, rowCellNum, columnCellNum, this);

        /**
         *
         *  if grid is 5 x 5
         *  starting cell:= (0, 4)
         *  destination cell := (4, 0)
         *
         */
        Cell startCell = new Cell(0, 0, this);
        startCell = grid.get(grid.indexOf(startCell));
        startCell.setStartCell(true);
        this.start = startCell;

        Cell destinationCell = new Cell(columnCellNum - 1, rowCellNum - 1, this);
        System.out.println(destinationCell);

        System.out.println("Every cell in grid");
        for (Cell cell : grid) {
            System.out.println(cell);
        }

        destinationCell = grid.get(grid.indexOf(destinationCell));
        destinationCell.setDestinationCell(true);
        this.destination = destinationCell;
    }

    public int getRowNum() {
        return this.rowCellNum;
    }

    public int getColNum() {
        return this.columnCellNum;
    }

    public boolean isAuto() {
        return this.isAuto;
    }

    public String getDateTimeCreated() {
        return this.dateTimeCreated;
    }

    public void setDateTimeEdited(String dateTime) {
        this.dateTimeEdited = dateTime;
    }

    public String getDateTimeEdited() {
        return this.dateTimeEdited;
    }

    /**
     * Gets author's firstName
     * @return authorFirstName
     */
    public String getAuthorFirstName() {
        return this.authorFirstName;
    }
    /**
     * Gets author's firstName
     * @return authorLastName
     */
    public String getAuthorLastName() {
        return this.authorLastName;
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }

    public void solveMaze() {
        solution = new Solution(this);
    }

    // randomly generate values and store them into int[][] array

    /**
     * @return grid of cells that make up this maze
     */
    public List<Cell> getGrid(){
        return grid;
    }

    /**
     * Generate mazeMap automatically if isAuto is true,
     * otherwise generate blank mazeMap for a user to start from square one
     */
    public void generateMazeMap() {
        while(!this.isSolvable) {
            for(Cell cell : grid) {
                Cell currentCell = grid.get(grid.indexOf(cell));
                currentCell.setVisited(false);
                currentCell.setWall(0, true);
                currentCell.setWall(1, true);
                currentCell.setWall(2, true);
                currentCell.setWall(3, true);
                currentCell.setVisited(false);
                currentCell.setTrailCell(false);

                Cell startCell = new Cell(0, 0, this);
                startCell = grid.get(grid.indexOf(startCell));
                startCell.setStartCell(true);
                this.start = startCell;

                Cell destinationCell = new Cell(columnCellNum - 1, rowCellNum - 1, this);
                destinationCell = grid.get(grid.indexOf(destinationCell));
                destinationCell.setDestinationCell(true);
                this.destination = destinationCell;
            }

            if (isAuto) {
                autoGenerate(mazePanel, this);
            } else {
                manualGenerate(mazePanel, this);
            }

            solveMaze();
            clearSolutionDisplay();
        }
    }

    public void setSolvable(boolean val) {
        if (!solutionTrail.isEmpty()) {
            solutionTrail.clear();
        }
        this.isSolvable = val;
    }

    public boolean isSolvable() {return isSolvable;}

    public void setStart(Cell cell) {
        this.start = cell;
    }

    public Cell getStartCell(){
        return this.start;
    }

    public void setDestination(Cell cell) {
        this.destination = cell;
    }

    public Cell getDestinationCell(){
        return this.destination;
    }

    public List<Cell> getSolutionTrail() { return this.solutionTrail; }

    public void setEdited(boolean val) {
        this.isEdited = val;
    }

    public boolean isEdited() { return isEdited; }

    public void setSolutionTrail(List<Cell> _solutionTrail) {
        Cell solutionCell;

        Set<Cell> __solutionTrail = new HashSet<>(_solutionTrail);

        for(Cell cell : __solutionTrail) {
            solutionCell = grid.get(grid.indexOf(cell));
            solutionCell.setTrailCell(true);
            solutionTrail.add(solutionCell);
        }

    }

    public void setDeadEnds() {
        for(Cell cell: getGrid()) {
            if (cell.has3Walls() && !cell.isStartCell() && !cell.isDestinationCell()){
                deadEnds.add(cell);
            }
        }

        deadEndsCount = deadEnds.size();
    }

    public int getDeadEndsCount() {
        if (!deadEnds.isEmpty()) {
            return deadEndsCount;
        }

        return 0;
    }

    public Set<Cell> getDeadEnds() {
        return deadEnds;
    }

    public void clearSolutionDisplay() {
        for (Cell cell : solutionTrail) {
            grid.get(grid.indexOf(cell)).setTrailCell(false);
        }
    }

    public void setSolutionDisplay() {
        for (Cell cell : solutionTrail) {
            grid.get(grid.indexOf(cell)).setTrailCell(true);
        }
    }

    /**
     * Auto-generates a maze with randomly populated values in mazeMap array
     */
    protected abstract void autoGenerate(MazePanel mazeGrid, Maze maze);

    /**
     * Generates a blank maze without wall so that a user builds a maze from square one
     */
    protected abstract void manualGenerate(MazePanel mazeGrid, Maze maze);

    /**
     * Adds a wall in an existing maze
     */
    public void addWall() {
        //...
    }

    /**
     * Removes a wall from an existing maze
     */
    public void removeWall() {
        //...
    }

    /**
     * Saves changes of existing maze
     */
    public void saveChanges() {
        //...
        // update lastEditedDateAndTime
    }

    /**
     * Returnss mazeName
     * @return mazeName
     */
    public String getMazeName() {
        return mazeName;
    }

    /**
     * Returns pixelSize value
     * @return size of pixel
     */
    public int getPixelSize() {
        if (rowCellNum * columnCellNum >0 && rowCellNum * columnCellNum <=100) {return 50; }
        else if (rowCellNum * columnCellNum >100 && rowCellNum * columnCellNum <=400) {return 40; }
        else if (rowCellNum * columnCellNum >400 && rowCellNum * columnCellNum <=1000) {return 30;}
        else if (rowCellNum * columnCellNum >1000 && rowCellNum * columnCellNum <=2000) {return 20;}
        else if (rowCellNum * columnCellNum >2000 && rowCellNum * columnCellNum <=3000) {return 16;}
        else if (rowCellNum * columnCellNum >3000 && rowCellNum * columnCellNum <=4000) {return 12;}
        else {return 8;}
    }

    /**
     * Returns solution object
     * @return solution object
     */
//    public Solution getSolution() {
//        return solution;
//    }

}
