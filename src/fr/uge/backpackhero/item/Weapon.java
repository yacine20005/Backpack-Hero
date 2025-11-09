package fr.uge.backpackhero.item;

public abstract class Weapon extends Equipment {

    private final int damage;
    private final int energyCost;

    public Weapon(String name, int damage, int energyCost) {
        super(name);
        this.damage = damage;
        this.energyCost = energyCost;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnergyCost() {
        return energyCost;
    }
}