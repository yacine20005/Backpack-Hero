package fr.uge.backpackhero.model.level;

/**
 * Represents a position in a 2D grid with x and y coordinates.
 * 
 * @param x the horizontal coordinate (column)
 * @param y the vertical coordinate (row)
 * 
 */
public record Position(int x, int y) {

    /**
     * Checks if the position is within the bounds of the specified width and
     * height.
     * 
     * @param width  the width of the grid
     * @param height the height of the grid
     * @return true if the position is within bounds, false otherwise
     */
    public boolean checkBounds(int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Returns a string representation of the position in the format (x, y).
     * 
     * @return a string representation of the position
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
