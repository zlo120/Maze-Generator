package gui;

import database.DbConnection;
import mazeGenerator.BasicMaze;
import mazeGenerator.Maze;
import mazeGenerator.MazeGeneratorException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 *  The following ButtonColumn class is extracted from "https://tips4java.wordpress.com/2009/07/12/table-button-column/"
 *
 *  The ButtonColumn class provides a renderer and an editor that looks like a
 *  JButton. The renderer and editor will then be used for a specified column
 *  in the table. The TableModel will contain the String to be displayed on
 *  the button.
 *
 *  The button can be invoked by a mouse click or by pressing the space bar
 *  when the cell has focus. Optionally a mnemonic can be set to invoke the
 *  button. When the button is invoked the provided Action is invoked. The
 *  source of the Action will be the table. The action command will contain
 *  the model row number of the button that was clicked.
 *
 */
public class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener
{
    /**
     * Main table that the class is going to control.
     */
    private JTable table;

    /**
     * The action that is used for the button column.
     */
    private Action action;

    /**
     * The key that is pressed in integer.
     */
    private int mnemonic;

    /**
     * The border in "normal state"
     */
    private Border originalBorder;

    /**
     * The border in "focused state"
     */
    private Border focusBorder;

    /**
     * The button that rendered.
     */
    private JButton renderButton;

    /**
     * The button that is edited.
     */
    private JButton editButton;

    /**
     * the value of cell edited.
     */
    private Object editorValue;

    /**
     * determines if selected cell is edited.
     */
    private boolean isButtonColumnEditor;

    private DefaultTableModel model;

    private GUI gui;

    /**
     *  Create the ButtonColumn to be used as a renderer and editor. The
     *  renderer and editor will automatically be installed on the TableColumn
     *  of the specified column.
     *
     *  @param table the table containing the button renderer/editor
     *  @param action the Action to be invoked when the button is invoked
     *  @param column the column to which the button renderer/editor is added
     */
    public ButtonColumn(JTable table, Action action, int column, DefaultTableModel model, GUI gui)
    {
        this.table = table;
        this.action = action;
        this.model = model;
        this.gui = gui;

        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted( false );
        editButton.addActionListener( this );
        originalBorder = editButton.getBorder();
        setFocusBorder( new LineBorder(Color.BLUE) );
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer( this );
        columnModel.getColumn(column).setCellEditor( this );
        table.addMouseListener( this );
    }


    /**
     *  Get foreground color of the button when the cell has focus
     *
     *  @return the foreground color
     */
    public Border getFocusBorder()
    {
        return focusBorder;
    }

    /**
     *  The foreground color of the button when the cell has focus
     *
     *  @param focusBorder the foreground color
     */
    public void setFocusBorder(Border focusBorder)
    {
        this.focusBorder = focusBorder;
        editButton.setBorder( focusBorder );
    }

    /**
     * Mnemonic is a key-press, following function returns the current key-press
     *
     * @return the mnemonic, the key pressed.
     */
    public int getMnemonic()
    {
        return mnemonic;
    }

    /**
     *  The mnemonic to activate the button when the cell has focus
     *
     *  @param mnemonic the mnemonic the key pressed.
     */
    public void setMnemonic(int mnemonic)
    {
        this.mnemonic = mnemonic;
        renderButton.setMnemonic(mnemonic);
        editButton.setMnemonic(mnemonic);
    }

    /**
     * @param table the table with button column
     * @param value the value of the cell selected
     * @param isSelected check if cell is selected
     * @param row row that is selected
     * @param column column that is selected
     * @return editButton
     */
    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
    {
        if (value == null)
        {
            editButton.setText( "" );
            editButton.setIcon( null );
        }
        else if (value instanceof Icon)
        {
            editButton.setText( "" );
            editButton.setIcon( (Icon)value );
        }
        else
        {
            editButton.setText( value.toString() );
            editButton.setIcon( null );
        }

        this.editorValue = value;
        return editButton;
    }

