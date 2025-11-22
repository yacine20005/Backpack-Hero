package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.List;

import fr.uge.backpackhero.model.level.room.CorridorRoom;
import fr.uge.backpackhero.model.level.room.EnemyRoom;
import fr.uge.backpackhero.model.level.room.ExitRoom;
import fr.uge.backpackhero.model.level.room.HealerRoom;
import fr.uge.backpackhero.model.level.room.MerchantRoom;
import fr.uge.backpackhero.model.level.room.TreasureRoom;

public class Dungeon {
    private final List<Floor> floors;

    public Dungeon() {
        this.floors = new ArrayList<>();
        initializeFloors();
    }

    private void initializeFloors() {

        // Floor 1
        Floor floor1 = new Floor(5, 5);

        floor1.setRoom(new Position(0, 0), new CorridorRoom());
        floor1.setRoom(new Position(1, 0), new CorridorRoom());
        floor1.setRoom(new Position(2, 0), new EnemyRoom());
        floor1.setRoom(new Position(3, 0), new CorridorRoom());
        floor1.setRoom(new Position(4, 0), new TreasureRoom());
        floor1.setRoom(new Position(2, 1), new CorridorRoom());
        floor1.setRoom(new Position(2, 2), new MerchantRoom());

        floors.add(floor1);

        // Floor 2
        Floor floor2 = new Floor(5, 5);

        floor2.setRoom(new Position(0, 0), new CorridorRoom());
        floor2.setRoom(new Position(1, 0), new EnemyRoom());
        floor2.setRoom(new Position(2, 0), new CorridorRoom());
        floor2.setRoom(new Position(3, 0), new HealerRoom());
        floor2.setRoom(new Position(4, 0), new ExitRoom());

        floors.add(floor2);

        // Floor 3
        Floor floor3 = new Floor(5, 5);

        floor3.setRoom(new Position(0, 0), new CorridorRoom());
        floor3.setRoom(new Position(1, 0), new EnemyRoom());
        floor3.setRoom(new Position(2, 0), new EnemyRoom());
        floor3.setRoom(new Position(3, 0), new CorridorRoom());
        floor3.setRoom(new Position(4, 0), new ExitRoom());

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
