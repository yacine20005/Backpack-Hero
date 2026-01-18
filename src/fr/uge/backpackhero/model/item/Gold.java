package fr.uge.backpackhero.model.item;

/**
 * Represents a gold item that can be collected by the hero.
 * Gold has an amount that can be increased or decreased.
 * We assume that gold is always represented as a single unit in the backpack
 * and that gold is always stackable.
 * Exemple : if the hero has 10 gold and picks up 5 more gold, the total amount
 * of gold becomes 15 and occupies the same single slot in the backpack.
 * 
 * @author Yacine
 */
public class Gold extends Item {

    private int amount;

    /**
     * Creates a new Gold item.
     * We are never going to use this constructor directly since gold is always
     * represented as a single unit in the backpack.
     * 
     * @param amount the amount of gold
     * @param shape  the shape of the gold item in the backpack
     */
    public Gold(int amount, Shape shape) {
        super("Gold", shape, Rarity.COMMON, 0);
        if (amount < 0) {
            throw new IllegalArgumentException("Gold amount cannot be negative");
        }
        this.amount = amount;
    }

    /**
     * Creates a new Gold item with a single shape.
     * 
     * @param amount the amount of gold
     */
    public Gold(int amount) {
        this(amount, Shape.SINGLE);
    }

    /**
     * Returns the amount of gold.
     * 
     * @return the amount of gold
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of gold.
     * 
     * @param amount the amount of gold
     */
    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Gold amount cannot be negative");
        }
        this.amount = amount;
    }

}