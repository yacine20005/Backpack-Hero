package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private final List<Floor> floors;

    public Dungeon() {
        this.floors = new ArrayList<>();
        initializeFloors();
    }

    private void initializeFloors() {

        // Floor 1
        Floor floor1 = new Floor(5, 5);

        floor1.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR));
        floor1.setRoom(new Position(1, 0), new Room(RoomType.CORRIDOR));
        floor1.setRoom(new Position(2, 0), new Room(RoomType.ENEMY));
        floor1.setRoom(new Position(3, 0), new Room(RoomType.CORRIDOR));
        floor1.setRoom(new Position(4, 0), new Room(RoomType.TREASURE));
        floor1.setRoom(new Position(2, 1), new Room(RoomType.CORRIDOR));
        floor1.setRoom(new Position(2, 2), new Room(RoomType.MERCHANT));

        floors.add(floor1);

        // Floor 2
        Floor floor2 = new Floor(5, 5);

        floor2.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR));
        floor2.setRoom(new Position(1, 0), new Room(RoomType.ENEMY));
        floor2.setRoom(new Position(2, 0), new Room(RoomType.CORRIDOR));
        floor2.setRoom(new Position(3, 0), new Room(RoomType.HEALER));
        floor2.setRoom(new Position(4, 0), new Room(RoomType.EXIT));

        floors.add(floor2);

        // Floor 3
        Floor floor3 = new Floor(5, 5);

        floor3.setRoom(new Position(0, 0), new Room(RoomType.CORRIDOR));
        floor3.setRoom(new Position(1, 0), new Room(RoomType.ENEMY));
        floor3.setRoom(new Position(2, 0), new Room(RoomType.ENEMY));
        floor3.setRoom(new Position(3, 0), new Room(RoomType.CORRIDOR));
        floor3.setRoom(new Position(4, 0), new Room(RoomType.EXIT));

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
