package fr.uge.backpackhero.model;

import java.util.HashMap;
import java.util.Map;

import fr.uge.backpackhero.item.Item;
import fr.uge.backpackhero.model.level.Position;

public class Backpack {

    private final int width = 3;
    private final int height = 5;

    private final Map<Position, Item> items; 
    
    public Backpack() {
        this.items = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Position, Item> getItems() {
        return Map.copyOf(items);
    }
}
