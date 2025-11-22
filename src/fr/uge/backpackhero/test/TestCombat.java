package fr.uge.backpackhero.test;

import fr.uge.backpackhero.model.Hero;
import fr.uge.backpackhero.model.CombatEngine;
import fr.uge.backpackhero.model.enemy.RatLoup;
import fr.uge.backpackhero.model.enemy.Enemy;

public class TestCombat {
    public static void main(String[] args) {
        IO.println("=== Starting Combat Test ===");

        Hero hero = new Hero();
        CombatEngine engine = new CombatEngine();
        Enemy rat = new RatLoup();

        engine.startTurn(hero);
        IO.println(hero);

        IO.println("\n--- Enemy Attack (No Block) ---");

        for (int i = 0; i < 5; i++) {
            IO.println("Turn " + (i + 1));
            engine.enemyTurn(hero, rat);
            IO.println(hero);
        }

        IO.println("\n--- Block Absorption Test ---");
        hero.setBlock(10);
        IO.println("Hero Block set to 10.");
        new fr.uge.backpackhero.model.enemy.Attack().execute(hero, rat); // Call the attack action by creating a new instance of it
        IO.println(hero);

        IO.println("=== Test Complete ===");
    }
}
