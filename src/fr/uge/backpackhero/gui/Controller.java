package fr.uge.backpackhero.gui;

import java.util.ArrayList;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.logic.CombatEngine;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.entity.Hero;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.Weapon;
import fr.uge.backpackhero.model.item.Gold;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;
import fr.uge.backpackhero.model.loot.LootTables;

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

        // If we are in loot screen mode with a selected loot item
        if (state.isLootScreenOpen() && state.getSelectedLootItem() != null) {
            var lootItem = state.getSelectedLootItem();
            if (state.getBackpack().place(lootItem, pos)) {
                state.removeLootItem(lootItem);
                state.setSelectedLootItem(null);
                IO.println("Placed loot item: " + lootItem.getName());
            } else {
                IO.println("Cannot place item here.");
            }
            View.draw(context, state);
            return;
        }

        // If in loot screen mode without selected item, allow item movement in backpack
        if (state.isLootScreenOpen() && state.getSelectedLootItem() == null) {
            handleItemMovement(context, state, pos);
            return;
        }

        // If in merchant BUY mode with selected item
        if (state.getMerchantMode().equals("BUY") && state.getSelectedMerchantItem() != null && !state.isInCombat()) {
            var room = state.getCurrentFloor().getRoom(state.getPosition());
            if (room != null && room.getType() == RoomType.MERCHANT) {
                var shop = room.getMerchantItems();
                if (shop != null) {
                    var selectedItem = state.getSelectedMerchantItem();
                    Integer price = shop.get(selectedItem);
                    if (price != null) {
                        if (!state.getBackpack().spendGold(price)) {
                            IO.println("Not enough gold.");
                        } else if (state.getBackpack().place(selectedItem, pos)) {
                            shop.remove(selectedItem);
                            state.setSelectedMerchantItem(null);
                            IO.println("Bought: " + selectedItem.getName() + " for " + price + "g");
                        } else {
                            state.getBackpack().addGold(price);
                            IO.println("Cannot place item here.");
                        }
                    }
                }
                View.draw(context, state);
                return;
            }
        }

        // If in merchant SELL mode, clicking on item opens sell confirmation
        if (state.getMerchantMode().equals("SELL") && !state.isInCombat()) {
            var room = state.getCurrentFloor().getRoom(state.getPosition());
            if (room != null && room.getType() == RoomType.MERCHANT) {
                handleBackpackSellClick(context, state, pos);
                return;
            }
        }

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

    private static void handleBackpackSellClick(ApplicationContext context, GameState state, Position pos) {
        var backpack = state.getBackpack();
        var anchor = backpack.getAnchorAt(pos);
        if (anchor.isEmpty()) {
            return;
        }

        var item = backpack.getItems().get(anchor.get());
        if (item == null) {
            return;
        }

        // Prevent selling Gold
        switch (item) {
            case Gold gold -> {
                System.out.println("Cannot sell gold!");
                View.draw(context, state);
                return;
            }
            default -> {
            }
        }

        state.openSellConfirm(item, anchor.get());
        View.draw(context, state);
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
            if (currentAnchor.isPresent() && currentAnchor.get().equals(selectedAnchor)) {
                // Clicked on the same item, deselect
                state.clearSelectedItem();
                View.draw(context, state);
            } else {
                // Try to move the item to the new position
                if (backpack.move(selectedAnchor, pos)) {
                    state.clearSelectedItem();
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

        return switch (item) {
            case Weapon weapon -> {
                Enemy target = null;
                for (Enemy enemy : enemies) {
                    if (enemy.isAlive()) {
                        target = enemy;
                        break;
                    }
                }
                if (target == null)
                    yield false;
                yield combat.heroAttack(hero, target, weapon);
            }
            case Armor armor -> combat.heroDefend(hero, armor);
            default -> false;
        };
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

        if (room != null && room.getType() == RoomType.TREASURE) {
            var treasureItems = room.getTreasureItems();
            if (treasureItems != null && !treasureItems.isEmpty()) {
                int goldAmount = room.collectGold();
                if (goldAmount > 0) {
                    state.getBackpack().addGold(goldAmount);
                    IO.println("Found " + goldAmount + " gold!");
                }
                state.openLootScreen(treasureItems);
                IO.println("Treasure room! " + treasureItems.size() + " items to choose from.");
                View.draw(context, state);
            }
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

        var lootItems = LootTables.generateLootFromEnemies(combat.getCurrentEnemies(), state.getFloor());
        int reward = combat.calculateGoldReward();
        state.getBackpack().addGold(reward);

        state.openLootScreen(lootItems);
        IO.println("Combat won! Gained " + reward + " gold and " + lootItems.size() + " items to choose from.");
        View.draw(context, state);
        return true;
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
        int x = View.POPUP_X;
        int y = View.POPUP_Y;
        int w = View.POPUP_WIDTH;
        int h = 280;
        if (mx < x || mx > x + w || my < y || my > y + h) {
            return false;
        }

        return true;
    }

    public static void handleSellConfirmYes(GameState state) {
        if (!state.isSellConfirmOpen())
            return;

        var item = state.getSellConfirmItem();
        var anchor = state.getSellConfirmAnchor();
        if (item != null && anchor != null) {
            int sellPrice = item.getPrice() / 2;
            state.getBackpack().removeItem(anchor);
            state.getBackpack().addGold(sellPrice);
            System.out.println("Sold: " + item.getName() + " for " + sellPrice + "g");
        }
        state.closeSellConfirm();
    }

    public static void handleSellConfirmNo(GameState state) {
        if (!state.isSellConfirmOpen())
            return;
        state.closeSellConfirm();
    }

    public static void handleHealerAccept(ApplicationContext context, GameState state) {
        if (!state.isHealerPromptOpen())
            return;

        int cost = state.getHealerCost();
        int heal = state.getHealerHealAmount();

        if (!state.getBackpack().spendGold(cost)) {
            System.out.println("Not enough gold.");
            View.draw(context, state);
            return;
        }

        var hero = state.getHero();
        int before = hero.getHp();
        hero.setHp(before + heal);

        state.closeHealerPrompt();
        View.draw(context, state);
    }

    public static void handleHealerDecline(ApplicationContext context, GameState state) {
        if (!state.isHealerPromptOpen())
            return;
        state.closeHealerPrompt();
        View.draw(context, state);
    }

    public static void handleLootContinue(ApplicationContext context, GameState state) {
        if (!state.isLootScreenOpen())
            return;

        state.closeLootScreen();
        
        // Only end combat if we were in combat
        if (state.isInCombat()) {
            var combat = state.getCombatEngine();
            combat.endCombat();
        }

        var floor = state.getCurrentFloor();
        var pos = state.getPosition();
        floor.setRoom(pos, new Room(RoomType.CORRIDOR, null, null, null, 0, 0));

        IO.println("Loot screen closed.");
        View.draw(context, state);
    }

    public static boolean handleLootScreenClick(ApplicationContext context, GameState state, PointerEvent pe) {
        if (state.isGameOver()) {
            return true;
        }

        if (!state.isLootScreenOpen())
            return false;
        if (pe.action() != PointerEvent.Action.POINTER_DOWN)
            return true;

        return true;
    }

    public static void handleMerchantItemSelection(ApplicationContext context, GameState state, int index) {
        var room = state.getCurrentFloor().getRoom(state.getPosition());
        if (room == null || room.getType() != RoomType.MERCHANT) {
            return;
        }

        var items = room.getMerchantItems();
        if (items == null || items.isEmpty()) {
            return;
        }

        var list = new ArrayList<>(items.entrySet());

        if (index < list.size()) {
            Item selectedItem = list.get(index).getKey();
            // Toggle selection: deselect if already selected
            if (selectedItem.equals(state.getSelectedMerchantItem())) {
                state.setSelectedMerchantItem(null);
                System.out.println("Deselected merchant item: " + selectedItem.getName());
            } else {
                state.setSelectedMerchantItem(selectedItem);
                System.out.println("Selected merchant item: " + selectedItem.getName());
            }
            View.draw(context, state);
        }
    }

    public static void handleLootItemSelection(ApplicationContext context, GameState state, int index) {
        var loot = state.getAvailableLoot();
        if (loot == null || loot.isEmpty()) {
            return;
        }

        if (index < loot.size()) {
            Item selectedItem = loot.get(index);
            // Toggle selection: deselect if already selected
            if (selectedItem.equals(state.getSelectedLootItem())) {
                state.setSelectedLootItem(null);
                System.out.println("Deselected loot item: " + selectedItem.getName());
            } else {
                state.setSelectedLootItem(selectedItem);
                System.out.println("Selected loot item: " + selectedItem.getName());
            }
            View.draw(context, state);
        }
    }

}
