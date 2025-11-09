package fr.uge.backpackhero.item.magic;

import fr.uge.backpackhero.item.Equipment;

public class ManaStone extends Equipment {

    private final int manaProvided;

    public ManaStone(String name, int manaProvided) {
        super(name);
        this.manaProvided = manaProvided;
    }

    public int getManaProvided() {
        return manaProvided;
    }
}
