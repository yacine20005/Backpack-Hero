package fr.uge.backpackhero.model.item;

import java.util.Objects;

public abstract class Item {

    private final String name;
    public Shape shape;

    public Item(String name, Shape shape) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.shape = Objects.requireNonNull(shape, "shape cannot be null");
    }

    public String getName() {
        return name;
    }

    public Shape getShape() {
        return shape;
    }

    public boolean isManaStone() {
        return false;
    }

    public int getManaProvided() {
        return 0;
    }

    protected void setShape(Shape shape) {
        Objects.requireNonNull(shape, "shape cannot be null");
        this.shape = shape;
    }
}
