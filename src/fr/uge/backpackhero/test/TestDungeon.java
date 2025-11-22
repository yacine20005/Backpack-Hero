package fr.uge.backpackhero.test;

import fr.uge.backpackhero.model.level.Dungeon;

public class TestDungeon {
    public static void main(String[] args) {
        IO.println("Testing Dungeon...");
        Dungeon dungeon = new Dungeon();

        IO.println("Floor Count: " + dungeon.getFloorCount());

        for (int i = 0; i < dungeon.getFloorCount(); i++) {
            IO.println("\nFloor " + (i + 1) + ":");
            IO.println(dungeon.getFloor(i));
        }
    }
}
