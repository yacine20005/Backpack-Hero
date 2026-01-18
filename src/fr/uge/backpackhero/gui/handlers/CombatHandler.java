package fr.uge.backpackhero.gui.handlers;

import java.util.Objects;
import com.github.forax.zen.ApplicationContext;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.CombatEngine;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.State;
import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.entity.Hero;
import fr.uge.backpackhero.model.loot.LootTables;

public class CombatHandler {
    private final GameState state;
    private final View view;

    public CombatHandler(GameState state, View view) {
        this.state = Objects.requireNonNull(state);
        this.view = Objects.requireNonNull(view);
    }

    /**
     * Handles the end turn action initiated by the user.
     * 
     * @param context the application context
     */
    public void handleEndTurn(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getState() != State.COMBAT) {
            return;
        }

        state.getCombatEngine().endHeroTurn(state.getHero());
        IO.println("Turn ended.");

        processEnemiesTurn(context);
        view.draw(context);
    }

    /**
     * Handles game logic after a hero action (e.g., using an item).
     * 
     * @param context the application context
     */
    public void afterHeroAction(ApplicationContext context) {
        if (checkEndOfCombat(context))
            return;
        processEnemiesTurn(context);
        view.draw(context);
    }

    private void processEnemiesTurn(ApplicationContext context) {
        var combat = state.getCombatEngine();
        var hero = state.getHero();
        var enemies = combat.getCurrentEnemies();
        if (hero.getEnergy() != 0) {
            return;
        }
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                var action = combat.getEnemyIntent(enemy);
                if (action != null) {
                    combat.enemyTurn(hero, enemy, action);
                }
            }
        }
        if (!hero.isAlive()) {
            System.out.println("The hero is dead.");
            state.setGameOver(true);
            state.setState(State.EXPLORATION);
            combat.endCombat();
            view.draw(context);
            return;
        }
        combat.decideEnemyIntents();
        combat.heroTurn(hero, state.getBackpack());
    }

    private boolean checkEndOfCombat(ApplicationContext context) {
        CombatEngine combat = state.getCombatEngine();
        Hero hero = state.getHero();

        if (!combat.isCombatOver(hero)) {
            return false;
        }

        if (!hero.isAlive()) {
            System.out.println("The hero is dead.");
            state.setGameOver(true);
            state.setState(State.EXPLORATION);
            combat.endCombat();
            view.draw(context);
            return true;
        }

        // Calculate rewards
        var lootItems = LootTables.generateLootFromEnemies(combat.getCurrentEnemies(), state.getFloor());
        int goldReward = combat.calculateGoldReward();
        int xpReward = combat.calculateXpReward();

        // Give rewards
        state.getBackpack().addGold(goldReward);
        int levelsGained = hero.addXp(xpReward);

        // Handle level up - enter cell unlock mode
        if (levelsGained > 0) {
            // Calculate total cells to unlock for all levels
            int totalCells = 0;
            for (int i = 0; i < levelsGained; i++) {
                int levelNum = hero.getLevel() - levelsGained + i + 1;
                totalCells += (levelNum % 2 == 0) ? 4 : 3;
            }
            state.startCellUnlockMode(totalCells);
            IO.println("LEVEL UP! Level " + hero.getLevel() + " - Choose " + totalCells + " cells to unlock!");
        }

        state.openLootScreen(lootItems);
        IO.println("Combat won! Gained " + goldReward + " gold, " + xpReward + " XP and " + lootItems.size()
                + " items to choose from.");
        if (levelsGained > 0) {
            IO.println("You gained " + levelsGained + " level(s)! Now level " + hero.getLevel());
        }
        view.draw(context);
        return true;
    }
}
