package fr.uge.backpackhero.model.item;

/**
 * Represents a weapon item that can be used by the hero to deal damage.
 * Weapons have damage, energy cost, and mana cost attributes.
 * 
 * @author Yacine
 */
public class Weapon extends Item {

    private final int damage;
    private final int energyCost;
    private final int manaCost;

    /**
     * Creates a new Weapon item.
     * 
     * @param name       the name of the weapon
     * @param damage     the damage dealt by the weapon
     * @param energyCost the energy cost to use the weapon
     * @param manaCost   the mana cost to use the weapon
     * @param shape      the shape of the weapon
     * @param rarity     the rarity of the weapon
     * @param price      the price of the weapon
     */
    public Weapon(String name, int damage, int energyCost, int manaCost, Shape shape, Rarity rarity, int price) {
        super(name, shape, rarity, price);
        this.damage = damage;
        this.energyCost = energyCost;
        this.manaCost = manaCost;
    }

    /**
     * Creates a Wood Sword weapon.
     * 
     * @return a Wood Sword weapon
     */
    public static Weapon woodSword() {
        return new Weapon("Wood Sword", 5, 1, 0, Shape.VERTICAL_2, Rarity.COMMON, 5);
    }

    /**
     * Creates a Montaintop weapon, a grenade launcher from Destiny 2.
     * 
     * @return a Montaintop weapon
     */
    public static Weapon montaintop() {
        return new Weapon("Montaintop", 10, 2, 0, Shape.VERTICAL_2, Rarity.UNCOMMON, 15);
    }

    /**
     * Creates a Last Word weapon, a exotic revolver from Destiny 2.
     * 
     * @return a Last Word weapon
     */
    public static Weapon lastWord() {
        return new Weapon("Last Word", 15, 3, 0, Shape.VERTICAL_3, Rarity.RARE, 30);
    }

    /**
     * Creates a Sturn weapon, a revolver from Destiny 2.
     * 
     * @return a Sturn weapon
     */
    public static Weapon sturn() {
        return new Weapon("Sturn", 20, 3, 0, Shape.HORIZONTAL_3, Rarity.EPIC, 50);
    }

    /**
     * Creates a Telesto weapon, a fusion rifle from Destiny 2.
     * 
     * @return a Telesto weapon
     */
    public static Weapon telesto() {
        return new Weapon("Telesto", 25, 0, 2, Shape.SQUARE_2X2, Rarity.EXOTIC, 100);
    }

    /**
     * Creates a Wooden Bow ranged weapon.
     * 
     * @return a new Wooden Bow weapon instance
     */
    public static Weapon woodenBow() {
        return new Weapon("Wooden Bow", 4, 1, 0, Shape.L_SHAPE, Rarity.COMMON, 8);
    }

    /**
     * Returns the damage dealt by the weapon.
     * 
     * @return the damage dealt by the weapon
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the energy cost to use the weapon.
     * 
     * @return the energy cost to use the weapon
     */
    public int getEnergyCost() {
        return energyCost;
    }

    /**
     * Returns the mana cost to use the weapon.
     * 
     * @return the mana cost to use the weapon
     */
    public int getManaCost() {
        return manaCost;
    }
}