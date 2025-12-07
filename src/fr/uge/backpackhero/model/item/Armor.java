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

    public static Armor woodenShield() {
        return new Armor("Wooden Shield", 3, 1, 0, Shape.SINGLE, Rarity.COMMON, 5);
    }

    public static Armor emeraldShield() {
        return new Armor("Emerald Shield", 4, 1, 0, Shape.SQUARE_2X2, Rarity.UNCOMMON, 15);
    }

    public static Armor celestialnighthawk() {
        return new Armor("Celestial Nighthawk", 5, 2, 0, Shape.SQUARE_2X2, Rarity.UNCOMMON, 15);
    }

    public static Armor liarshandshake() {
        return new Armor("Liar's Handshake", 7, 0, 2, Shape.HORIZONTAL_3, Rarity.RARE, 30);
    }

    public static Armor luckypants() {
        return new Armor("Lucky Pants", 10, 3, 0, Shape.VERTICAL_3, Rarity.EPIC, 50);
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