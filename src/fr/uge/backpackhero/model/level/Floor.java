package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Floor {
    private final int width;
    private final int height;
    private final List<List<Room>> grid;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setRoom(Position pos, Room room) {
        Objects.requireNonNull(pos);
        Objects.requireNonNull(room);
        if (!pos.checkBounds(width, height)) {
            throw new IllegalArgumentException("Position " + pos + " is out of bounds");
        }
        grid.get(pos.y()).set(pos.x(), room);
    }

    public Room getRoom(Position pos) {
        Objects.requireNonNull(pos);
        if (!pos.checkBounds(width, height)) {
            throw new IllegalArgumentException("Position " + pos + " is out of bounds");
        }
        return grid.get(pos.y()).get(pos.x());
    }

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
