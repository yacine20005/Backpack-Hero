package fr.uge.backpackhero.model.item;

public class ManaStone extends Item {

    private final int manaProvided;

    public ManaStone(String name, int manaProvided) {
        super(name);
        this.manaProvided = manaProvided;
    }

    public int getManaProvided() {
        return manaProvided;
    }
}
