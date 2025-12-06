package fr.uge.backpackhero.model.item;

/**
 * Represents a mana stone item that can be used by the hero to gain mana.
 * Mana stones provide a certain amount of mana when in the backpack.
 * 
 * @author Yacine
 */
public class ManaStone extends Item {

    private final int manaProvided;

    /**
     * Creates a new ManaStone item.
     * We are not going to use this constructor directly since mana stones are
     * always represented as a single unit in the backpack.
     * 
     * @param name         the name of the mana stone
     * @param manaProvided the amount of mana provided by the mana stone
     * @param shape        the shape of the mana stone in the backpack
     */
    public ManaStone(String name, int manaProvided, Shape shape) {
        if (manaProvided < 0) {
            throw new IllegalArgumentException("Mana provided cannot be negative");
        }
        super(name, shape);
        this.manaProvided = manaProvided;
    }

    /**
     * Creates a new ManaStone item with a single shape.
     * 
     * @param name         the name of the mana stone
     * @param manaProvided the amount of mana provided by the mana stone
     */
    public ManaStone(String name, int manaProvided) {
        this(name, manaProvided, Shape.SINGLE);
    }

    /**
     * Returns the amount of mana provided by the mana stone.
     * 
     * @return the amount of mana provided
     */
    public int getManaProvided() {
        return manaProvided;
    }

    /**
     * Indicates that this item is a mana stone.
     * 
     * @return true
     */
    @Override
    public boolean isManaStone() {
        return true;
    }
}
