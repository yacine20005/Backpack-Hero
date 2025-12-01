package fr.uge.backpackhero.model.item;

import java.util.Objects;

public abstract class Item {

    private final String name;
    private Shape shape;

    public Item(String name, Shape shape) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(shape, "shape cannot be null");
        this.name = name;
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public Shape getShape() {
        return shape;
    }

    public void rotate() {
        this.shape = shape.rotate90();
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
