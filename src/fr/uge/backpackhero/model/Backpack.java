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
    private int mana; // TODO : move the mana management to Hero
    private final Map<Position, Item> items;
    private final Set<Position> occupiedCells; // We use a set to track occupied cells faster

    public Backpack(int width, int height) {
        if (width <= 1 || height <= 1) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
        this.items = new HashMap<>();
        this.occupiedCells = new HashSet<>();
        this.mana = 0;
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

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMana() {
        mana = 0;
        for (var item : items.values()) {
            mana += item.getManaProvided();
        }
        return mana;
    }

    public boolean isOccupied(Position pos) {
        return occupiedCells.contains(Objects.requireNonNull(pos, "pos cannot be null"));
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
        Objects.requireNonNull(item, "item cannot be null");
        Objects.requireNonNull(anchor, "anchor cannot be null");
        if (!canPlace(item, anchor)) {
            return false;
        }
        items.put(anchor, item);
        occupiedCells.addAll(item.getShape().absolutePositions(anchor));
        return true;
    }

    public Item remove(Position anchor) {
        Objects.requireNonNull(anchor, "anchor cannot be null");
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
        Objects.requireNonNull(fromAnchor, "fromAnchor cannot be null");
        Objects.requireNonNull(toAnchor, "toAnchor cannot be null");
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
        Objects.requireNonNull(anchor, "anchor cannot be null");
        var item = items.get(anchor);
        if (item == null) {
            return false;
        }
        remove(anchor);
        item.shape.rotate90();
        if (place(item, anchor)) {
            return true;
        }
        // If failed, undo the rotation (3 rotations = back to original rotation)
        item.shape.rotate270();
        place(item, anchor);
        return false;
    }

}
