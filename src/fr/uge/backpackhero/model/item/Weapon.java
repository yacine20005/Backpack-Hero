package fr.uge.backpackhero.model.item;

import java.util.Objects;

/**
 * Represents a weapon item that can be used by the hero to deal damage.
 * Weapons have damage, energy cost, and mana cost attributes.
 * 
 * @author Yacine
 */
public final class Weapon implements Item {

    private final String name;
    private Shape shape;
    private final Rarity rarity;
    private final int price;
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
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.shape = Objects.requireNonNull(shape, "shape cannot be null");
        this.rarity = Objects.requireNonNull(rarity, "rarity cannot be null");
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        this.price = price;
        this.damage = damage;
        this.energyCost = energyCost;
        this.manaCost = manaCost;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Shape getShape() {
        return shape;
    }
    
    @Override
    public void setShape(Shape shape) {
        this.shape = Objects.requireNonNull(shape, "shape cannot be null");
    }
    
    @Override
    public Rarity getRarity() {
        return rarity;
    }
    
    @Override
    public int getPrice() {
        return price;
    }

    /**
     * Creates a Wooden Sword melee weapon.
     * 
     * @return a new Wooden Sword weapon instance
     */
    public static Weapon woodenSword() {
        return new Weapon("Wooden Sword", 5, 1, 0, Shape.SINGLE, Rarity.COMMON, 10);
    }

    /**
     * Creates a Wooden Bow ranged weapon.
     * 
     * @return a new Wooden Bow weapon instance
     */
    public static Weapon woodenBow() {
        return new Weapon("Wooden Bow", 8, 2, 0, Shape.SINGLE, Rarity.COMMON, 8);
    }

    /**
     * Creates an Iron Sword melee weapon.
     * 
     * @return a new Iron Sword weapon instance
     */
    public static Weapon ironSword() {
        return new Weapon("Iron Sword", 12, 1, 0, Shape.VERTICAL_2, Rarity.UNCOMMON, 25);
    }

    /**
     * Creates an Iron Bow ranged weapon.
     * 
     * @return a new Iron Bow weapon instance
     */
    public static Weapon ironBow() {
        return new Weapon("Iron Bow", 15, 2, 0, Shape.HORIZONTAL_2, Rarity.UNCOMMON, 20);
    }

    /**
     * Creates a Golden Sword melee weapon.
     * 
     * @return a new Golden Sword weapon instance
     */
    public static Weapon goldenSword() {
        return new Weapon("Golden Sword", 20, 1, 0, Shape.VERTICAL_3, Rarity.RARE, 40);
    }

    /**
     * Creates a Golden Bow ranged weapon.
     * 
     * @return a new Golden Bow weapon instance
     */
    public static Weapon goldenBow() {
        return new Weapon("Golden Bow", 18, 2, 0, Shape.HORIZONTAL_3, Rarity.RARE, 35);
    }

    /**
     * Creates a Diamond Sword melee weapon.
     * 
     * @return a new Diamond Sword weapon instance
     */
    public static Weapon diamondSword() {
        return new Weapon("Diamond Sword", 25, 1, 0, Shape.SQUARE_2X2, Rarity.EPIC, 70);
    }

    /**
     * Creates a Diamond Bow ranged weapon.
     * 
     * @return a new Diamond Bow weapon instance
     */
    public static Weapon diamondBow() {
        return new Weapon("Diamond Bow", 22, 2, 0, Shape.SQUARE_2X2, Rarity.EPIC, 60);
    }

    /**
     * Creates a Sturn weapon, a revolver from Destiny 2.
     * 
     * @return a Sturn weapon
     */
    public static Weapon sturn() {
        return new Weapon("Sturn", 35, 3, 0, Shape.SQUARE_3X3, Rarity.EXOTIC, 100);
    }

    /**
     * Creates a Red Death weapon, a pulse rifle from Destiny 2.
     * 
     * @return a Red Death weapon
     */
    public static Weapon redDeath() {
        return new Weapon("Red Death", 30, 3, 0, Shape.SQUARE_3X3, Rarity.EXOTIC, 90);
    }

    /**
     * Creates a Jade Rabbit weapon, a scout rifle from Destiny 2.
     * 
     * @return a Jade Rabbit weapon
     */
    public static Weapon jadeRabbit() {
        return new Weapon("Jade Rabbit", 28, 3, 0, Shape.SQUARE_3X3, Rarity.EXOTIC, 80);
    }

    /**
     * Creates a Telesto weapon, a fusion rifle from Destiny 2.
     * 
     * @return a Telesto weapon
     */
    public static Weapon telesto() {
        return new Weapon("Telesto", 40, 3, 0, Shape.SQUARE_3X3, Rarity.EXOTIC, 150);
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