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
     * @param name the name of the weapon
     * @param damage the damage dealt by the weapon
     * @param energyCost the energy cost to use the weapon
     * @param manaCost the mana cost to use the weapon
     * @param shape the shape of the weapon
     */
    public Weapon(String name, int damage, int energyCost, int manaCost, Shape shape) {
        super(name, shape);
        this.damage = damage;
        this.energyCost = energyCost;
        this.manaCost = manaCost;
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