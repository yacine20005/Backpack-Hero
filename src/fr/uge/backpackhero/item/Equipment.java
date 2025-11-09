package fr.uge.backpackhero.item;

public abstract class Equipment implements Item {

    private final String name;

    public Equipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
