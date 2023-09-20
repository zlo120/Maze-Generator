package gui;

import database.DbConnection;
import mazeGenerator.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MazeEditor {
    /**
     * The main frame for the entire menu.
     */
    private JFrame mainFrame;

    /**
     * The main panel to hold all components in the menu.
     */
    private JPanel mainPanel;

    /**
     *  The left side of the menu.
     *  Contains components to change logo, display difficulty metrics,
     *  make changes to the maze (add and modify walls, move image) and apply, save or cancel the changes.
     */
    private JPanel leftPanel;

    /**
     * The right side of the menu.
     * Contains the actual maze, check solvable button and show solution checkbox.
     */
    private JPanel rightPanel;

    /**
     * Button to check if the displayed maze is solvable.
     */
    JButton checkSolvableButton;


    /**
     * Button to display the actual solution on the maze.
     * Displayed as a colored lines following the possible escape route/s.
     */
    JCheckBox solutionButton;

    /**
     * Button to allow user to change the given logo.
     *
     */
    JButton changeImageButton;

    /**
     * Button to allow user to change the given exit image.
     * Only appears for children maze.
     */
    JButton changeExitImageButton;


    /**
     *  Panel with components that allows user to display difficulty metrics.
     */
    JPanel difficultyPanel;

    /**
     * Button that displays the "optimal path metric"
     * The formula is the number of boxes on optimal path / the total number of boxes.
     */
    JButton optimalPathButton;

    /**
     * Button that displays the "Dead ends metric".
     * The formula is the number of dead ends / the total number of boxes.
     */
    JButton deadEndButton;

    /**
     * The panel that holds components to edit the mazes in various ways;
     * Add/remove walls, move images.
     */
    JPanel editPanel;

    /**
     * Button to allow user to add extra walls.
     */
    JButton addWallButton;

    /**
     * Button to allow user to remove selected walls.
     */
    JButton removeWallButton;

    /**
     * Button to allow user to move a logo.
     */
    JButton moveImageButton;

    /**
     * Panel to hold components that confirms editing of the maze (Apply, save, cancel)
     */
    JPanel confirmEditPanel;

    /**
     * Button to save all the changes to the maze in database.
     */
    JButton saveButton;

    /**
     * Button to cancel all the changes and revert back to original state of the maze.
     */
    JButton cancelButton;

    /**
     * The main gbc to control left panel of the menu.
     */
    GridBagConstraints leftConstraint = new GridBagConstraints();

    /**
     * The main gbc to control right panel of the menu.
     */
    GridBagConstraints rightConstraint = new GridBagConstraints();

    /**
     *  The mazeGrid object
     */
    MazePanel mazeGrid;

    JLabel mazeTitleLabel;

    DbConnection conn;

    Maze maze;

    public static final int HEIGHT = Maze.HEIGHT; // best to keep these the same. variable is only created for readability.
    public static final int WIDTH = HEIGHT;
    public static final int H = 25;
    private int cols, rows;

    /**
     * @param title of the maze and the GUI
     * @param firstName of Author
     * @param lastName of Author
     * @param rows int number of rows in the maze
     * @param cols in number of columns in the maze
     * @param isBasic either child or standard maze
     * @param isAuto is either automatically generated or a blank maze
     */
    public MazeEditor(String title, String firstName, String lastName, int rows, int cols, boolean isBasic, boolean isAuto, Maze maze) throws MazeGeneratorException, SQLException {
        this.rows = rows;
        this.cols = cols;
        this.conn = new DbConnection();
        this.maze = maze;

        // Create the main frame and panel.
        mainFrame = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50,15,50,15));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // Separation of left and right panels.
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout());

        rightConstraint.gridx = 0;
        rightConstraint.gridy = 0;
        rightConstraint.gridheight = 0;
        rightConstraint.fill = GridBagConstraints.HORIZONTAL;

        Maze.HEIGHT = rows * H;
        Maze.WIDTH = cols * H;

        if (maze == null) {
            // Actively interact with mazeGenerating classes
            DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeCreated = dtf.format(LocalDateTime.now());

            if (isBasic) {
                this.maze = new BasicMaze(title, firstName, lastName, rows, cols, isAuto, dateTimeCreated, null);
            } else {
                this.maze = new ChildrenMaze(title, firstName, lastName, rows, cols, isAuto, dateTimeCreated, null);
            }

            this.maze.generateMazeMap();
        }

        mazeGrid = new MazePanel(this.maze.getGrid(), this.maze.getRowNum(), this.maze.getColNum(), this.maze);

        mazeGrid.addMouseListener(new WallClickListener(mazeGrid, this.maze));

        rightPanel.add(mazeGrid);

        mazeTitleLabel = new JLabel(title);
        mazeTitleLabel.setFont(new Font(mazeTitleLabel.getFont().getName(), mazeTitleLabel.getFont().getStyle(), 30));
        leftConstraint.gridx = 0;
        leftConstraint.gridy = 0;
        leftConstraint.gridheight = 1;
        leftConstraint.gridwidth = 3;
        leftConstraint.anchor = GridBagConstraints.CENTER;
        leftConstraint.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(mazeTitleLabel, leftConstraint);

        leftConstraint.gridy = 1;

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 2,25,10 ));
        changeImageButton = new JButton();
        changeImageButton.setText("Change Image");
        moveImageButton = new JButton();
        moveImageButton.setText("Move Image");

        imagePanel.add(moveImageButton);

        leftConstraint.gridx = 0;
        leftConstraint.gridy = 2;
        leftConstraint.gridheight = 1;
        leftConstraint.gridwidth = 1;
        leftConstraint.insets = new Insets(0,0,0,0);
        JButton solutionButton = new JButton("Show solution");
        JButton removeSolutionBTN = new JButton("Remove solution");
        removeSolutionBTN.setVisible(false);

        leftConstraint.gridy = 3;

        imagePanel.add(changeImageButton);
        imagePanel.add(moveImageButton);
        imagePanel.add(solutionButton);
        imagePanel.add(removeSolutionBTN);
        leftPanel.add(imagePanel, leftConstraint);

        JPanel metricPanel = new JPanel();
        metricPanel.setLayout(new GridLayout(3,2,0,5));
        leftConstraint.gridy = 5;
        leftConstraint.insets = new Insets(50, 0,0,0);
        JLabel statusLabel = new JLabel("Status: ");
        JLabel solvableLabel = new JLabel("Solvable");
        JLabel optimalPathLabel = new JLabel("Optimal path: ");
        JLabel optimalPathMetric = new JLabel(Integer.toString(this.maze.getSolutionTrail().size()));
        JLabel deadEndLabel = new JLabel("Dead ends: ");
        JLabel deadEndMetric = new JLabel(Integer.toString(this.maze.getDeadEndsCount()));

        removeSolutionBTN.addActionListener(_e -> {
            removeSolutionBTN.setVisible(false);

            this.maze.clearSolutionDisplay();
            rightPanel.repaint();
        });

        solutionButton.addActionListener(e -> {

            removeSolutionBTN.setVisible(true);

            if (!this.maze.getSolutionTrail().isEmpty() && !this.maze.isEdited()) {
                this.maze.setSolutionDisplay();
                rightPanel.repaint();
                return;
            }

            Solution mazeSolution = new Solution(this.maze);
            List<Cell> solutionToMaze = mazeSolution.getSolutionTrail();

            this.maze.setEdited(false);

            if (!this.maze.isSolvable()) {

                this.maze.clearSolutionDisplay();

                rightPanel.repaint();

                removeSolutionBTN.setVisible(false);
                solvableLabel.setText("Unsolvable");
                optimalPathMetric.setText("Undetermined");
                deadEndMetric.setText("Undetermined");


                JOptionPane.showMessageDialog(mainFrame,
                        "No solution can be found.");

                solutionButton.setText("Solve this maze");

                return;
            }

            solutionButton.setText("Show solution");
            solvableLabel.setText("Solvable");
            optimalPathMetric.setText(Integer.toString(this.maze.getSolutionTrail().size()));
            deadEndMetric.setText(Integer.toString(this.maze.getDeadEndsCount()));

            System.out.println("Dead ends: " + this.maze.getDeadEnds());

            System.out.println("\nmaze.solution is: ");
            if (!solutionToMaze.isEmpty()) {
                for (Cell cell : solutionToMaze) {
                    System.out.println(cell);
                }
            } else {
                System.out.print("No solution generated...");
            }

            rightPanel.repaint();
        });

        metricPanel.add(statusLabel);
        metricPanel.add(solvableLabel);
        metricPanel.add(optimalPathLabel);
        metricPanel.add(optimalPathMetric);
        metricPanel.add(deadEndLabel);
        metricPanel.add(deadEndMetric);

        leftPanel.add(metricPanel, leftConstraint);


