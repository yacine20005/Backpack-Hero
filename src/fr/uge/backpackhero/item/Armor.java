package fr.uge.backpackhero.item;

public class Armor extends Item {

    private final int protection;
    private final int energyCost;

    public Armor(String name, int protection, int energyCost) {
        super(name);
        this.protection = protection;
        this.energyCost = energyCost;
    }

    public int getProtection() {
        return protection;
    }

    public int getEnergyCost() {
        return energyCost;
    }
}
