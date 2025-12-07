package fr.uge.backpackhero.gui;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.logic.CombatEngine;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.entity.Hero;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.Weapon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;

/**
 * Controller class to handle user interactions and game logic.
 * It processes clicks in the backpack and dungeon areas, manages combat
 * actions, and updates the game state accordingly.
 * 
 * @author @Naniiiii944
 */
public class Controller {

    private Controller() {
        // Private constructor to prevent warnings
    }

    /**
     * Handles a click event in the backpack area.
     * 
     * @param context      the application context
     * @param state        the current game state
     * @param pointerEvent the pointer event representing the click
     */
    public static void handleBackpackClick(ApplicationContext context, GameState state, PointerEvent pointerEvent) {
        int x = (int) (pointerEvent.location().x() / View.TILE_SIZE);
        int y = (int) ((pointerEvent.location().y() - View.TILE_SIZE) / View.TILE_SIZE);
        if (y < 0)
            return;
        var pos = new Position(x, y);
        var optItem = state.getBackpack().getItemAt(pos);
        if (optItem.isEmpty()) {
            return;
        }

        var item = optItem.get();
        if (!state.isInCombat()) {
            IO.println(item.toString());
            return;
        }

        if (!useItemInCombat(state, item)) {
            IO.println("Cannot use item in combat.");
            View.draw(context, state);
            return;
        }

        afterHeroAction(context, state);
    }

    /**
     * Uses the given item during combat if possible.
     * A weapon triggers an attack on the first alive enemy, an armor triggers a
     * defend action.
     *
     * @param state the current game state
     * @param item  the item clicked in the backpack
     * @return true if the combat action was successfully used, false otherwise
     */
    static boolean useItemInCombat(GameState state, Item item) {
        var combat = state.getCombatEngine();
        var hero = state.getHero();
        var enemies = combat.getCurrentEnemies();

        if (item instanceof Weapon weapon) {
            Enemy target = null;
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    target = enemy;
                    break;
                }
            }
            if (target == null)
                return false;
            return combat.heroAttack(hero, target, weapon);
        }

        if (item instanceof Armor armor) {
            return combat.heroDefend(hero, armor);
        }

        return false;
    }

    /**
     * Handles a click event in the dungeon area.
     * 
     * @param context      the application context
     * @param state        the current game state
     * @param pointerEvent the pointer event representing the click
     */
    public static void handleDungeonClick(ApplicationContext context, GameState state, PointerEvent pointerEvent) {
        if (state.isInCombat()) {
            return;
        }

        int x = (int) ((pointerEvent.location().x() - View.BACKPACK_PIXEL_WIDTH) / View.TILE_SIZE);
        int y = (int) (pointerEvent.location().y() / View.TILE_SIZE);
        var clickedPos = new Position(x, y);
        var floor = state.getCurrentFloor();
        if (!isMoveAllowed(state.getPosition(), clickedPos, floor))
            return;
        state.setPosition(clickedPos);
        var room = floor.getRoom(clickedPos);
        if (room != null && room.getType() == RoomType.ENEMY) {
            var enemies = room.getEnemies();
            if (enemies != null && !enemies.isEmpty()) {
                var combat = state.getCombatEngine();
                combat.startCombat(enemies);
                combat.heroTurn(state.getHero(), state.getBackpack());
            }
        }
        View.draw(context, state);
    }

    /**
     * Checks if a move from the current position to the target position is allowed
     * on the given floor.
     * 
     * @param current the current position
     * @param target  the target position
     * @param floor   the floor on which the move is attempted
     * @return true if the move is allowed, false otherwise
     */
    static boolean isMoveAllowed(Position current, Position target, Floor floor) {
        if (!target.checkBounds(floor.getWidth(), floor.getHeight())) {
            return false;
        }
        if (floor.getRoom(target) == null) {
            return false;
        }
        int deltaX = Math.abs(current.x() - target.x());
        int deltaY = Math.abs(current.y() - target.y());
        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }

    /**
     * Performs actions after the hero has taken an action in combat.
     * 
     * @param context the application context
     * @param state   the current game state
     */
    static void afterHeroAction(ApplicationContext context, GameState state) {
        if (checkEndOfCombat(context, state))
            return;
        processEnemiesTurn(context, state);
        View.draw(context, state);
    }

    static void processEnemiesTurn(ApplicationContext context, GameState state) {
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
            IO.println("The hero is dead.");
            combat.endCombat();
            View.draw(context, state);
            return;
        }
        combat.decideEnemyIntents();
        combat.heroTurn(hero, state.getBackpack());
    }

    /**
     * Checks if the combat has ended and handles the end of combat logic.
     * 
     * @param context the application context
     * @param state   the current game state
     * @return true if the combat has ended, false otherwise
     */
    static boolean checkEndOfCombat(ApplicationContext context, GameState state) {
        CombatEngine combat = state.getCombatEngine();
        Hero hero = state.getHero();
        if (!combat.isCombatOver(hero))
            return false;
        if (!hero.isAlive()) {
            IO.println("The hero is dead.");
            combat.endCombat();
            View.draw(context, state);
            return true;
        }
        var floor = state.getCurrentFloor();
        var pos = state.getPosition();
        floor.setRoom(pos, new Room(RoomType.CORRIDOR, null, null, null, 0, 0));
        combat.endCombat();
        IO.println("Combat won.");
        View.draw(context, state);
        return true;
    }

}
