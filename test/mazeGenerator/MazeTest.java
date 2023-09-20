package mazeGenerator;

import gui.MazePanel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
//import mazeGenerator.Maze;

/**
 * Unit test for the Maze class
 */
public class MazeTest {

    /*
     * Define some commonly-used integer constants
     */
    final int pixelsize50 = 50;
    final int pixelsize40 = 40;
    final int pixelsize30 = 30;
    final int pixelsize20 = 20;
    final int pixelsize16 = 16;
    final int pixelsize12 = 12;
    final int pixelsize8 = 8;
    final int negative = -1;
    final int zero = 0;
    final int boundary = 2;
    final int minimum = 3;


    /*
     * Test that the rows and columns values should be greater than or equal to 3
     */
    @Test
    public void NegativeRow() throws MazeGeneratorException {
        assertThrows(MazeGeneratorException.class, () -> {
            Maze maze = new BasicMaze("Awesome Maze", "James",
                    "Deen", negative, minimum, true, "YYYY-MM-DD HH:mm:ss", null);
        });
    }

    @Test
    public void NegativeColumn() throws MazeGeneratorException {
        assertThrows(MazeGeneratorException.class, () -> {
            Maze maze = new BasicMaze("Awesome Maze", "James",
                    "Deen", minimum, negative, true, "YYYY-MM-DD HH:mm:ss", null);
        });
    }

    @Test
    public void ZeroRow() throws MazeGeneratorException {
        assertThrows(MazeGeneratorException.class, () -> {
            Maze maze = new BasicMaze("Awesome Maze", "James",
                    "Deen", zero, minimum, true, "YYYY-MM-DD HH:mm:ss", null);
        });
    }

    @Test
    public void ZeroColumn() throws MazeGeneratorException {
        assertThrows(MazeGeneratorException.class, () -> {
            Maze maze = new BasicMaze("Awesome Maze", "James",
                    "Deen", minimum, zero, true, "YYYY-MM-DD HH:mm:ss", null);
        });
    }

    @Test
    public void BoundaryRow() throws MazeGeneratorException {
        assertThrows(MazeGeneratorException.class, () -> {
            Maze maze = new BasicMaze("Awesome Maze", "James",
                    "Deen", boundary, minimum, true, "YYYY-MM-DD HH:mm:ss", null);
        });
    }

    @Test
    public void BoundaryColumn() throws MazeGeneratorException {
        assertThrows(MazeGeneratorException.class, () -> {
            Maze maze = new BasicMaze("Awesome Maze", "James",
                    "Deen", minimum, boundary, true, "YYYY-MM-DD HH:mm:ss", null);
        });
    }

    /*
     * Test that cell size can be determined depending on the range of the number of cell
     * the minimum number of cell(9) = MinimumRowCellNum(3) * MinimumColumnCellNum(3)
     */

    @Test
    public void NumberOfCell_9() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 3, 3, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize50);
    }

    @Test
    public void NumberOfCell_100() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 10, 10, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize50);
    }

    // Cannot test with boundary number 101 as it is prime number
    @Test
    public void NumberOfCell_102() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 6, 17, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize40);
    }

    @Test
    public void NumberOfCell_400() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 20, 20, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize40);
    }

    // Cannot test with boundary number 401 as it is prime number
    @Test
    public void NumberOfCell_402() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 6, 67, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize30);
    }

    @Test
    public void NumberOfCell_1000() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 25, 40, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize30);
    }

    @Test
    public void NumberOfCell_1001() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 13, 77, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize20);
    }

    @Test
    public void NumberOfCell_2000() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 40, 50, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize20);
    }

    @Test
    public void NumberOfCell_2001() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 23, 87, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize16);
    }

    @Test
    public void NumberOfCell_3000() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 60, 50, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize16);
    }

    // Cannot test with boundary number 3001 as it is prime number
    @Test
    public void NumberOfCell_3002() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 38, 79, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize12);
    }

    @Test
    public void NumberOfCell_4000() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 50, 80, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize12);
    }

    // Cannot test with boundary number 4001 as it is prime number
    @Test
    public void NumberOfCell_4002() throws MazeGeneratorException {
        Maze maze = new BasicMaze("Awesome Maze", "James",
                "Deen", 58, 69, true, "YYYY-MM-DD HH:mm:ss", null);
        assertEquals(maze.getPixelSize(), pixelsize8);
    }

    @Test
    public void isSolvable10() throws MazeGeneratorException {

        for(int i = 0 ; i < 500 ; i++) {
            BasicMaze maze = new BasicMaze("Easy Maze", "Tom", "Hanks",
                    10, 10, true, "YYYY-MM-DD HH:mm:ss", null);
            maze.generateMazeMap();
            assertEquals(maze.isSolvable(), true);
        }

    }

    @Test
    public void isSolvable30() throws MazeGeneratorException {

        for(int i = 0 ; i < 25 ; i++) {
            BasicMaze maze = new BasicMaze("Easy Maze", "Tom", "Hanks",
                    30, 30, true, "YYYY-MM-DD HH:mm:ss", null);
            maze.generateMazeMap();
            assertEquals(maze.isSolvable(), true);
        }

    }

    @Test
    public void isSolvable50() throws MazeGeneratorException {
        BasicMaze maze = new BasicMaze("Easy Maze", "Tom", "Hanks",
                50, 50, true, "YYYY-MM-DD HH:mm:ss", null);
        maze.generateMazeMap();
        assertEquals(maze.isSolvable(), true);
    }
}