//        leftPanel.add(editPanel, leftConstraint);

        //        difficultyPanel.add(deadEndButton);
//        difficultyPanel.add(new JLabel("25%"));
//
//        leftConstraint.gridx = 0;
//        leftConstraint.gridy = 2;
//        leftConstraint.gridheight = 1;
//        leftConstraint.gridwidth = 1;
//        leftConstraint.weighty = 0.4;
//        leftPanel.add(difficultyPanel, leftConstraint);

        // Add confirmEditPanel (Apply, save, cancel changes)
        confirmEditPanel = new JPanel();
        confirmEditPanel.setLayout(new GridLayout(0,3, 25, 0));

        saveButton = new JButton();
        saveButton.setText("Save");

        saveButton.addActionListener(e -> {
            int decision = JOptionPane.showConfirmDialog(mainFrame,
                    "You would like to save this maze?",
                    "Save this maze?",
                    JOptionPane.YES_NO_OPTION);

            if (decision  == JOptionPane.YES_OPTION) {

                // check if this maze exists
                try {
                    if (conn.mazeExists(this.maze.getAuthorFirstName(), this.maze.getAuthorLastName(), this.maze.getMazeName()) ){
                        conn.updateMaze(this.maze);
                        new JDialog(mainFrame, "Your maze has been updated!");
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    conn.insertMaze(this.maze);
                    new JDialog(mainFrame, "Your maze has been saved!");
                } catch (SQLException ex) {
                    new JDialog(mainFrame, "Something went wrong with saving your maze...");
                    ex.printStackTrace();
                } catch (IOException ex) {
                    new JDialog(mainFrame, "Something went wrong with saving your maze...");
                    ex.printStackTrace();
                } catch (Exception ex) {
                    new JDialog(mainFrame, "Something went wrong with saving your maze...");
                    ex.printStackTrace();
                }

            }
        });

        cancelButton = new JButton();
        // button has been changed from cancel to close
        cancelButton.setText("Close");

        cancelButton.addActionListener(e -> {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        });

        confirmEditPanel.add(saveButton);
        confirmEditPanel.add(cancelButton);

        leftConstraint.gridx = 0;
        leftConstraint.gridy = 7;
        leftConstraint.gridheight = 1;
        leftConstraint.gridwidth = 1;
        leftConstraint.weighty = 0.1;
        leftConstraint.anchor = GridBagConstraints.SOUTH;
        leftConstraint.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(confirmEditPanel, leftConstraint);

        /**
         * Got method for preventing closing the window from
         * https://stackoverflow.com/questions/7613577/java-how-do-i-prevent-windowclosing-from-actually-closing-the-window
         */
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        /**
         * Method for confirming the window closing inspired by
         * https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
         */
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int decision = JOptionPane.showConfirmDialog(mainFrame,
                        "Are you sure you want to close this window? Make sure to save before closing!",
                        "Close Window?",
                        JOptionPane.YES_NO_OPTION);
                if (decision  == JOptionPane.YES_OPTION) {
                    mainFrame.dispose();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            new GUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        // Finish off the main Panel
        mainPanel.add(leftPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(15,0)));
        mainPanel.add(rightPanel);
        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setSize(new Dimension(rows * this.maze.getPixelSize() + leftPanel.getWidth() + 100, cols * this.maze.getPixelSize() + 150));
        mainFrame.setTitle(title + " - " + firstName + " " + lastName);
        mazeGrid.takePicture(mazeGrid);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}