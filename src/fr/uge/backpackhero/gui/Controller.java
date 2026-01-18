package fr.uge.backpackhero.gui;

import java.util.ArrayList;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.logic.CombatEngine;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.MerchantMode;
import fr.uge.backpackhero.logic.PopupType;
import fr.uge.backpackhero.logic.State;
import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.entity.Hero;
import fr.uge.backpackhero.model.item.Item;
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

    private final GameState state;
    private final View view;

    /**
     * Creates a new Controller instance.
     * 
     * @param state the game state to control
     * @param view  the view to update after actions
     */
    public Controller(GameState state, View view) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
        this.view = Objects.requireNonNull(view, "view cannot be null");
    }

    /**
     * Handles a click event in the backpack area.
     * Left click: select/deselect or move items, or use in combat
     * 
     * @param context      the application context
     * @param pointerEvent the pointer event representing the click
     */
    public void handleBackpackClick(ApplicationContext context, PointerEvent pointerEvent) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(pointerEvent, "pointerEvent cannot be null");

        if (state.isGameOver()) {
            return;
        }

        int x = (int) (pointerEvent.location().x() / View.TILE_SIZE);
        int y = (int) (pointerEvent.location().y() / View.TILE_SIZE);
        if (y < 0)
            return;
        var pos = new Position(x, y);

        // If in cell unlock mode, try to unlock the clicked cell
        if (state.getState() == State.CELL_UNLOCK) {
            if (state.getBackpack().canUnlockCell(pos)) {
                state.unlockCellAt(pos);
                IO.println("Cell unlocked! " + state.getCellsToUnlock() + " remaining.");
                if (state.getState() != State.CELL_UNLOCK) {
                    IO.println("All cells unlocked!");
                }
            } else {
                IO.println("Cannot unlock this cell. Choose an adjacent cell.");
            }
            view.draw(context);
            return;
        }

        // If we are in loot screen mode with a selected loot item
        if (state.getState() == State.LOOT_SCREEN && state.getSelectedLootItem() != null) {
            var lootItem = state.getSelectedLootItem();
            if (state.getBackpack().place(lootItem, pos)) {
                state.removeLootItem(lootItem);
                state.setSelectedLootItem(null);
                IO.println("Placed loot item: " + lootItem.getName());
            } else {
                IO.println("Cannot place item here.");
            }
            view.draw(context);
            return;
        }

        // If in loot screen mode without selected item, allow item movement in backpack
        if (state.getState() == State.LOOT_SCREEN && state.getSelectedLootItem() == null) {
            handleItemMovement(context, pos);
            return;
        }

        // If in merchant BUY mode with selected item
        if (state.getMerchantMode() == MerchantMode.BUY && state.getSelectedMerchantItem() != null
                && state.getState() != State.COMBAT) {
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
                view.draw(context);
                return;
            }
        }

        // If in merchant SELL mode, clicking on item opens sell confirmation
        if (state.getMerchantMode() == MerchantMode.SELL && state.getState() != State.COMBAT) {
            var room = state.getCurrentFloor().getRoom(state.getPosition());
            if (room != null && room.getType() == RoomType.MERCHANT) {
                handleBackpackSellClick(context, pos);
                return;
            }
        }

        // If we're not in combat, handle item movement
        if (state.getState() != State.COMBAT) {
            handleItemMovement(context, pos);
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
            view.draw(context);
            return;
        }

        afterHeroAction(context);
    }

    private void handleBackpackSellClick(ApplicationContext context, Position pos) {
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
            case Gold _ -> {
                System.out.println("Cannot sell gold!");
                view.draw(context);
                return;
            }
            default -> {
            }
        }

        state.openSellConfirm(item, anchor.get());
        view.draw(context);
    }

    /**
     * Handles item movement in the backpack (selection and placement).
     * 
     * @param context the application context
     * @param state   the current game state
     * @param pos     the clicked position in the backpack
     */
    private void handleItemMovement(ApplicationContext context, Position pos) {
        var backpack = state.getBackpack();
        var currentAnchor = backpack.getAnchorAt(pos);

        // If no item is currently selected
        if (state.getSelectedItemAnchor() == null) {
            if (currentAnchor.isPresent()) {
                var anchor = currentAnchor.get(); // Because currentAnchor is an Optional and we checked with isPresent
                var item = backpack.getItems().get(anchor);
                state.setSelectedItem(anchor, item);
                view.draw(context);
            }
        }
        // An item is selected, try to move it
        else {
            var selectedAnchor = state.getSelectedItemAnchor();
            if (currentAnchor.isPresent() && currentAnchor.get().equals(selectedAnchor)) {
                // Clicked on the same item, deselect
                state.clearSelectedItem();
                view.draw(context);
            } else {
                // Try to move the item to the new position
                if (backpack.move(selectedAnchor, pos)) {
                    state.clearSelectedItem();
                } else {
                    System.out.println("Cannot move item to this position.");
                }
                view.draw(context);
            }
        }
    }

    /**
     * Rotates the currently selected item in the backpack.
     * 
     * @param context the application context
     * @param state   the current game state
     */
    public void handleRotateItem(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        var selectedItemAnchor = state.getSelectedItemAnchor();
        if (selectedItemAnchor == null) {
            System.out.println("No item selected.");
            return;
        }

        var backpack = state.getBackpack();
        if (!backpack.rotateItem(selectedItemAnchor)) {
            System.out.println("Cannot rotate item in current position.");
        }
        view.draw(context);
    }

    /**
     * Uses the given item during combat if possible.
     * Delegates to CombatEngine for the actual combat logic.
     *
     * @param state the current game state
     * @param item  the item clicked in the backpack
     * @return true if the combat action was successfully used, false otherwise
     */
    static boolean useItemInCombat(GameState state, Item item) {
        return state.getCombatEngine().useItem(state.getHero(), item);
    }

    /**
     * Handles a click event in the dungeon area.
     * 
     * @param context      the application context
     * @param state        the current game state
     * @param pointerEvent the pointer event representing the click
     */
    public void handleDungeonClick(ApplicationContext context, PointerEvent pointerEvent) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(pointerEvent, "pointerEvent cannot be null");
        if (state.getState() == State.COMBAT) {
            return;
        }
        if (state.isGameOver()) {
            return;
        }

        // Block movement if any popup is open
        if (state.getState() == State.HEALER_PROMPT || state.getActivePopup() == PopupType.SELL_CONFIRM
                || state.getActivePopup() == PopupType.DISCARD_CONFIRM) {
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
            view.draw(context);
            return;
        }

        if (room != null && room.getType() == RoomType.HEALER) {
            int heal = room.getHealAmount();
            int cost = Math.max(1, heal / 2);
            state.openHealerPrompt(prevPos, heal, cost);
            view.draw(context);
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
                view.draw(context);
            }
            return;
        }

        if (room != null && room.getType() == RoomType.ENEMY) {
            var enemies = room.getEnemies();
            if (enemies != null && !enemies.isEmpty()) {
                var combat = state.getCombatEngine();
                combat.startCombat(enemies);
                combat.heroTurn(state.getHero(), state.getBackpack());
                state.setState(State.COMBAT);
            }
        }
        view.draw(context);
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
    private void afterHeroAction(ApplicationContext context) {
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

    /**
     * Checks if the combat has ended and handles the end of combat logic.
     * 
     * @param context the application context
     * @param state   the current game state
     * @return true if the combat has ended, false otherwise
     */
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

    public boolean handleMerchantClick(PointerEvent event) {
        Objects.requireNonNull(event, "event cannot be null");
        if (state.isGameOver()) {
            return true;
        }

        var room = state.getCurrentFloor().getRoom(state.getPosition());
        if (room == null || room.getType() != RoomType.MERCHANT || state.getState() == State.COMBAT) {
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

    public void handleSellConfirmYes() {
        if (state.getActivePopup() != PopupType.SELL_CONFIRM)
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

    public void handleSellConfirmNo() {
        if (state.getActivePopup() != PopupType.SELL_CONFIRM)
            return;
        state.closeSellConfirm();
    }

    public void handleDiscardItem(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        // Only block if another popup is already open or game is over
        if (state.getActivePopup() == PopupType.SELL_CONFIRM || state.getState() == State.HEALER_PROMPT
                || state.isGameOver() || state.isVictory()) {
            return;
        }

        var selectedAnchor = state.getSelectedItemAnchor();
        if (selectedAnchor == null) {
            System.out.println("No item selected to discard.");
            return;
        }

        var item = state.getBackpack().getItems().get(selectedAnchor);
        if (item == null) {
            return;
        }
        // Prevent discarding Gold
        switch (item) {
            case Gold _ -> {
                System.out.println("Cannot discard gold!");
                view.draw(context);
                return;
            }
            default -> {
            }
        }

        state.openDiscardConfirm(item, selectedAnchor);
        view.draw(context);
    }

    public void handleDiscardConfirmYes(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getActivePopup() != PopupType.DISCARD_CONFIRM)
            return;

        var item = state.getDiscardConfirmItem();
        var anchor = state.getDiscardConfirmAnchor();
        if (item != null && anchor != null) {
            state.getBackpack().removeItem(anchor);
            state.clearSelectedItem();
            System.out.println("Discarded: " + item.getName());
        }
        state.closeDiscardConfirm();
        view.draw(context);
    }

    public void handleDiscardConfirmNo(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getActivePopup() != PopupType.DISCARD_CONFIRM)
            return;
        state.closeDiscardConfirm();
        view.draw(context);
    }

    public void handleHealerAccept(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getState() != State.HEALER_PROMPT)
            return;

        int cost = state.getHealerCost();
        int heal = state.getHealerHealAmount();

        if (!state.getBackpack().spendGold(cost)) {
            System.out.println("Not enough gold.");
            view.draw(context);
            return;
        }

        var hero = state.getHero();
        int before = hero.getHp();
        hero.setHp(before + heal);

        state.closeHealerPrompt();
        view.draw(context);
    }

    public void handleHealerDecline(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getState() != State.HEALER_PROMPT)
            return;
        state.closeHealerPrompt();
        view.draw(context);
    }

    public void handleLootContinue(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getState() != State.LOOT_SCREEN)
            return;

        state.closeLootScreen();

        // Only end combat if we were in combat
        if (state.getState() == State.LOOT_SCREEN) {
            var combat = state.getCombatEngine();
            combat.endCombat();
        }

        var floor = state.getCurrentFloor();
        var pos = state.getPosition();
        floor.setRoom(pos, new Room(RoomType.CORRIDOR, null, null, null, 0, 0));

        IO.println("Loot screen closed.");
        view.draw(context);
    }

    public boolean handleLootScreenClick(ApplicationContext context, PointerEvent pe) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(pe, "pe cannot be null");
        if (state.isGameOver()) {
            return true;
        }

        if (state.getState() != State.LOOT_SCREEN)
            return false;
        if (pe.action() != PointerEvent.Action.POINTER_DOWN)
            return true;

        return true;
    }

    public void handleMerchantItemSelection(ApplicationContext context, int index) {
        Objects.requireNonNull(context, "context cannot be null");
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
            view.draw(context);
        }
    }

    public void handleLootItemSelection(ApplicationContext context, int index) {
        Objects.requireNonNull(context, "context cannot be null");
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
            view.draw(context);
        }
    }

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

}
