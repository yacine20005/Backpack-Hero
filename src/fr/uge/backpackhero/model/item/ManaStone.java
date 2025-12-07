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
     * 
     * @param name         the name of the mana stone
     * @param manaProvided the amount of mana provided by the mana stone
     * @param shape        the shape of the mana stone in the backpack
     * @param rarity       the rarity of the mana stone
     * @param price        the price of the mana stone
     */
    public ManaStone(String name, int manaProvided, Shape shape, Rarity rarity, int price) {
        super(name, shape, rarity, price);
        if (manaProvided < 0) {
            throw new IllegalArgumentException("Mana provided cannot be negative");
        }
        this.manaProvided = manaProvided;
    }

    public static ManaStone smallManaStone() {
        return new ManaStone("Small Mana Stone", 3, Shape.SINGLE, Rarity.COMMON, 8);
    }

    public static ManaStone bigManaStone() {
        return new ManaStone("Big Mana Stone", 5, Shape.SINGLE, Rarity.UNCOMMON, 15);
    }

    public static ManaStone blueCrystal() {
        return new ManaStone("Blue Crystal", 8, Shape.SQUARE_2X2, Rarity.RARE, 25);
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
