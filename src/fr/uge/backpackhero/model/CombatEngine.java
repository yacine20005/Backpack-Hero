package fr.uge.backpackhero.model;

import java.util.List;
import java.util.Random;

public class CombatEngine {

    private final Random random = new Random();

    public void initializeHeroTurn(Hero hero) {
        hero.setEnergy(3);
        hero.setBlock(0); // We reset the block at the start of the turn like in Slay the Spire
        // TODO : verify if it's correct in Backpack Hero
    }

    public void enemyAttack(Hero hero, Enemy enemy) {
        int damage = enemy.getAttack();
        int heroBlock = hero.getBlock();

        if (heroBlock >= damage) {
            hero.setBlock(heroBlock - damage);
        } else {
            hero.setBlock(0);
            hero.setHp(hero.getHp() - (damage - heroBlock));
        }
    }

    public void enemyDefend(Enemy enemy) {
        enemy.setBlock(enemy.getDefense());
    }

    public void enemyTurn(Hero hero, Enemy enemy) {
        if (random.nextInt(2) == 0) {
            enemyAttack(hero, enemy);
        } else {
            enemyDefend(enemy);
        }
    }

    public boolean isCombatOver(Hero hero, List<Enemy> enemies) {
        if (!hero.isAlive()) {
            return true; // Hero is defeated
        }
        boolean allEnemiesDefeated = enemies.stream().allMatch(enemy -> !enemy.isAlive());
        return allEnemiesDefeated;
    }

    public boolean loopCombat(Hero hero, List<Enemy> enemies) {
        while (!isCombatOver(hero, enemies)) {
            // TODO : player turn logic
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    enemyTurn(hero, enemy);
                    if (!hero.isAlive()) {
                        return false;
                    }
                }
            }
        }
        return hero.isAlive(); // Return true if hero wins, false otherwise
    }
}
