package fr.uge.backpackhero.model.item;

public class Weapon extends Item {

    private final int damage;
    private final int energyCost;
    private final int manaCost;

    public Weapon(String name, int damage, int energyCost, int manaCost, Shape shape) {
        super(name, shape);
        this.damage = damage;
        this.energyCost = energyCost;
        this.manaCost = manaCost;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getManaCost() {
        return manaCost;
    }
}