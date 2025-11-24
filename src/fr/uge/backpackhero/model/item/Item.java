package fr.uge.backpackhero.model.item;

public abstract class Item {

    private final String name;
    
    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
