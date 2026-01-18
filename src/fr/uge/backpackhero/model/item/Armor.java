package fr.uge.backpackhero.model.item;

/**
 * Represents an armor item that can be equipped by the hero.
 * Armor provides protection and has an associated energy cost.
 * 
 * @author Yacine
 */
public class Armor extends Item {

    private final int protection;
    private final int energyCost;
    private final int manaCost;

    /**
     * Creates a new Armor item.
     * 
     * @param name       the name of the armor
     * @param protection the amount of protection provided by the armor
     * @param energyCost the energy cost to use the armor
     * @param manaCost   the mana cost to use the armor
     * @param shape      the shape of the armor item in the backpack
     * @param rarity     the rarity of the armor
     * @param price      the price of the armor
     */
    public Armor(String name, int protection, int energyCost, int manaCost, Shape shape, Rarity rarity, int price) {
        super(name, shape, rarity, price);
        if (protection < 0 || energyCost < 0 || manaCost < 0) {
            throw new IllegalArgumentException("Protection, energy cost, and mana cost must be non-negative");
        }
        this.protection = protection;
        this.energyCost = energyCost;
        this.manaCost = manaCost;
    }

    /**
     * Creates a Wooden Shield armor.
     * 
     * @return a Wooden Shield armor
     */
    public static Armor woodenShield() {
        return new Armor("Wooden Shield", 3, 1, 0, Shape.SINGLE, Rarity.COMMON, 5);
    }

    /**
     * Creates a Iron Shield armor.
     * 
     * @return an Iron Shield armor
     */
    public static Armor ironShield() {
        return new Armor("Iron Shield", 5, 2, 0, Shape.SQUARE_2X2, Rarity.UNCOMMON, 15);
    }

    /**
     * Creates a Golden Shield armor.
     * 
     * @return a Golden Shield armor
     */
    public static Armor goldenShield() {
        return new Armor("Golden Shield", 8, 2, 0, Shape.SQUARE_2X2, Rarity.RARE, 30);
    }

    public static Armor diamondShield() {
        return new Armor("Diamond Shield", 12, 3, 0, Shape.SQUARE_2X2, Rarity.EPIC, 50);
    }

    /**
     * Creates a Celestial Nighthawk armor from Destiny 2.
     * 
     * @return a Celestial Nighthawk armor
     */
    public static Armor celestialnighthawk() {
        return new Armor("Celestial Nighthawk", 18, 3, 0, Shape.SQUARE_3X3, Rarity.EXOTIC, 100);
    }

    /**
     * Creates Lucky Pants armor from Destiny 2.
     * 
     * @return Lucky Pants armor
     */
    public static Armor luckypants() {
        return new Armor("Lucky Pants", 15, 2, 0, Shape.VERTICAL_3, Rarity.EXOTIC, 80);
    }

    /**
     * Returns the protection value of the armor.
     * 
     * @return the protection value
     */
    public int getProtection() {
        return protection;
    }

    /**
     * Returns the energy cost to use the armor.
     * 
     * @return the energy cost
     */
    public int getEnergyCost() {
        return energyCost;
    }

    /**
     * Returns the mana cost to use the armor.
     * 
     * @return the mana cost
     */
    public int getManaCost() {
        return manaCost;
    }
}