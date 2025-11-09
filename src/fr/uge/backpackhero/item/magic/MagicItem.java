package fr.uge.backpackhero.item.magic;

import fr.uge.backpackhero.item.Equipment;

public class MagicItem extends Equipment {

    private final int manaCost;
    private final int damage;

    public MagicItem(String name, int manaCost, int damage) {
        super(name);
        this.manaCost = manaCost;
        this.damage = damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getDamage() {
        return damage;
    }
}
