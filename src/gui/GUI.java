package gui;

import database.DbConnection;
import mazeGenerator.Maze;
import mazeGenerator.MazeGeneratorException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

/**
 * Main menu for the program. Buttons for creating mazes, editing mazes, exporting and searching are available here.
 */
public class GUI extends JFrame {
    /**
     * An array of strings that contain the title for each column.
     * Note from kmin983: the type (datetime vs string) for created and last edited is undetermined until the creation of actual database.
     *
     * "Export Maze" - Bool as placeholder for the checkbox to include the selected "maze" in export. Default is unchecked.
     * "Export Solution" - Bool as placeholder for the checkbox to include the selected "maze solution" in export. Default is unchecked.
     * "Maze Name" - String to display maze title
     * "Author First Name" - String to display first name of the maze's author.
     * "Author Last Name" - String to display last name of the maze's author.
     * "Created" - Represents when a maze is created. "DD/MM/YY hh:mm:ss" datetime value represented as string.
     * "Last Edited" - Represents when a maze is last edited. "DD/MM/YY hh:mm:ss" datetime value represented as string.
     * "Export Individually" - String value for the individual export button name.
     */
    private String mazeColumnNames[] = {"Export Maze", "Export Solution",  "Maze Name", "Author First Name", "Author Last Name", "Created", "Last Edited", "Export Individually"};

    /**
     * An array of arrays containing objects.
     * Used as temporary data for main GUI table representing maze details.
     * The data in each field represents are descripted in {@link GUI#mazeColumnNames} field.
     */
    private Object mazeTempData[][];

    /**
     * A class to enable functionality of a button click inside JTable.
     * Externally sourced. More information from {@link ButtonColumn}
     */
    private ButtonColumn exportColumn;

    /**
     * The center panel of the GUI.
     * The only purpose is to hold mainContentPanel and to keep its styling together.
     */
    private JPanel centerPanel;

    /**
     * The main content panel to encompasses most of the GUI contents
     * including the Table, create maze button, remove maze button and export button.
     */
    private JPanel mainContentPanel;

    /**
     * The panel that includes a search bar and the button.
     */
    private JPanel searchPanel;

    /**
     * The search button.
     * If clicked, the maze list in the {@link GUI#mazeTable} is sorted according to the text input inside {@link GUI#searchBox}.
     */
    private JButton searchButton;

    /**
     * The search box.
     * The text input from the user is used to sort the table {@link GUI#mazeTable} accordingly.
     */
    private JTextField searchBox;

    /**
     * The main table to display the list of mazes with their information.
     * The information can be found from {@link GUI#mazeColumnNames} and {@link GUI#mazeTempData}
     */
    private JTable mazeTable;

    /**
     * The button to edit maze.
     * User selects a particular row that represents a maze inside {@link GUI#mazeTable} followed by clicking this button,
     * which displays a new page to allow users for editing that maze.
     */
    private JButton editMaze;

    /**
     * The button to create a maze.
     * By selecting this button, user is guided to {@link CreateMazeMenuGUI}
     */
    private JButton createMazeButton;

    /**
     * The button to remove a maze from the table.
     */
    private JButton removeMazeButton;

    /**
     * The panel to hold {@link GUI#createMazeButton} and {@link GUI#removeMazeButton}
     */
    private JPanel buttonPanel;

    /**
     * The button that exports rows (mazes) seleccted from the main table {@link GUI#mazeTable}
     */
    private JButton exportButton;

    /**
     * Main scroll panel to display rows within main table {@link GUI#mazeTable}
     */
    private JScrollPane scrollPane;

    /**
     * This constructor creates the main menu GUI component.
     */
    public GUI() throws SQLException, IOException, ClassNotFoundException, MazeGeneratorException {

        // Catch exceptions within GUI.
        super("Maze Generator");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                UnsupportedLookAndFeelException |
                InstantiationException|
                IllegalAccessException ignored) {

        }

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1400, 700));
        this.setLayout(new BorderLayout());

        // Creating search bar
        searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        searchPanel.setPreferredSize(new Dimension(0,80));

        // Customizing search button
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(130, 50));

        // Customizing TextField
        searchBox = new JTextField(30);
        searchBox.setPreferredSize(new Dimension(0, 50));
        searchBox.setFont(new Font("SansSerif", Font.PLAIN, 20));

        searchPanel.add(searchBox);
        searchPanel.add(searchButton);

        // Creating main Panel
        centerPanel = new JPanel();
        mainContentPanel = new JPanel();

        // Adding components to the mainPanel
        mainContentPanel.setPreferredSize(new Dimension(1200,450));
        Action newAction = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exported");
            }
        };


        /*
         *
         * Found how to disable editing the cell at www.tutorialspoint.com
         * https://www.tutorialspoint.com/how-can-we-disable-the-cell-editing-inside-a-jtable-in-java
         *
         * */
        final DefaultTableModel model = new DefaultTableModel(null, mazeColumnNames);
        DbConnection conn = new DbConnection();
        Set<Maze> mazes = conn.getAllMazes();

        for(Maze maze : mazes) {
            model.insertRow(0, new Object[] {false, false,
                    maze.getMazeName(),
                    maze.getAuthorFirstName(),
                    maze.getAuthorLastName(),
                    maze.getDateTimeCreated(),
                    maze.getDateTimeEdited(),
                    "Open"
            });
        }
        mazeTable = new JTable(model) {
            private static final long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    case 1:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 0 || column == 1 || column == 7);
            }
//
//            public boolean editCellAt(int row, int column, java.util.EventObject e) {
//                return false;
//            }
        };

        mazeTable.setAutoCreateRowSorter(true);

        exportColumn = new ButtonColumn(mazeTable, newAction, 7, model, this);

        mazeTable.setRowHeight(30);
        mazeTable.setFont(new Font("SansSerif", Font.PLAIN, 13));

        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Button to edit maze
        editMaze = new JButton("Edit Maze");
        editMaze.setPreferredSize(new Dimension(200, 50));
        editMaze.setEnabled(false);

        // Button to create the maze
        createMazeButton = new JButton("Create Maze");
        createMazeButton.addActionListener(e -> new CreateMazeMenuGUI(this));
        createMazeButton.setPreferredSize(new Dimension(200, 50));

        removeMazeButton = new JButton("Remove Maze");
        removeMazeButton.setPreferredSize(new Dimension(200, 50));

        removeMazeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // check for selected row first
                if (mazeTable.getSelectedRow() != -1) {
                    // remove selected row from the model
                    model.removeRow(mazeTable.getSelectedRow());
                }
            }
        });

        // Put create and remove maze buttons to panel
        buttonPanel = new JPanel();
        buttonPanel.add(createMazeButton);
        buttonPanel.add(removeMazeButton);
        exportButton = new JButton("Export Selected");
        exportButton.setPreferredSize(new Dimension(200, 50));
        mainContentPanel.add(buttonPanel);

        // Putting the table inside a JScrollPane
        scrollPane = new JScrollPane(mazeTable);

        mainContentPanel.add(scrollPane);
        centerPanel.add(mainContentPanel);
        mainContentPanel.add(exportButton);
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bring everything together to main GUI
        this.add(searchPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}