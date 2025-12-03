package fr.uge.backpackhero.engine;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.model.Backpack;
import fr.uge.backpackhero.model.CombatEngine;
import fr.uge.backpackhero.model.Enemy;
import fr.uge.backpackhero.model.GameState;
import fr.uge.backpackhero.model.Hero;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.Weapon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;

public class Controller {

    static void handleBackpackClick(ApplicationContext context, GameState state, PointerEvent pe) {
        int x = (int) (pe.location().x() / View.TILE_SIZE);
        int y = (int) ((pe.location().y() - View.TILE_SIZE) / View.TILE_SIZE);
        if (y < 0) {
            IO.println("Click out of backpack bounds");
        } else {
            IO.println(state.getBackpack().getItemAt(new Position(x, y)).toString());
        }
    }

    static void handleDungeonClick(ApplicationContext context, GameState state, PointerEvent pe) {
        if (state.isInCombat()) {
            handleCombatClick(context, state, pe);
            return;
        }
        int x = (int) ((pe.location().x() - View.BACKPACK_PIXEL_WIDTH) / View.TILE_SIZE);
        int y = (int) (pe.location().y() / View.TILE_SIZE);
        var clickedPos = new Position(x, y);
        var floor = state.getCurrentFloor();
        if (!isMoveAllowed(state.getPosition(), clickedPos, floor)) return;
        state.setPosition(clickedPos);
        var room = floor.getRoom(clickedPos);
        if (room != null && room.getType() == RoomType.ENEMY) {
            var enemies = room.getEnemies();
            if (enemies != null && !enemies.isEmpty()) {
                state.startCombat(enemies);
                state.getCombatEngine().heroTurn(state.getHero(), state.getBackpack());
            }
        }
        View.draw(context, state);
    }

    static boolean isMoveAllowed(Position current, Position target, Floor floor) { 
        if (floor.getRoom(target) == null) {
            return false;
        }
        int deltaX = Math.abs(current.x() - target.x());
        int deltaY = Math.abs(current.y() - target.y());
        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }

    static void handleCombatClick(ApplicationContext context, GameState state, PointerEvent pe) {
        var info = context.getScreenInfo();
        int screenHeight = (int) info.height(), width =
                View.BACKPACK_PIXEL_WIDTH + state.getCurrentFloor().getWidth() * View.TILE_SIZE;
        int buttonWidth = 120, buttonHeight = 40, spacing = 20;
        int totalWidth = 2 * buttonWidth + spacing;
        int buttonY = (screenHeight - buttonHeight) / 2 + 100;
        int startX = (width - totalWidth) / 2;
        int attackX = startX, defendX = startX + buttonWidth + spacing;
        int mouseX = (int) pe.location().x(), mouseY = (int) pe.location().y();
        boolean attackClicked = mouseY >= buttonY && mouseY <= buttonY + buttonHeight
                && mouseX >= attackX && mouseX <= attackX + buttonWidth;
        boolean defendClicked = mouseY >= buttonY && mouseY <= buttonY + buttonHeight
                && mouseX >= defendX && mouseX <= defendX + buttonWidth;
        if (!attackClicked && !defendClicked) return;
        boolean used = attackClicked ? heroAttackAction(state) : heroDefendAction(state);
        if (!used) {
            IO.println("Cannot use action no equipment or not enough energy.");
            View.draw(context, state);
            return;
        }
        afterHeroAction(context, state);
    }

    static boolean heroAttackAction(GameState state) { 
        CombatEngine combat = state.getCombatEngine();
        Hero hero = state.getHero();
        Backpack backpack = state.getBackpack();
        var enemies = state.getCurrentEnemies();
        Weapon weapon = null;
        for (Item item : backpack.getItems().values()) {
            if (item instanceof Weapon w) { weapon = w; break; }
        }
        if (weapon == null) return false;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                return combat.heroAttack(hero, enemy, weapon);
            }
        }
        return false;
    }

    static boolean heroDefendAction(GameState state) {
        CombatEngine combat = state.getCombatEngine();
        Hero hero = state.getHero();
        Backpack backpack = state.getBackpack();
        Armor armor = null;
        for (Item item : backpack.getItems().values()) {
            if (item instanceof Armor a) { armor = a; break; }
        }
        if (armor == null) return false;
        return combat.heroDefend(hero, armor);
    }

    static void afterHeroAction(ApplicationContext context, GameState state) { 
        if (checkEndOfCombat(context, state)) return;
        View.draw(context, state);
    }

    static boolean checkEndOfCombat(ApplicationContext context, GameState state) {
        CombatEngine combat = state.getCombatEngine();
        Hero hero = state.getHero();
        var enemies = state.getCurrentEnemies();
        if (!combat.isCombatOver(hero, enemies)) return false;
        if (!hero.isAlive()) {
            IO.println("The hero is dead.");
            state.endCombat();
            View.draw(context, state);
            return true;
        }
        var floor = state.getCurrentFloor();
        var pos = state.getPosition();
        floor.setRoom(pos, new Room(RoomType.CORRIDOR, null, null, null, 0));
        state.endCombat();
        IO.println("Combat won.");
        View.draw(context, state);
        return true;
    }

}
