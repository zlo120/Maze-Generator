package gui;

import mazeGenerator.MazeGeneratorException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * This form gives the user an appropriate fields to specify how they would like to generate their maze.
 */
public class CreateMazeMenuGUI {

    /**
     * The main frame for the entire menu.
     */
    JFrame mainFrame;

    /**
     * The main panel to hold all components in the menu.
     */
    JPanel mainPanel;

    /**
     *  The top side of the menu.
     *  Contains label and user input components for maze type, maze name, first name, last name and maze size.
     */
    JPanel topPanel;

    /**
     * The bottom side of the menu.
     * Contains audio button, manual button, upload logo buttons and create button.
     */
    JPanel bottomPanel;

    /**
     * The panel to hold radiobutton for selecting type of maze (basic vs children)
     */
    JPanel typePanel;

    /**
     * Button group so that only one radio button can be selected from {@link CreateMazeMenuGUI#typePanel}
     */
    ButtonGroup typeGroup;

    /**
     * Allows user to indicate to use a basic maze.
     */
    JRadioButton basicButton;

    /**
     * Allows user to indicate to use a children maze.
     */
    JRadioButton childrenButton;

    /**
     * Records the user input of maze name.
     */
    JTextField mazeNameField;

    /**
     * Panel for user input for sizes.
     */
    JPanel sizePanel;

    /**
     * Contains components for user's choice of number of rows.
     */
    JPanel rowPanel;

    /**
     * Label to indicate rows.
     */
    JLabel rowLabel;

    /**
     * User input for the number of rows for the maze.
     */
    JTextField rowField;

    /**
     * Contains components for user's choice of number of columns.
     */
    JPanel columnPanel;

    /**
     * Label to indicate column.
     */
    JLabel columnLabel;

    /**
     * User input for the number of columns for the maze.
     */
    JTextField colField;
    /**
     * User input for the first name of the author.
     */
    JTextField firstNameField;

    /**
     * User input for the last name of the author.
     */
    JTextField lastNameField;

    /**
     * Radio button to indicate the use of auto-generated maze.
     */
    JRadioButton autoButton;

    /**
     * Radio button to indicate the use of manually-generated maze.
     */
    JRadioButton manualButton;

    /**
     * Button group so that only one radio button can be selected from autoButton/manualButton.
     */
    ButtonGroup autoButtonGroup;

    /**
     *  Button to create a maze.
     *  Opens the {@link MazeEditor}
     */
    JButton createButton;

    /**
     * Label to indicate the file path and name of user selected logo.
     */
    JLabel logoLabel;

    /**
     * Button to load user's choice for a logo.
     */
    JButton logoButton;

    /**
     * Button that allows user to upload entry image.
     * Only appears when user selects children type maze.
     */
    JButton entryImageButton = new JButton("Upload Entry Image");

    /**
     *  Button that allows user to upload exit image.
     *  Only appears when user selects children type maze.
     */
    JButton exitImageButton = new JButton("Upload Exit Image");

    /**
     * The main gbc to control the styling of the menu.
     */
    GridBagConstraints gbc = new GridBagConstraints();

    /**
     * This string value changes the mazeType in {@link MazeEditor}
     * Default value is basic.
     * Note from kmin983: This is soon to be changed into enum or any other more suitable type.
     */
    String mazeType = "Basic";

