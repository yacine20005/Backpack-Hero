package fr.uge.backpackhero.item;

import java.util.Objects;

public abstract class Equipment implements Item {

    private final String name;

    public Equipment(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
