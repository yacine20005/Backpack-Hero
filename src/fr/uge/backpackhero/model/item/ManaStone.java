package fr.uge.backpackhero.model.item;

public class ManaStone extends Item {

    private final int manaProvided;

    public ManaStone(String name, int manaProvided, Shape shape) {
        super(name, shape);
        this.manaProvided = manaProvided;
    }
    
    public ManaStone(String name, int manaProvided) {
        this(name, manaProvided, Shape.SINGLE);
    }

    public int getManaProvided() {
        return manaProvided;
    }

    public boolean isManaStone() {
        return true;
    }
}
