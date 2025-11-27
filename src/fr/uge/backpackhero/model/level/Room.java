package fr.uge.backpackhero.model.level;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.uge.backpackhero.model.Enemy;
import fr.uge.backpackhero.model.item.Item;

public class Room {
    private final RoomType type;
    private final List<Enemy> enemies;
    private final List<Item> treasureItems;
    private final HashMap<Item, Integer> merchantItems;
    private final int healAmount;  

    public Room(RoomType type, List<Enemy> enemies, List<Item> treasureItems, HashMap<Item, Integer> merchantItems, int healAmount) {
        this.type = Objects.requireNonNull(type);
        this.enemies = enemies;
        this.treasureItems = treasureItems;
        this.merchantItems = merchantItems;
        this.healAmount = healAmount;
    }

    public RoomType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public int getHealAmount() {
        return healAmount;
    }
    
    public HashMap<Item, Integer> getMerchantItems() {
        return merchantItems;
    }

    public List<Item> getTreasureItems() {
        return treasureItems;
    }
}
