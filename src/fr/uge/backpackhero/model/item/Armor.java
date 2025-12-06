package fr.uge.backpackhero.model.item;

public class Armor extends Item {

    private final int protection;
    private final int energyCost;

    public Armor(String name, int protection, int energyCost, Shape shape) {
        if (protection < 0 || energyCost < 0) {
            throw new IllegalArgumentException("Protection and energy cost must be non-negative");
        }
        super(name, shape);
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
