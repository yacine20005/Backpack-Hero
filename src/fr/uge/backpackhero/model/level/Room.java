package fr.uge.backpackhero.model.level;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.uge.backpackhero.model.Enemy;
import fr.uge.backpackhero.model.item.Item;

/**
 * Represents a room in the dungeon.
 * A room can be of different types such as enemy rooms, treasure rooms,
 * corridors, etc.
 * Each room can contain enemies, treasure items, merchant items, and healing
 * amounts.
 * If the room is a floor exit, it allows the player to move to the next floor.
 * Usually, rooms have one of the attributes set and the others to null or zero
 * depending on the room type.
 * 
 * @author Yacine
 */
public class Room {
    private final RoomType type;
    private final List<Enemy> enemies;
    private final List<Item> treasureItems;
    private final HashMap<Item, Integer> merchantItems;
    private final int healAmount;

    /**
     * Creates a new Room with the specified attributes.
     * Like said before, usually only one of the attributes is set depending on the
     * room type.
     * 
     * @param type          the type of the room
     * @param enemies       the list of enemies in the room
     * @param treasureItems the list of treasure items in the room
     * @param merchantItems the map of merchant items and their prices in the room
     * @param healAmount    the amount of healing available in the room
     */
    public Room(RoomType type, List<Enemy> enemies, List<Item> treasureItems, HashMap<Item, Integer> merchantItems,
            int healAmount) {
        this.type = Objects.requireNonNull(type);
        this.enemies = enemies;
        this.treasureItems = treasureItems;
        this.merchantItems = merchantItems;
        this.healAmount = healAmount;
    }

    /**
     * Returns the type of the room.
     * 
     * @return the type of the room
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Returns a string representation of the room, showing its type.
     * 
     * @return a string representation of the room
     */
    @Override
    public String toString() {
        return type.toString();
    }

    /**
     * Returns the list of enemies in the room.
     * 
     * @return the list of enemies in the room
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the amount of healing available in the room.
     * 
     * @return the amount of healing available in the room
     */
    public int getHealAmount() {
        return healAmount;
    }

    /**
     * Returns the map of merchant items and their prices in the room.
     * 
     * @return the map of merchant items and their prices in the room
     */
    public HashMap<Item, Integer> getMerchantItems() {
        return merchantItems;
    }

    /**
     * Returns the list of treasure items in the room.
     * 
     * @return the list of treasure items in the room
     */
    public List<Item> getTreasureItems() {
        return treasureItems;
    }
}
