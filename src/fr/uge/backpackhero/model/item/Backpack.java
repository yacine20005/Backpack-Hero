package fr.uge.backpackhero.model.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private final Set<Position> unlockedCells; // Cells that can be used

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
        this.unlockedCells = new HashSet<>();
        this.mana = 0;

        // Start with 9 unlocked cells in a 3x3 square in the center
        // For a 7x5 backpack (7 cols, 5 rows), center is at columns 2-4, rows 1-3
        int startCol = (width - 3) / 2; // (7-3)/2 = 2
        int startRow = (height - 3) / 2; // (5-3)/2 = 1
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                unlockedCells.add(new Position(col, row));
            }
        }

        place(Weapon.woodenSword(), new Position(startCol, startRow));
        place(Armor.woodenShield(), new Position(startCol + 1, startRow));
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
     * Returns a set of positions that are unlocked (usable) in the backpack.
     * 
     * @return a set of unlocked positions in the backpack
     */
    public Set<Position> getUnlockedCells() {
        return unlockedCells;
    }

    /**
     * Checks if a cell is unlocked (usable).
     * 
     * @param pos the position to check
     * @return true if the cell is unlocked, false otherwise
     */
    public boolean isUnlocked(Position pos) {
        return unlockedCells.contains(pos);
    }

    /**
     * Checks if a cell can be unlocked (is locked and adjacent to an unlocked
     * cell).
     * 
     * @param pos the position to check
     * @return true if the cell can be unlocked, false otherwise
     */
    public boolean canUnlockCell(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        if (!pos.checkBounds(width, height)) {
            return false;
        }
        return !unlockedCells.contains(pos) && hasAdjacentUnlockedCell(pos);
    }

    /**
     * Unlocks a specific cell if it's adjacent to an unlocked cell.
     * 
     * @param pos the position to unlock
     * @return true if the cell was unlocked, false otherwise
     */
    public boolean unlockCell(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        if (!canUnlockCell(pos)) {
            return false;
        }
        unlockedCells.add(pos);
        return true;
    }

    /**
     * Gets all positions that can be unlocked (locked cells adjacent to unlocked
     * ones).
     * 
     * @return a list of positions that can be unlocked
     */
    public List<Position> getUnlockableCells() {
        var candidates = new ArrayList<Position>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                var pos = new Position(col, row);
                if (canUnlockCell(pos)) {
                    candidates.add(pos);
                }
            }
        }
        return candidates;
    }

    /**
     * Checks if a position has at least one adjacent unlocked cell.
     */
    private boolean hasAdjacentUnlockedCell(Position pos) {
        int[][] deltas = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        for (var delta : deltas) {
            var adjacent = new Position(pos.x() + delta[0], pos.y() + delta[1]);
            if (adjacent.checkBounds(width, height) && unlockedCells.contains(adjacent)) {
                return true;
            }
        }
        return false;
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
            if (!unlockedCells.contains(cell)) {
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

        var fromCells = item.getShape().getAbsolutePositions(fromAnchor);
        var toCells = item.getShape().getAbsolutePositions(toAnchor);

        for (var cell : toCells) {
            if (!cell.checkBounds(width, height)) {
                return false;
            }
            if (!unlockedCells.contains(cell)) {
                return false;
            }
        }

        for (var cell : toCells) {
            if (occupiedCells.contains(cell) && !fromCells.contains(cell)) {
                return false;
            }
        }

        remove(fromAnchor);
        place(item, toAnchor);
        return true;
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

    /** @return the Gold item if it exists in the bag, otherwise null */
    public Gold findGold() {
        for (Item item : items.values()) {
            switch (item) {
                case Gold gold -> {
                    return gold;
                }
                default -> {
                }
            }
        }
        return null;
    }

    /**
     * Adds gold to the bag.
     * - If Gold already exists, it is stacked.
     * - Otherwise, one Gold is placed in the first available slot.
     * 
     * @return true if added, false if bag is full.
     */
    public boolean addGold(int amount) {
        if (amount <= 0)
            return true;

        Gold gold = findGold();
        if (gold != null) {
            gold.setAmount(gold.getAmount() + amount);
            return true;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position p = new Position(x, y);
                if (!occupiedCells.contains(p) && isUnlocked(p)) {
                    return place(new Gold(amount), p);
                }
            }
        }
        return false;
    }

    /**
     * @return the total amount of gold (0 if there are no Gold items in the bag)
     */
    public int goldAmount() {
        Gold gold = findGold();
        return (gold == null) ? 0 : gold.getAmount();
    }

    public boolean placeFirstFit(Item item) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position p = new Position(x, y);
                if (canPlace(item, p)) {
                    return place(item, p);
                }
            }
        }
        return false;
    }

    public boolean spendGold(int cost) {
        if (cost <= 0)
            return true;
        Gold gold = findGold();
        if (gold == null)
            return false;
        int current = gold.getAmount();
        if (current < cost)
            return false;
        gold.setAmount(current - cost);
        return true;
    }

    /**
     * Finds the anchor position of the given item in the backpack.
     * 
     * @param item the item to find
     * @return the anchor position of the item, or null if not found
     */
    public Position findAnchorFor(Item item) {
        Objects.requireNonNull(item, "item cannot be null");
        for (var entry : items.entrySet()) {
            if (entry.getValue() == item) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Removes the item at the specified anchor position.
     * 
     * @param anchor the anchor position of the item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean removeItem(Position anchor) {
        Objects.requireNonNull(anchor, "anchor cannot be null");
        return remove(anchor) != null;
    }

}
