package fr.uge.backpackhero.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.Shape;
import fr.uge.backpackhero.model.item.Weapon;
import fr.uge.backpackhero.model.level.Position;

public class Backpack {

    private final int width;
    private final int height;

    private final Map<Position, Item> items;
    private final Set<Position> occupiedCells; // We will use a set to track occupied cells faster

    public Backpack(int width, int height) {
        this.width = width;
        this.height = height;
        this.items = new HashMap<>();
        this.occupiedCells = new HashSet<>();
        place(new Weapon("Wooden Sword", 5, 1, 0, Shape.VERTICAL_3), new Position(0, 0));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Position, Item> getItems() {
        return items;
    }

    public Set<Position> getOccupiedCells() {
        return occupiedCells;
    }

    public boolean isOccupied(Position pos) {
        return occupiedCells.contains(pos);
    }

    public boolean canPlace(Item item, Position anchor) {
        Objects.requireNonNull(item, "item cannot be null");
        Objects.requireNonNull(anchor, "anchor cannot be null");

        var cells = item.getShape().absolutePositions(anchor);
        for (var cell : cells) {
            if (!cell.checkBounds(width, height)) {
                return false;
            }
            if (occupiedCells.contains(cell)) {
                return false;
            }
        }
        return true;
    }

    public boolean place(Item item, Position anchor) {
        if (!canPlace(item, anchor)) {
            return false;
        }

        items.put(anchor, item);
        occupiedCells.addAll(item.getShape().absolutePositions(anchor));
        return true;
    }

    public Item remove(Position anchor) {
        var item = items.remove(anchor);
        if (item != null) {
            occupiedCells.removeAll(item.getShape().absolutePositions(anchor));
        }
        return item;
    }

    public Optional<Item> getItemAt(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        for (var entry : items.entrySet()) {
            var anchor = entry.getKey();
            var item = entry.getValue();
            if (item.getShape().absolutePositions(anchor).contains(pos)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public Optional<Position> getAnchorAt(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        for (var entry : items.entrySet()) {
            var anchor = entry.getKey();
            var item = entry.getValue();
            if (item.getShape().absolutePositions(anchor).contains(pos)) {
                return Optional.of(anchor);
            }
        }
        return Optional.empty();
    }

    public boolean move(Position fromAnchor, Position toAnchor) {
        var item = items.get(fromAnchor);
        if (item == null) {
            return false;
        }
        remove(fromAnchor);
        if (place(item, toAnchor)) {
            return true;
        }
        place(item, fromAnchor);
        return false;
    }

    public boolean rotateItem(Position anchor) {
        var item = items.get(anchor);
        if (item == null) {
            return false;
        }
        remove(anchor);
        item.rotate();
        if (place(item, anchor)) {
            return true;
        }
        // If failed, undo the rotation (3 rotations = back to original rotation)
        item.rotate();
        item.rotate();
        item.rotate();
        place(item, anchor);
        return false;
    }

}
