package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a floor in the dungeon consisting of a grid of rooms.
 * Each room can be of different types such as enemy rooms, treasure rooms, corridors, etc.
 * 
 * @author Yacine
 */
public class Floor {
    private final int width;
    private final int height;
    private final List<List<Room>> grid;

    /**
     * Creates a new Floor with the specified width and height.
     * In this initialization, all rooms are set to null to avoid accidental access to uninitialized rooms that can cause bugs in the future and we don't like bugs do we?
     * 
     * @param width the width of the floor
     * @param height the height of the floor
     */
    public Floor(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
        this.grid = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            ArrayList<Room> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(null); // Intialize with null to be able to check if a room is empty
            }
            this.grid.add(row);
        }
    }

    /**
     * Returns the width of the floor.
     * 
     * @return the width of the floor
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the floor.
     * 
     * @return the height of the floor
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets a room at the specified position on the floor.
     * 
     * @param pos the position of the room to set
     * @param room the room to set at the specified position
     */
    public void setRoom(Position pos, Room room) {
        Objects.requireNonNull(pos);
        Objects.requireNonNull(room);
        if (!pos.checkBounds(width, height)) {
            throw new IllegalArgumentException("Position " + pos + " is out of bounds");
        }
        grid.get(pos.y()).set(pos.x(), room);
    }

    /**
     * Returns the room at the specified position on the floor.
     * 
     * @param pos the position of the room to retrieve
     * @return the room at the specified position
     */
    public Room getRoom(Position pos) {
        Objects.requireNonNull(pos);
        if (!pos.checkBounds(width, height)) {
            throw new IllegalArgumentException("Position " + pos + " is out of bounds");
        }
        return grid.get(pos.y()).get(pos.x());
    }

    /**
     * Returns a string representation of the floor, showing the layout of rooms.
     * 
     * @return a string representation of the floor
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Room room = grid.get(y).get(x);
                if (room == null) {
                    string.append(".");
                } else {
                    string.append(room.toString());
                }
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}
