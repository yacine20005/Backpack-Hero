package fr.uge.backpackhero.model.item;

import java.util.Objects;

/**
 * Represents a generic item that can be stored in the hero's backpack.
 * Each item has a name and a shape that determines how it occupies space in the backpack.
 * 
 * @author Yacine
 */
public abstract class Item {

    private final String name;
    /** The shape of the item defining how it occupies space in the backpack. */
    public Shape shape;

    /**
     * Creates a new Item that can be stored in the backpack.
     * 
     * @param name the name of the item
     * @param shape the shape of the item in the backpack
     */
    public Item(String name, Shape shape) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.shape = Objects.requireNonNull(shape, "shape cannot be null");
    }

    /**
     * Returns the name of the item.
     * 
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the shape of the item.
     * 
     * @return the shape of the item
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Indicates whether the item is a mana stone.
     * 
     * @return true if the item is a mana stone, false otherwise
     */
    public boolean isManaStone() {
        return false;
    }

    /**
     * Returns the amount of mana provided by the item.
     * 
     * @return the amount of mana provided by the item
     */
    public int getManaProvided() {
        return 0;
    }

    /**
     * Sets the shape of the item.
     * 
     * @param shape the new shape of the item
     */
    public void setShape(Shape shape) {
        Objects.requireNonNull(shape, "shape cannot be null");
        this.shape = shape;
    }
}
