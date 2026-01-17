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
import fr.uge.backpackhero.model.item.ManaStone;

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
     * Left click: select/deselect or move items, or use in combat
     * 
     * @param context      the application context
     * @param state        the current game state
     * @param pointerEvent the pointer event representing the click
     */
    public static void handleBackpackClick(ApplicationContext context, GameState state, PointerEvent pointerEvent) {
    	if (state.isGameOver()) {
    	    return;
    	}

        int x = (int) (pointerEvent.location().x() / View.TILE_SIZE);
        int y = (int) ((pointerEvent.location().y() - View.TILE_SIZE) / View.TILE_SIZE);
        if (y < 0)
            return;
        var pos = new Position(x, y);

        // If we're not in combat, handle item movement
        if (!state.isInCombat()) {
            handleItemMovement(context, state, pos);
            return;
        }

        // In combat mode, use items
        var optItem = state.getBackpack().getItemAt(pos);
        if (optItem.isEmpty()) {
            return;
        }

        var item = optItem.get();
        if (!useItemInCombat(state, item)) {
            System.out.println("Cannot use item in combat.");
            View.draw(context, state);
            return;
        }

        afterHeroAction(context, state);
    }

    /**
     * Handles item movement in the backpack (selection and placement).
     * 
     * @param context the application context
     * @param state   the current game state
     * @param pos     the clicked position in the backpack
     */
    private static void handleItemMovement(ApplicationContext context, GameState state, Position pos) {
        var backpack = state.getBackpack();
        var currentAnchor = backpack.getAnchorAt(pos);

        // If no item is currently selected
        if (state.getSelectedItemAnchor() == null) {
            if (currentAnchor.isPresent()) {
                var anchor = currentAnchor.get(); // Because currentAnchor is an Optional and we checked with isPresent
                var item = backpack.getItems().get(anchor);
                state.setSelectedItem(anchor, item);
                View.draw(context, state);
            }
        }
        // An item is selected, try to move it
        else {
            var selectedAnchor = state.getSelectedItemAnchor();
            if (selectedAnchor.equals(pos)) {
                // Clicked on the same item, deselect
                state.clearSelectedItem();
                View.draw(context, state);
            } else {
                // Try to move the item to the new position
                if (backpack.move(selectedAnchor, pos)) {
                    // Update the anchor position in the state
                    state.setSelectedItem(pos, state.getSelectedItem());
                } else {
                    System.out.println("Cannot move item to this position.");
                }
                View.draw(context, state);
            }
        }
    }

    /**
     * Rotates the currently selected item in the backpack.
     * 
     * @param context the application context
     * @param state   the current game state
     */
    public static void handleRotateItem(ApplicationContext context, GameState state) {
        var selectedItemAnchor = state.getSelectedItemAnchor();
        if (selectedItemAnchor == null) {
            System.out.println("No item selected.");
            return;
        }

        var backpack = state.getBackpack();
        if (!backpack.rotateItem(selectedItemAnchor)) {
            System.out.println("Cannot rotate item in current position.");
        }
        View.draw(context, state);
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
        if (state.isGameOver()) {
            return;
        }


        int x = (int) ((pointerEvent.location().x() - View.BACKPACK_PIXEL_WIDTH) / View.TILE_SIZE);
        int y = (int) (pointerEvent.location().y() / View.TILE_SIZE);
        var clickedPos = new Position(x, y);
        var floor = state.getCurrentFloor();
        if (!isMoveAllowed(state.getPosition(), clickedPos, floor))
            return;
        var prevPos = state.getPosition();
        state.setPosition(clickedPos);
        state.setPosition(clickedPos);
        var room = floor.getRoom(clickedPos);
        
        if (room != null && room.getType() == RoomType.EXIT) {
            state.exitFloor();
            View.draw(context, state);
            return;
        }
        
        if (room != null && room.getType() == RoomType.HEALER) {
            int heal = room.getHealAmount();
            int cost = Math.max(1, heal / 2); 
            state.openHealerPrompt(prevPos, heal, cost);
            View.draw(context, state);
            return;
        }
        
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
            System.out.println("The hero is dead.");
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

        if (!combat.isCombatOver(hero)) {
            return false;
        }

        if (!hero.isAlive()) {
            System.out.println("The hero is dead.");
            state.setGameOver(true);
            combat.endCombat();
            View.draw(context, state);
            return true;
        }

        var floor = state.getCurrentFloor();
        var pos = state.getPosition();

        floor.setRoom(pos, new Room(RoomType.CORRIDOR, null, null, null, 0, 0));

        int reward = combat.calculateGoldReward();
        state.getBackpack().addGold(reward);

        combat.endCombat();
        System.out.println("Combat won. Gained " + reward + " gold.");
        View.draw(context, state);
        return true;
    }

    public static Item rollLootItem(int floorIndex, java.util.Random rng) {
        int r = rng.nextInt(100);

        if (floorIndex == 0) {
            if (r < 40) return Weapon.woodSword();
            if (r < 60) return Armor.woodenShield();
            if (r < 80) return Weapon.woodenBow();
            return ManaStone.smallManaStone();
        }
        if (floorIndex == 1) {
            if (r < 30) return Weapon.sturn();
            if (r < 55) return Armor.emeraldShield();
            if (r < 75) return Armor.luckypants();
            return ManaStone.bigManaStone();
        }
        if (r < 30) return Weapon.telesto();
        if (r < 55) return Weapon.montaintop();
        if (r < 75) return Weapon.lastWord();
        if (r < 90) return Armor.celestialnighthawk();
        return Armor.liarshandshake();
    }
    public static boolean handleMerchantClick(GameState state, PointerEvent event) {
    	if (state.isGameOver()) {
    	    return true;
    	}

        var room = state.getCurrentFloor().getRoom(state.getPosition());
        if (room == null || room.getType() != RoomType.MERCHANT || state.isInCombat()) {
            return false;
        }
        int mx = (int) event.location().x();
        int my = (int) event.location().y();
        int x = 520;
        int y = 40;
        int w = 250;
        int h = 220;
        if (mx < x || mx > x + w || my < y || my > y + h) {
            return false;
        }
        var shop = room.getMerchantItems();
        if (shop == null || shop.isEmpty()) return true;
        var list = new java.util.ArrayList<>(shop.entrySet());
        list.sort(java.util.Comparator.comparing(e -> e.getKey().getName()));
        int lineStartY = y + 95;
        int lineH = 22;
        int index = (my - lineStartY) / lineH;
        if (index < 0 || index >= list.size() || index >= 6) {
            return true;
        }
        var entry = list.get(index);
        Item item = entry.getKey();
        int price = entry.getValue();
        if (!state.getBackpack().spendGold(price)) {
            System.out.println("Not enough gold.");
            return true;
        }
        boolean placed = state.getBackpack().placeFirstFit(item);
        if (!placed) {
            state.getBackpack().addGold(price);
            System.out.println("Backpack full. Purchase cancelled.");
            return true;
        }
        shop.remove(item);
        System.out.println("Bought: " + item.getName() + " for " + price + "g");
        return true;
    }
    
    public static boolean handleHealerPromptClick(ApplicationContext context, GameState state, PointerEvent pe) {
    	if (state.isGameOver()) {
    	    return true;
    	}

        if (!state.isHealerPromptOpen()) return false;
        if (pe.action() != PointerEvent.Action.POINTER_DOWN) return true;

        int mx = (int) pe.location().x();
        int my = (int) pe.location().y();

        int boxX = View.BACKPACK_PIXEL_WIDTH + 60;
        int boxY = 80;
        int boxW = 320;
        int boxH = 180;

        int acceptX = boxX + 30, acceptY = boxY + 120, btnW = 110, btnH = 35;
        int leaveX  = boxX + 180, leaveY  = boxY + 120;

        if (mx >= leaveX && mx <= leaveX + btnW && my >= leaveY && my <= leaveY + btnH) {
            state.closeHealerPrompt();
            View.draw(context, state);
            return true;
        }

       
        if (mx >= acceptX && mx <= acceptX + btnW && my >= acceptY && my <= acceptY + btnH) {
            int cost = state.getHealerCost();
            int heal = state.getHealerHealAmount();

            if (!state.getBackpack().spendGold(cost)) {
                System.out.println("Not enough gold.");
                View.draw(context, state);
                return true;
            }

            var hero = state.getHero();
            int before = hero.getHp();
            hero.setHp(before + heal);

            state.closeHealerPrompt();
            View.draw(context, state);
            return true;
        }

        return true; 
    }




}
