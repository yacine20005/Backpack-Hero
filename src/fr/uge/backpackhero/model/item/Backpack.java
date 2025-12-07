package fr.uge.backpackhero.model.item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import fr.uge.backpackhero.model.level.Position;

/**
 * Represents the hero's backpack which can hold various items.
 * The backpack has a defined width and height, and items can be placed,
 * removed, moved, and rotated within it.
 * It also tracks the mana provided by the mana stones contained in the
 * backpack.
 * 
 * @author Yacine
 */
public class Backpack {

    private final int width;
    private final int height;
    private int mana; // TODO : move the mana management to Hero
    private final Map<Position, Item> items;
    private final Set<Position> occupiedCells; // We use a set to track occupied cells faster

    /**
     * Creates a new Backpack with the specified width and height.
     * ckpack
     * 
     * @param height the height of the backpack
     */
    public Backpack(int width, int height) {
        if (width <= 1 || height <= 1) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
        this.items = new HashMap<>();
        this.occupiedCells = new HashSet<>();
        this.mana = 0;
        place(Weapon.woodSword(), new Position(0, 0));
        place(Armor.woodenShield(), new Position(1, 0));
    }

    /**
     * Returns the width of the backpack.
     * 
     * @return the width of the backpack
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the backpack.
     * 
     * @return the height of the backpack
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a map of items in the backpack with their anchor positions.
     * 
     * @return a map of items in the backpack with their anchor positions
     */
    public Map<Position, Item> getItems() {
        return items;
    }

    /**
     * Returns a set of positions that are currently occupied by items in the
     * backpack.
     * 
     * @return a set of occupied positions in the backpack
     */
    public Set<Position> getOccupiedCells() {
        return occupiedCells;
    }

    /**
     * Sets the mana value of the backpack.
     * 
     * @param mana the mana value to set
     */
    public void setMana(int mana) {
        this.mana = mana;
    }

    /**
     * Returns the total mana provided by the mana stones in the backpack.
     * 
     * @return the total mana provided by the mana stones
     */
    public int getMana() {
        mana = 0;
        for (var item : items.values()) {
            mana += item.getManaProvided();
        }
        return mana;
    }

    /**
     * Checks if a given position in the backpack is occupied by an item.
     * 
     * @param pos the position to check
     * @return true if the position is occupied, false otherwise
     */
    public boolean isOccupied(Position pos) {
        return occupiedCells.contains(Objects.requireNonNull(pos, "pos cannot be null"));
    }

    /**
     * Checks if an item can be placed at the specified anchor position in the
     * backpack.
     * 
     * @param item   the item to be placed
     * @param anchor the anchor position where the item is to be placed
     * @return true if the item can be placed, false otherwise
     */
    public boolean canPlace(Item item, Position anchor) {
        Objects.requireNonNull(item, "item cannot be null");
        Objects.requireNonNull(anchor, "anchor cannot be null");
        var cells = item.getShape().getAbsolutePositions(anchor);
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

    /**
     * Places an item at the specified anchor position in the backpack.
     * 
     * @param item   the item to be placed
     * @param anchor the anchor position where the item is to be placed
     * @return true if the item was successfully placed, false otherwise
     */
    public boolean place(Item item, Position anchor) {
        Objects.requireNonNull(item, "item cannot be null");
        Objects.requireNonNull(anchor, "anchor cannot be null");
        if (!canPlace(item, anchor)) {
            return false;
        }
        items.put(anchor, item);
        occupiedCells.addAll(item.getShape().getAbsolutePositions(anchor));
        return true;
    }

    /**
     * Removes the item at the specified anchor position from the backpack.
     * 
     * @param anchor the anchor position of the item to be removed
     * @return the removed item, or null if no item was found at the anchor position
     */
    public Item remove(Position anchor) {
        Objects.requireNonNull(anchor, "anchor cannot be null");
        var item = items.remove(anchor);
        if (item != null) {
            occupiedCells.removeAll(item.getShape().getAbsolutePositions(anchor));
        }
        return item;
    }

    /**
     * Gets the item located at the specified position in the backpack.
     * 
     * @param pos the position to check
     * @return an Optional containing the item if found, or an empty Optional if no
     *         item is at the position
     */
    public Optional<Item> getItemAt(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        for (var entry : items.entrySet()) {
            var anchor = entry.getKey();
            var item = entry.getValue();
            if (item.getShape().getAbsolutePositions(anchor).contains(pos)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the anchor position of the item located at the specified position in the
     * backpack.
     * 
     * @param pos the position to check
     * @return an Optional containing the anchor position if found, or an empty
     *         Optional if no item is at the position
     */
    public Optional<Position> getAnchorAt(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        for (var entry : items.entrySet()) {
            var anchor = entry.getKey();
            var item = entry.getValue();
            if (item.getShape().getAbsolutePositions(anchor).contains(pos)) {
                return Optional.of(anchor);
            }
        }
        return Optional.empty();
    }

    /**
     * Moves an item from one anchor position to another within the backpack.
     * 
     * @param fromAnchor the current anchor position of the item
     * @param toAnchor   the new anchor position where the item should be moved
     * @return true if the item was successfully moved, false otherwise
     */
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

    /**
     * Rotates the item at the specified anchor position by 90 degrees clockwise.
     * 
     * @param anchor the anchor position of the item to be rotated
     * @return true if the item was successfully rotated, false otherwise
     */
    public boolean rotateItem(Position anchor) {
        Objects.requireNonNull(anchor, "anchor cannot be null");
        var item = items.get(anchor);
        if (item == null) {
            return false;
        }
        remove(anchor);
        item.setShape(item.getShape().rotate90());
        if (place(item, anchor)) {
            return true;
        }
        // If failed, undo the rotation (3 rotations = back to original rotation)
        item.setShape(item.getShape().rotate270());
        place(item, anchor);
        return false;
    }

}