    /**
     * @return editorValue the editor value of a cell
     */
    @Override
    public Object getCellEditorValue()
    {
        return editorValue;
    }


    /**
     * Implement TableCellRenderer interface
     *
     * @param table the table with the button column
     * @param value value the value of the cell selected
     * @param isSelected check if cell is selected
     * @param hasFocus check if cell is on focus
     * @param row row that is selected
     * @param column column that is selected
     * @return renderButton the button that is selected and is rendered.
     */
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected)
        {
//            renderButton.setForeground(table.getSelectionForeground());
//            renderButton.setBackground(table.getSelectionBackground());
        }
        else
        {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        if (hasFocus)
        {
            renderButton.setBorder( focusBorder );
        }
        else
        {
            renderButton.setBorder( originalBorder );
        }

        if (value == null)
        {
            renderButton.setText( "" );
            renderButton.setIcon( null );
        }
        else if (value instanceof Icon)
        {
            renderButton.setText( "" );
            renderButton.setIcon( (Icon)value );
        }
        else
        {
            renderButton.setText( value.toString() );
            renderButton.setIcon( null );
        }

        return renderButton;
    }

    /**
     * Implement ActionListener interface.
     * @param e Action passed from the parent.
     */
    public void actionPerformed(ActionEvent e)
    {
        int row = table.convertRowIndexToModel( table.getEditingRow() );
        // The button has been pressed. Stop editing and invoke the custom Action
        fireEditingStopped();

        //  Invoke the Action

        ActionEvent event = new ActionEvent(
                table,
                ActionEvent.ACTION_PERFORMED,
                "" + row);
        action.actionPerformed(event);
    }

    /**
     *  Implement MouseListener interface
     *  When the mouse is pressed the editor is invoked. If you then drag
     *  the mouse to another cell before releasing it, the editor is still
     *  active. Make sure editing is stopped when the mouse is released.
     * @param e A Mouse event to detect the mouse press.
     */
    public void mousePressed(MouseEvent e)
    {
        if (table.isEditing()
                &&  table.getCellEditor() == this)
            isButtonColumnEditor = true;
    }

    /**
     * Implement MouseListener interface
     * When the mouse is released the editor is invoked.
     * @param e A Mouse event to detect the mouse release.
     */
    public void mouseReleased(MouseEvent e)
    {
        if (isButtonColumnEditor
                &&  table.isEditing())
            table.getCellEditor().stopCellEditing();

        isButtonColumnEditor = false;
    }

    /**
     * Implement MouseListener interface.
     * When the mouse is clicked the editor is invoked.
     * @param e A mouse event to detect and control the mouse click.
     */
    public void mouseClicked(MouseEvent e) {

        String mazeName = model.getValueAt(this.table.getSelectedRow(), 2).toString();
        String firstName = model.getValueAt(this.table.getSelectedRow(), 3).toString();
        String lastName = model.getValueAt(this.table.getSelectedRow(), 4).toString();

        System.out.println("Maze: " + mazeName + " " + firstName + " " + lastName);

        try {
            DbConnection conn = new DbConnection();
            Maze maze = conn.retrieveMaze(firstName, lastName, mazeName);

            System.out.println("Maze: " + maze.getMazeName() + " " + maze.getAuthorFirstName() + " " + maze.getAuthorLastName());
            boolean isBasic = false;
            if (maze.getClass() == BasicMaze.class) {
                isBasic = true;
            }

            System.out.println(maze.getRowNum());
            System.out.println(maze.getColNum());

            new MazeEditor(maze.getMazeName(), maze.getAuthorFirstName(), maze.getAuthorLastName(), maze.getRowNum(),
                    maze.getColNum(), isBasic, true, maze);

            gui.dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (MazeGeneratorException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Action to be taken after mouse is entered.
     * @param e A mouse event to detect and control mouse enter.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Action to be taken after mouse is exited.
     * @param e A mouse event to detect and control mouse exit.
     */
    public void mouseExited(MouseEvent e) {}

}