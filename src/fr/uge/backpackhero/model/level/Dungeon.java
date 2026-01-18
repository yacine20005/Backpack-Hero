package fr.uge.backpackhero.model.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.loot.LootTables;

/**
 * Represents a dungeon consisting of multiple floors.
 * Each floor is generated procedurally and respects the required room counts.
 * 
 */
public class Dungeon {

    private static final int FLOOR_COUNT = 3;
    private static final int WIDTH = 11;
    private static final int HEIGHT = 5;

    private static final int ENEMY_COUNT = 3;
    private static final int TREASURE_COUNT = 2;

    private final List<Floor> floors;
    private final Random rng;

    /**
     * Creates a new Dungeon with procedurally generated floors.
     */
    public Dungeon() {
        this.floors = new ArrayList<>();
        this.rng = new Random();

        for (int i = 0; i < FLOOR_COUNT; i++) {
            floors.add(createRandomFloor(i, rng));
        }
    }

    private Floor createRandomFloor(int floorIndex, Random rng) {
        Floor floor = new Floor(WIDTH, HEIGHT);

        List<Position> corridors = buildConnectedCorridors(rng);

        for (Position p : corridors) {
            floor.setRoom(p, new Room(RoomType.CORRIDOR, null, null, null, 0, 0));
        }

        placeSpecialRooms(floor, corridors, floorIndex, rng);

        return floor;
    }

    private List<Position> buildConnectedCorridors(Random rng) {
        var corridors = new ArrayList<Position>();
        var used = new HashSet<Position>();

        int x = 0;
        int y = 0;

        Position start = new Position(0, 0);
        corridors.add(start);
        used.add(start);

        while (x < WIDTH - 1) {
            int dir = rng.nextInt(3);

            if (dir == 0) {
                x++;
            } else if (dir == 1 && y > 0) {
                y--;
            } else if (dir == 2 && y < HEIGHT - 1) {
                y++;
            } else {
                x++;
            }

            Position p = new Position(x, y);
            if (used.add(p)) {
                corridors.add(p);
            }
        }

        int branches = 2 + rng.nextInt(3);
        for (int i = 0; i < branches; i++) {
            Position base = corridors.get(rng.nextInt(corridors.size()));
            addSmallBranch(corridors, used, base, rng);
        }

        return corridors;
    }

    private void addSmallBranch(List<Position> corridors, HashSet<Position> used, Position base, Random rng) {
        int len = 1 + rng.nextInt(3);
        int x = base.x();
        int y = base.y();

        for (int i = 0; i < len; i++) {
            int dir = rng.nextInt(4);
            int nx = x, ny = y;

            if (dir == 0)
                nx--;
            else if (dir == 1)
                nx++;
            else if (dir == 2)
                ny--;
            else
                ny++;

            if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) {
                return;
            }

            Position p = new Position(nx, ny);
            if (used.add(p)) {
                corridors.add(p);
            }

            x = nx;
            y = ny;
        }
    }

    private void placeSpecialRooms(Floor floor, List<Position> corridors, int floorIndex, Random rng) {
        Position exit = corridors.get(0);
        for (Position p : corridors) {
            if (p.x() > exit.x()) {
                exit = p;
            }
        }
        floor.setRoom(exit, new Room(RoomType.EXIT, null, null, null, 0, 0));

        var free = new ArrayList<>(corridors);
        free.remove(exit);
        free.remove(new Position(0, 0));

        // Merchant
        Position merchant = pickAndRemove(free, rng);
        floor.setRoom(merchant, new Room(RoomType.MERCHANT, null, null, merchantStock(floorIndex), 0, 0));

        // Healer
        Position healer = pickAndRemove(free, rng);
        floor.setRoom(healer, new Room(RoomType.HEALER, null, null, null, healerHeal(floorIndex), 0));

        // Treasures
        for (int i = 0; i < TREASURE_COUNT; i++) {
            Position t = pickAndRemove(free, rng);
            floor.setRoom(t, new Room(RoomType.TREASURE, null, treasureItems(floorIndex), null, 0, 0));
        }

        // Enemies
        for (int i = 0; i < ENEMY_COUNT; i++) {
            Position e = pickAndRemove(free, rng);
            floor.setRoom(e, new Room(RoomType.ENEMY, enemiesForFloor(floorIndex, rng), null, null, 0, 0));
        }
    }

    private Position pickAndRemove(List<Position> list, Random rng) {
        return list.remove(rng.nextInt(list.size()));
    }

    private int healerHeal(int floorIndex) {
        if (floorIndex == 0)
            return 20;
        if (floorIndex == 1)
            return 25;
        return 30;
    }

    private List<Enemy> enemiesForFloor(int floorIndex, Random rng) {
        int enemyCount = 1 + rng.nextInt(3); // 1 to 3 enemies

        if (floorIndex == 0) {
            // Floor 0: Easy enemies
            return switch (enemyCount) {
                case 1 -> List.of(Enemy.ratWolf());
                case 2 -> List.of(Enemy.smallRatWolf(), Enemy.smallRatWolf());
                default -> List.of(Enemy.smallRatWolf(), Enemy.smallRatWolf(), Enemy.ratWolf());
            };
        }

        if (floorIndex == 1) {
            // Floor 1: Medium enemies
            return switch (enemyCount) {
                case 1 -> List.of(Enemy.goblin());
                case 2 -> List.of(Enemy.goblin(), Enemy.frogWizard());
                default -> List.of(Enemy.goblin(), Enemy.goblin(), Enemy.goblinChief());
            };
        }

        // Floor 2: Hard enemies (including new Phase 3 enemies)
        return switch (enemyCount) {
            case 1 -> List.of(Enemy.demon());
            case 2 -> List.of(Enemy.demon(), Enemy.livingShadow());
            default -> List.of(Enemy.demon(), Enemy.demonKing(), Enemy.beeQueen());
        };
    }

    private List<Item> treasureItems(int floorIndex) {
        return LootTables.generateLoot(floorIndex, 2, rng);
    }

    private HashMap<Item, Integer> merchantStock(int floorIndex) {
        var shop = new HashMap<Item, Integer>();
        var items = LootTables.generateLoot(floorIndex, 3, rng);

        for (var item : items) {
            int price = item.getPrice();
            shop.put(item, price);
        }

        return shop;
    }

    /**
     * Returns the floor at the specified index.
     * 
     * @param index the index of the floor to retrieve
     * @return the floor at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
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
