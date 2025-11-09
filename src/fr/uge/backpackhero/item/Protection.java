package fr.uge.backpackhero.item;

public abstract class Protection extends Equipment {

    private final int protection;
    private final int energyCost;

    public Protection(String name, int protection, int energyCost) {
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