    /**
     * Constructor to create the form that allows users to create their maze.
     * Includes the fields: type, name, author, size and logo
     */
    public CreateMazeMenuGUI(GUI gui){

        // Create the main GUi frame
        mainFrame = new JFrame();
        mainFrame.setSize(new Dimension(550, 400));
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // Create the main panel
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500,350));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25,50,25,50));
        GridLayout layout = new GridLayout(2, 0);
        layout.setVgap(20);
        mainPanel.setLayout(layout);

        // Separation of top panel and bottom panel
        topPanel = new JPanel();
        bottomPanel = new JPanel();

        topPanel.setLayout(new GridLayout(0, 2));

        // Radio buttons for user to decide which "type" of maze to choose.
        typePanel = new JPanel();
        typePanel.setLayout(new GridLayout(0, 2));
        typeGroup = new ButtonGroup();
        basicButton = new JRadioButton("Basic");
        childrenButton = new JRadioButton("Children");

        // User is able to select a logo to upload from their computer.
        // Depending on maze type, different buttons appear.
        logoButton = new JButton("Upload Logo");
        basicButton.addActionListener(e -> {
            logoButton.setVisible(true);
            entryImageButton.setVisible(false);
            exitImageButton.setVisible(false);
            mazeType = "Basic";
        });
        childrenButton.addActionListener(e -> {
            logoButton.setVisible(false);
            entryImageButton.setVisible(true);
            exitImageButton.setVisible(true);
            mazeType = "Children";
        });

        // Group buttons together appropriately.
        typeGroup.add(basicButton);
        typeGroup.add(childrenButton);
        typePanel.add(basicButton);
        typePanel.add(childrenButton);

        topPanel.add(new JLabel("Type"));
        topPanel.add(typePanel);

        topPanel.add(new JLabel("Maze Name"));
        mazeNameField = new JTextField();
        topPanel.add(mazeNameField);

        topPanel.add(new JLabel("First Name"));
        firstNameField = new JTextField();
        topPanel.add(firstNameField);

        topPanel.add(new JLabel("Last Name"));
        lastNameField = new JTextField();
        topPanel.add(lastNameField);

        sizePanel = new JPanel();
        sizePanel.setLayout(new GridLayout(1, 2));

        rowPanel = new JPanel();
        rowPanel.setLayout(new GridLayout(1,2));
        rowLabel = new JLabel("Row");
        rowPanel.add(rowLabel);
        rowField = new JTextField();
        rowField.setHorizontalAlignment(JTextField.CENTER);
        rowPanel.add(rowField);

        columnPanel = new JPanel();
        columnPanel.setLayout(new GridLayout(1, 2));
        columnLabel = new JLabel("Column");
        columnPanel.add(columnLabel);
        colField = new JTextField();
        columnPanel.add(colField);

        sizePanel.add(rowPanel);
        sizePanel.add(columnPanel);
        topPanel.add(new JLabel("Size"));
        topPanel.add(sizePanel);
        mainPanel.add(topPanel);


        // Create the radio buttons
        // Auto or manual
        autoButton = new JRadioButton("Auto");
        manualButton = new JRadioButton("Manual");
        autoButtonGroup = new ButtonGroup();
        autoButtonGroup.add(autoButton);
        autoButtonGroup.add(manualButton);

        // Upload button
        bottomPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(autoButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(manualButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        entryImageButton.setPreferredSize(new Dimension(175, 35));
        logoButton.setPreferredSize(new Dimension(175, 35));
        bottomPanel.add(logoButton, gbc);
        bottomPanel.add(entryImageButton, gbc);
        entryImageButton.setVisible(false);


        gbc.gridx = 1;
        gbc.gridy = 1;
        logoLabel = new JLabel();
        bottomPanel.add(logoLabel, gbc);
        bottomPanel.add( Box.createGlue(), gbc);
        bottomPanel.add( Box.createVerticalStrut( 35), gbc); // empty box
        bottomPanel.add( Box.createHorizontalStrut(175), gbc);
        exitImageButton.setPreferredSize(new Dimension(175, 35));
        bottomPanel.add(exitImageButton, gbc);
        exitImageButton.setVisible(false);
        mainPanel.add(bottomPanel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.SOUTH;

        createButton = new JButton();
        createButton.setText("Create");
        createButton.addActionListener(e -> {

            String mazeName = mazeNameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();

            boolean isAuto = autoButton.isSelected();
            boolean isManual = manualButton.isSelected();
            boolean isBasic = basicButton.isSelected();
            boolean isChildren = childrenButton.isSelected();

            int rows, cols = 0;

            try{
                 rows = Integer.parseInt(rowField.getText());
            } catch (NumberFormatException ex) {
                rows = 0;
            }

            try{
                cols = Integer.parseInt(colField.getText());
            } catch (NumberFormatException ex) {
                cols = 0;
            }

            if (rows == 0 && cols == 0) {
                JOptionPane.showMessageDialog(mainFrame,
                        "You must enter a valid row and column number.");
                return;
            }

            if (rows < 3 || cols < 3) {
                JOptionPane.showMessageDialog(mainFrame,
                        "The minimum size for a maze is 3x3.");
                return;
            }

            if (mazeName.equals("")) {
                JOptionPane.showMessageDialog(mainFrame,
                        "You must enter a valid maze name.");
                return;
            }

            if (firstName.equals("")) {
                JOptionPane.showMessageDialog(mainFrame,
                        "You must enter a valid author first name.");
                return;
            }

            if (lastName.equals("")) {
                JOptionPane.showMessageDialog(mainFrame,
                        "You must enter a valid author last name.");
                return;
            }

            if(!isAuto && !isManual) {
                JOptionPane.showMessageDialog(mainFrame,
                        "You must decide if this maze is either an auto generated maze or a manually created maze.");
                return;
            }

            if(!isBasic && !isChildren) {
                JOptionPane.showMessageDialog(mainFrame,
                        "You must decide if this maze is either a basic maze or a children's maze.");
                return;
            }

            try {
                new MazeEditor(mazeName, firstName, lastName, rows, cols, isBasic, isAuto, null);
                mainFrame.dispose();
                gui.dispose();
            } catch (MazeGeneratorException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("\nMaze name: " + mazeName);
            System.out.println("First name: " + firstName);
            System.out.println("Last name: " + lastName);
            System.out.println("Rows: " + rows);
            System.out.println("Cols: " + cols);
            System.out.println("Auto btn: " + isAuto);
            System.out.println("Manual btn: " + isManual);
            System.out.println("Basic btn: " + isBasic);
            System.out.println("Children btn: " + isChildren);
        });

        bottomPanel.add(createButton, gbc);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        mainFrame.pack();
        mainFrame.setTitle("Create Maze");

    }
    
}
