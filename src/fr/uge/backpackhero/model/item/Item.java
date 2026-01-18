package fr.uge.backpackhero.model.item;

/**
 * Sealed interface representing items that can be stored in the hero's backpack.
 * Only specific types of items are permitted: Weapon, Armor, ManaStone, and Gold.
 * 
 */
public sealed interface Item permits Weapon, Armor, ManaStone, Gold {

    /**
     * Returns the name of the item.
     * 
     * @return the name of the item
     */
    String getName();

    /**
     * Returns the shape of the item.
     * 
     * @return the shape of the item
     */
    Shape getShape();

    /**
     * Sets the shape of the item.
     * 
     * @param shape the new shape of the item
     */
    void setShape(Shape shape);

    /**
     * Returns the amount of mana provided by the item.
     * Default implementation returns 0 for items that don't provide mana.
     * 
     * @return the amount of mana provided by the item
     */
    default int getManaProvided() {
        return 0;
    }

    /**
     * Indicates whether the item is a mana stone.
     * Default implementation returns false.
     * 
     * @return true if the item is a mana stone, false otherwise
     */
    default boolean isManaStone() {
        return false;
    }

    /**
     * Returns the rarity of the item.
     * 
     * @return the rarity of the item
     */
    Rarity getRarity();

    /**
     * Returns the price of the item.
     * 
     * @return the price of the item
     */
    int getPrice();
}
