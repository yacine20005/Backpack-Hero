package fr.uge.backpackhero.test;

import fr.uge.backpackhero.model.Hero;
import fr.uge.backpackhero.model.Backpack;
import fr.uge.backpackhero.model.CombatEngine;
import fr.uge.backpackhero.model.Enemy;

/**
 * TestCombat class to test combat mechanics between Hero and Enemy.
 * 
 * @author Yacine
 */
public class TestCombat {

    private TestCombat() {
        // Private constructor to prevent warnings
    }

    /**
     * Main method to run combat tests.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        IO.println("=== Starting Combat Test ===");

        Hero hero = new Hero();
        Backpack backpack = new Backpack(5, 5);
        CombatEngine engine = new CombatEngine();
        Enemy rat = Enemy.ratLoup();

        engine.heroTurn(hero, backpack);
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
        engine.enemyAttack(hero, rat);
        IO.println(hero);

        IO.println("=== Test Complete ===");
    }
}
