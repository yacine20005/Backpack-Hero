package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.uge.backpackhero.model.enemy.Enemy;
import fr.uge.backpackhero.model.enemy.PetitRatLoup;
import fr.uge.backpackhero.model.enemy.RatLoup;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Gold;
import fr.uge.backpackhero.model.item.ManaStone;
import fr.uge.backpackhero.model.item.Weapon;

public class Dungeon {
        private final List<Floor> floors;

        public Dungeon() {
                this.floors = new ArrayList<>();
                initializeFloors();
        }

        private void initializeFloors() {

                // Floor 1
                Floor floor1 = new Floor(5, 5);

                floor1.setRoom(new Position(1, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
                List<Enemy> enemy_1 = List.of(new PetitRatLoup(), new PetitRatLoup());
                floor1.setRoom(new Position(2, 0), new Room(RoomType.ENEMY, enemy_1, null, null, 0));
                floor1.setRoom(new Position(2, 0), new Room(RoomType.ENEMY, enemy_1, null, null, 0));
                floor1.setRoom(new Position(3, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
                List<Item> treasure_1 = List.of(new Weapon("Sword", 7, 1, 0), new Armor("Wooden Shield", 6, 1),
                                new Gold(5));
                floor1.setRoom(new Position(4, 0), new Room(RoomType.TREASURE, null, treasure_1, null, 0));
                floor1.setRoom(new Position(2, 1), new Room(RoomType.CORRIDOR, null, null, null, 0));
                HashMap<Item, Integer> merchant_1 = new HashMap<>();
                merchant_1.put(new Weapon("Iron Sword", 10, 2, 0), 15);
                merchant_1.put(new Armor("Steel Shield", 8, 2), 12);
                merchant_1.put(new ManaStone("Blue Crystal", 5), 8);
                floor1.setRoom(new Position(2, 2), new Room(RoomType.MERCHANT, null, null, merchant_1, 0));

                floors.add(floor1);

                // Floor 2
                Floor floor2 = new Floor(5, 5);

                floor2.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
                List<Enemy> enemy_2 = List.of(new RatLoup());
                floor2.setRoom(new Position(1, 0), new Room(RoomType.ENEMY, enemy_2, null, null, 0));
                floor2.setRoom(new Position(2, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
                floor2.setRoom(new Position(3, 0), new Room(RoomType.HEALER, null, null, null, 20));
                floor2.setRoom(new Position(4, 0), new Room(RoomType.EXIT, null, null, null, 0));

                floors.add(floor2);

                // Floor 3
                Floor floor3 = new Floor(5, 5);

                floor3.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
                List<Enemy> enemy_3a = List.of(new PetitRatLoup(), new RatLoup());
                floor3.setRoom(new Position(1, 0), new Room(RoomType.ENEMY, enemy_3a, null, null, 0));
                List<Enemy> enemy_3b = List.of(new RatLoup(), new RatLoup());
                floor3.setRoom(new Position(2, 0), new Room(RoomType.ENEMY, enemy_3b, null, null, 0));
                floor3.setRoom(new Position(3, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
                floor3.setRoom(new Position(4, 0), new Room(RoomType.EXIT, null, null, null, 0));

                floors.add(floor3);
        }

        public Floor getFloor(int index) {
                if (index < 0 || index >= floors.size()) {
                        throw new IndexOutOfBoundsException("Floor index out of bounds: " + index);
                }
                return floors.get(index);
        }

        public int getFloorCount() {
                return floors.size();
        }
}
