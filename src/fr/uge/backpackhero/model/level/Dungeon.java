package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.Enemy;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Gold;
import fr.uge.backpackhero.model.item.ManaStone;
import fr.uge.backpackhero.model.item.Weapon;

/**
 * Represents a dungeon consisting of multiple floors.
 * Each floor contains rooms with various types such as enemies, treasures,
 * merchants, etc.
 * 
 * @author Yacine
 */
public class Dungeon {
    private final List<Floor> floors;

    /**
     * Creates a new Dungeon with multiple floors predefined for the beta of the
     * game.
     * The dungeon consists of 3 floors.
     */
    public Dungeon() {
        this.floors = new ArrayList<>();
        initializeFloors();
    }

    /**
     * Initializes the floors of the dungeon with predefined rooms and
     * configurations.
     * 
     * pretty long method but it's only for the beta version of the game trust me
     * soon it'll be replaced by a procedural generation algorithm that will
     * outperform the ones from the biggest AAA games.
     */
    private void initializeFloors() {

        // Floor 1
        Floor floor1 = new Floor(11, 5);

        floor1.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));

        floor1.setRoom(new Position(1, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));

        List<Enemy> enemy_1 = List.of(Enemy.petitRatLoup(), Enemy.petitRatLoup());
        floor1.setRoom(new Position(2, 0), new Room(RoomType.ENEMY, enemy_1, null, null, 0));

        floor1.setRoom(new Position(2, 1), new Room(RoomType.CORRIDOR, null, null, null, 0));

        List<Item> treasure_1 = List.of(Weapon.woodSword(), Armor.woodenShield(), new Gold(5));
        floor1.setRoom(new Position(3, 1), new Room(RoomType.TREASURE, null, treasure_1, null, 0));

        floor1.setRoom(new Position(4, 1), new Room(RoomType.CORRIDOR, null, null, null, 0));

        List<Enemy> enemy_2 = List.of(Enemy.petitRatLoup());
        floor1.setRoom(new Position(5, 1), new Room(RoomType.ENEMY, enemy_2, null, null, 0));

        floor1.setRoom(new Position(5, 0), new Room(RoomType.HEALER, null, null, null, 20));

        floor1.setRoom(new Position(5, 2), new Room(RoomType.CORRIDOR, null, null, null, 0));

        HashMap<Item, Integer> merchant_1 = new HashMap<>();
        merchant_1.put(Weapon.montaintop(), 15);
        merchant_1.put(Armor.celestialnighthawk(), 12);
        merchant_1.put(ManaStone.blueCrystal(), 8);
        floor1.setRoom(new Position(5, 3), new Room(RoomType.MERCHANT, null, null, merchant_1, 0));

        floor1.setRoom(new Position(6, 2), new Room(RoomType.CORRIDOR, null, null, null, 0));

        List<Enemy> enemy_3 = List.of(Enemy.ratLoup());
        floor1.setRoom(new Position(7, 2), new Room(RoomType.ENEMY, enemy_3, null, null, 0));

        floor1.setRoom(new Position(8, 2), new Room(RoomType.CORRIDOR, null, null, null, 0));

        floor1.setRoom(new Position(8, 1), new Room(RoomType.CORRIDOR, null, null, null, 0));

        List<Item> treasure_2 = List.of(new Gold(20));
        floor1.setRoom(new Position(8, 0), new Room(RoomType.TREASURE, null, treasure_2, null, 0));

        floor1.setRoom(new Position(9, 2), new Room(RoomType.CORRIDOR, null, null, null, 0));

        floor1.setRoom(new Position(10, 2), new Room(RoomType.EXIT, null, null, null, 0));

        floors.add(floor1);

        // Floor 2
        Floor floor2 = new Floor(11, 5);

        floor2.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));

        List<Enemy> enemy_f2 = List.of(Enemy.ratLoup());
        floor2.setRoom(new Position(1, 0), new Room(RoomType.ENEMY, enemy_f2, null, null, 0));

        floor2.setRoom(new Position(2, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));

        floor2.setRoom(new Position(3, 0), new Room(RoomType.HEALER, null, null, null, 20));

        floor2.setRoom(new Position(4, 0), new Room(RoomType.EXIT, null, null, null, 0));

        floors.add(floor2);

        // Floor 3
        Floor floor3 = new Floor(11, 5);

        floor3.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        List<Enemy> enemy_3a = List.of(Enemy.petitRatLoup(), Enemy.ratLoup());
        floor3.setRoom(new Position(1, 0), new Room(RoomType.ENEMY, enemy_3a, null, null, 0));

        List<Enemy> enemy_3b = List.of(Enemy.ratLoup(), Enemy.ratLoup());
        floor3.setRoom(new Position(2, 0), new Room(RoomType.ENEMY, enemy_3b, null, null, 0));

        floor3.setRoom(new Position(3, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));

        floor3.setRoom(new Position(4, 0), new Room(RoomType.EXIT, null, null, null, 0));

        floors.add(floor3);
    }

    /**
     * Returns the floor at the specified index.
     * 
     * @param index the index of the floor to retrieve
     * @return the floor at the specified index
     */
    public Floor getFloor(int index) {
        if (index < 0 || index >= floors.size()) {
            throw new IndexOutOfBoundsException("Floor index out of bounds: " + index);
        }
        return floors.get(index);
    }

    /**
     * Returns the total number of floors in the dungeon.
     * 
     * @return the number of floors
     */
    public int getFloorCount() {
        return floors.size();
    }
}