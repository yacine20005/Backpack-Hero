package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.List;

import fr.uge.backpackhero.model.enemy.PetitRatLoup;
import fr.uge.backpackhero.model.enemy.RatLoup;
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

        floor1.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor1.setRoom(new Position(1, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor1.setRoom(new Position(2, 0), new Room(RoomType.ENEMY,
                List.of(new PetitRatLoup(), new PetitRatLoup()), null, null, 0));
        floor1.setRoom(new Position(3, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor1.setRoom(new Position(4, 0), new Room(RoomType.TREASURE, null,
                List.of(new Weapon("Sword", 7, 1, 0), new Armor("Wooden Shield", 6, 1), new Gold(5)), null, 0));
        floor1.setRoom(new Position(2, 1), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor1.setRoom(new Position(2, 2), new Room(RoomType.MERCHANT, null, null,
                List.of(new Weapon("Bow", 5, 1, 0), new Armor("Leather Armor", 2, 0),
                        new ManaStone("Mana Stone", 4)),
                0));

        floors.add(floor1);

        // Floor 2
        Floor floor2 = new Floor(5, 5);

        floor2.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor2.setRoom(new Position(1, 0), new Room(RoomType.ENEMY,
                List.of(new RatLoup()), null, null, 0));
        floor2.setRoom(new Position(2, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor2.setRoom(new Position(3, 0), new Room(RoomType.HEALER, null, null, null, 20));
        floor2.setRoom(new Position(4, 0), new Room(RoomType.EXIT, null, null, null, 0));

        floors.add(floor2);

        // Floor 3
        Floor floor3 = new Floor(5, 5);

        floor3.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR, null, null, null, 0));
        floor3.setRoom(new Position(1, 0), new Room(RoomType.ENEMY,
                List.of(new PetitRatLoup(), new RatLoup()), null, null, 0));
        floor3.setRoom(new Position(2, 0), new Room(RoomType.ENEMY,
                List.of(new RatLoup(), new RatLoup()), null, null, 0));
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
