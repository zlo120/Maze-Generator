package mazeGenerator;

import java.io.Serializable;

/**
 * This class manages images for maze
 */
public class Images implements Serializable {
    // Fields
    /**
     * default cell width
     */
    private final int DEFAULT_IMAGE_CELL_WIDTH = 2;
    /**
     * default cell height
     */
    private final int DEFAULT_IMAGE_CELL_HEIGHT = 2;
    /**
     * image cell width
     */
    private int widthCellSize;
    /**
     * image cell height
     */
    private int heightCellSize;

    /**
     * This constructor instantiates an image object with default size values
     */
    public Images() {
        this.widthCellSize = DEFAULT_IMAGE_CELL_WIDTH;
        this.heightCellSize = DEFAULT_IMAGE_CELL_HEIGHT;
    }

    /**
     * Resizes an image
     * @param widthCellSize image cell width
     * @param heightCellSize image cell height
     */
    public void resize(int widthCellSize, int heightCellSize) {
        //...
        // Change image size
    }

    /**
     * Displays image on a maze
     */
    public void display() {
        //...
    }

    /**
     * Allows a user to move an image
     */
    public void moveImage() {
        //...
    }
}
