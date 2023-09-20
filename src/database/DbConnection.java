package database;

import mazeGenerator.BasicMaze;
import mazeGenerator.ChildrenMaze;
import mazeGenerator.Maze;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Database made in MariaDB for storing and retrieving maze data
 * +-------------------+--------------+------+-----+---------+----------------+
 * | Field             | Type         | Null | Key | Default | Extra          |
 * +-------------------+--------------+------+-----+---------+----------------+
 * | MazeID            | int(11)      | NO   | PRI | NULL    | auto_increment |
 * | MazeName          | varchar(255) | YES  |     | NULL    |                |
 * | FirstName         | varchar(255) | YES  |     | NULL    |                |
 * | LastName          | varchar(255) | YES  |     | NULL    |                |
 * | MazeCreated       | datetime     | YES  |     | NULL    |                |
 * | LastEdited        | datetime     | YES  |     | NULL    |                |
 * | Maze              | longblob     | YES  |     | NULL    |                |
 * | MazeImage         | longblob     | YES  |     | NULL    |                |
 * | MazeImageSolution | longblob     | YES  |     | NULL    |                |
 * | IsBasicMaze       | tinyint(1)   | YES  |     | NULL    |                |
 * +-------------------+--------------+------+-----+---------+----------------+
 */
public class DbConnection {
    private Connection connection;

    /**
     * Constructor used for this project where the database variables are known
     * @throws SQLException
     */
    public DbConnection() throws SQLException {
        String url = "jdbc:mariadb://localhost:3307/MazesDB";
        String username = "root";
        String password = "Wasd";
        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * Constructor used for when database variables are unknown
     * @param url
     * @param username
     * @param password
     * @throws SQLException
     */
    public DbConnection(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public byte[] convertToBytes(Maze maze) throws IOException {
        ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
        ObjectOutputStream objectStream=new ObjectOutputStream(byteStream);
        objectStream.writeObject(maze);
        return byteStream.toByteArray();
    }

    public void insertMaze(Maze maze) throws SQLException, IOException {

        String isBasicMaze = "TRUE";

        if (maze.getClass() != BasicMaze.class) {
            isBasicMaze = "FALSE";
        }

        String SQLStatement=String.format(
                "INSERT INTO mazes(MazeName,FirstName,LastName,MazeCreated,Maze,IsBasicMaze)VALUES('%s','%s','%s','%s',?,%s);",
                maze.getMazeName(),maze.getAuthorFirstName(),maze.getAuthorLastName(),maze.getDateTimeCreated(),isBasicMaze
        );

        PreparedStatement insert = connection.prepareStatement(SQLStatement);

        byte[] mazeInBytes = convertToBytes(maze);

        insert.setBinaryStream(1,new ByteArrayInputStream(mazeInBytes),mazeInBytes.length);
        insert.execute();
    }

    public void updateMaze(Maze maze) throws SQLException,IOException{
        System.out.println("Updating db");
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currDate =dtf.format(LocalDateTime.now());

        maze.setDateTimeEdited(currDate);

        String SQLStatement = String.format(
                "UPDATE mazes SET LastEdited='%s',Maze=? WHERE FirstName ='%s' AND LastName='%s' AND MazeName='%s';",
                currDate,maze.getAuthorFirstName(),maze.getAuthorLastName(),maze.getMazeName());

        PreparedStatement insert=connection.prepareStatement(SQLStatement);

        byte[]mazeInBytes=convertToBytes(maze);

        insert.setBinaryStream(1,new ByteArrayInputStream(mazeInBytes),mazeInBytes.length);
        insert.execute();
    }

    public Maze retrieveMaze(String firstName, String lastName, String mazeName) throws SQLException, IOException, ClassNotFoundException {

        String SQL = "SELECT * FROM mazes WHERE FirstName='%s' AND LastName='%s' AND MazeName='%s';";
        SQL = String.format(SQL, firstName, lastName, mazeName);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(SQL);
        while (result.next()) {
            Blob blob = result.getBlob(7);
            byte[] data = blob.getBytes(1, (int) blob.length());

            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);

            int mazeType = Integer.parseInt(result.getNString(10));
            Maze retrievedMaze = null;

            if (mazeType == 1) {
                retrievedMaze = (BasicMaze)objectStream.readObject();
            } else {
                retrievedMaze = (ChildrenMaze)objectStream.readObject();
            }

            System.out.println(retrievedMaze.getMazeName());
            System.out.println(retrievedMaze.getColNum());
            System.out.println(retrievedMaze.getRowNum());

            return retrievedMaze;
        }

        return null;
    }

    public void deleteMaze(Maze maze) throws SQLException {
        String SQLstatement = String.format(
                "DELETE FROM mazes WHERE MazeName='%s' AND FirstName='%s' AND LastName='%s';",
                maze.getMazeName(), maze.getAuthorFirstName(), maze.getAuthorLastName());
        Statement statement = connection.createStatement();
        statement.execute(SQLstatement);
    }

    public boolean mazeExists(String firstName, String lastName, String mazeName) throws SQLException, IOException, ClassNotFoundException {
        return retrieveMaze(firstName, lastName, mazeName) != null;
    }

    public Set<Maze> getAllMazes() throws SQLException, IOException, ClassNotFoundException {
        Set<Maze> mazes = new HashSet<>();

        String sqlStatement = "SELECT * FROM mazes;";

        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(sqlStatement);

        while(results.next()) {
            Blob blob = results.getBlob(7);
            byte[] data = blob.getBytes(1, (int) blob.length());

            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);

            int mazeType = Integer.parseInt(results.getNString(10));

            Maze retrievedMaze = null;

            if (mazeType == 1) {
                retrievedMaze = (BasicMaze)objectStream.readObject();
            } else {
                retrievedMaze = (ChildrenMaze)objectStream.readObject();
            }

            mazes.add(retrievedMaze);
        }

        return mazes;
    }
}
