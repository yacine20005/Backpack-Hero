package fr.uge.backpackhero.model.level;

import java.util.Objects;

/**
 * Represents the type of a room in the dungeon.
 * Each room type is associated with a unique symbol.
 * 
 */
public enum RoomType {
    /** A simple corridor with no special content. */
    CORRIDOR("C"),
    /** A room containing enemies to fight. */
    ENEMY("E"),
    /** A room containing treasure to collect. */
    TREASURE("T"),
    /** A room with a merchant to buy/sell items. */
    MERCHANT("M"),
    /** A room with a healer to restore health. */
    HEALER("H"),
    /** An exit to the next floor. */
    EXIT("X");

    private final String symbol;

    /**
     * Creates a new RoomType with the specified symbol.
     * 
     * @param symbol the symbol representing the room type
     */
    RoomType(String symbol) {
        this.symbol = Objects.requireNonNull(symbol, "symbol cannot be null");
    }

    /**
     * Returns the symbol representing the room type.
     * 
     * @return the symbol representing the room type
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns a string representation of the room type.
     * 
     * @return a string representation of the room type
     */
    @Override
    public String toString() {
        return symbol;
    }
}
