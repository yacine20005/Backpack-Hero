package fr.uge.backpackhero.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.backpackhero.model.entity.Hero;
import fr.uge.backpackhero.model.item.Backpack;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.level.Dungeon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;

/**
 * GameState manages the overall state of the game, including the dungeon, hero,
 * backpack, current position, and combat status.
 * It provides methods to access and modify these components as the game
 * progresses.
 * 
 * @author Yacine
 */
public class GameState {

    final Dungeon dungeon = new Dungeon();
    int floor = 0;
    Position position = new Position(0, 0);
    final Hero hero = new Hero();
    Backpack backpack = new Backpack(7, 5);
    CombatEngine combatEngine = new CombatEngine();

    // Game state
    private State state = State.EXPLORATION;
    private PopupType activePopup = null; // null or SELL_CONFIRM, DISCARD_CONFIRM
    private boolean gameOver = false;
    private boolean victory = false;

    // Selection system
    private Position selectedItemAnchor = null;
    private Item selectedItem = null;
    private Item selectedLootItem = null;
    private Item selectedMerchantItem = null;

    // Confirm popup context (for SELL_CONFIRM and DISCARD_CONFIRM popups)
    private Position confirmAnchor = null;
    private Item confirmItem = null;

    // Cell unlock context (for CELL_UNLOCK state)
    private int cellsToUnlock = 0;
    private boolean pendingUnlockMode = false; // True if unlock mode should start after loot screen

    // Merchant system
    private MerchantMode merchantMode = MerchantMode.BUY;

    // Healer system
    private Position healerReturnPos = null;
    private int healerHealAmount = 0;
    private int healerCost = 0;

    // Loot system
    private List<Item> availableLoot = null;

    /**
     * Creates a new GameState with default values.
     * Initializes the dungeon, hero, backpack, and combat engine.
     */
    public GameState() {
    }

    /**
     * Returns the dungeon of the game.
     * 
     * @return the dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * Returns the current floor number.
     * 
     * @return the current floor number
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Sets the current floor number.
     * 
     * @param floor the new floor number
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Sets the current position of the hero.
     * 
     * @param position the new position of the hero
     */
    public void setPosition(Position position) {
        this.position = Objects.requireNonNull(position);
    }

    /**
     * Returns the current position of the hero.
     * 
     * @return the current position of the hero
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the hero of the game.
     * 
     * @return the hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Returns the current floor object.
     * 
     * @return the current floor
     */
    public Floor getCurrentFloor() {
        return dungeon.getFloor(floor);
    }

    /**
     * Exits the current floor and proceeds to the next one, or triggers victory if it was the last floor.
     */
    public void exitFloor() {
        if (floor + 1 >= dungeon.getFloorCount()) {
            this.victory = true;
            return;
        }
        this.floor++;
        this.position = new Position(0, 0);
    }

    /**
     * Returns the backpack of the hero.
     * 
     * @return the backpack
     */
    public Backpack getBackpack() {
        return backpack;
    }

    /**
     * Returns the combat engine of the game.
     * 
     * @return the combat engine
     */
    public CombatEngine getCombatEngine() {
        return combatEngine;
    }

    /**
     * Returns the current game state.
     * 
     * @return the current state
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the current game state.
     * 
     * @param state the new state
     */
    public void setState(State state) {
        this.state = Objects.requireNonNull(state);
    }

    /**
     * Returns the anchor position of the currently selected item in the backpack.
     * 
     * @return the anchor position of the selected item, or null if no item is
     *         selected
     */
    public Position getSelectedItemAnchor() {
        return selectedItemAnchor;
    }

    /**
     * Returns the currently selected item in the backpack.
     * 
     * @return the selected item, or null if no item is selected
     */
    public Item getSelectedItem() {
        return selectedItem;
    }

    /**
     * Sets the currently selected item and its anchor position in the backpack.
     * 
     * @param anchor the anchor position of the selected item
     * @param item   the selected item
     */
    public void setSelectedItem(Position anchor, Item item) {
        this.selectedItemAnchor = Objects.requireNonNull(anchor, "anchor cannot be null");
        this.selectedItem = Objects.requireNonNull(item, "item cannot be null");
    }

    /**
     * Clears the currently selected item.
     */
    public void clearSelectedItem() {
        this.selectedItemAnchor = null;
        this.selectedItem = null;
    }

    /**
     * Opens the healer prompt with the specified parameters.
     * 
     * @param returnPos the position to return to if the player declines
     * @param healAmount the amount of HP to heal
     * @param cost the cost in gold
     */
    public void openHealerPrompt(Position returnPos, int healAmount, int cost) {
        this.state = State.HEALER_PROMPT;
        this.healerReturnPos = Objects.requireNonNull(returnPos);
        this.healerHealAmount = healAmount;
        this.healerCost = cost;
    }

    /**
     * Closes the healer prompt and returns to exploration state.
     */
    public void closeHealerPrompt() {
        this.state = State.EXPLORATION;
        this.healerReturnPos = null;
        this.healerHealAmount = 0;
        this.healerCost = 0;
    }

    /**
     * Returns the position where the player was before entering the healer prompt.
     * 
     * @return the return position
     */
    public Position getHealerReturnPos() {
        return healerReturnPos;
    }

    /**
     * Returns the amount of HP the healer offers to heal.
     * 
     * @return the heal amount
     */
    public int getHealerHealAmount() {
        return healerHealAmount;
    }

    /**
     * Returns the cost of the healer's service.
     * 
     * @return the cost in gold
     */
    public int getHealerCost() {
        return healerCost;
    }

    /**
     * Checks if the game is over (loss).
     * 
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game over status.
     * 
     * @param value true if the game is over
     */
    public void setGameOver(boolean value) {
        this.gameOver = value;
    }

    /**
     * Checks if the player has won the game.
     * 
     * @return true if the player has won, false otherwise
     */
    public boolean isVictory() {
        return victory;
    }

    /**
     * Sets the victory status.
     * 
     * @param value true if the player has won
     */
    public void setVictory(boolean value) {
        this.victory = value;
    }

    /**
     * Returns the list of available loot items.
     * 
     * @return the list of loot items
     */
    public List<Item> getAvailableLoot() {
        return availableLoot;
    }

    /**
     * Opens the loot screen with the given items.
     * 
     * @param loot the list of loot items
     */
    public void openLootScreen(List<Item> loot) {
        this.state = State.LOOT_SCREEN;
        this.availableLoot = new ArrayList<>(Objects.requireNonNull(loot));
        this.selectedLootItem = null;
    }

    /**
     * Closes the loot screen and transitions to the next state (Cell Unlock or Exploration).
     */
    public void closeLootScreen() {
        this.availableLoot = null;
        this.selectedLootItem = null;

        // If there's a pending unlock mode, start it now
        if (pendingUnlockMode) {
            this.state = State.CELL_UNLOCK;
            pendingUnlockMode = false;
        } else {
            this.state = State.EXPLORATION;
        }
    }

    /**
     * Returns the currently selected loot item.
     * 
     * @return the selected loot item
     */
    public Item getSelectedLootItem() {
        return selectedLootItem;
    }

    /**
     * Sets the currently selected loot item.
     * 
     * @param item the item to select
     */
    public void setSelectedLootItem(Item item) {
        this.selectedLootItem = item; // null is allowed to deselect
    }

    /**
     * Removes an item from the available loot list.
     * 
     * @param item the item to remove
     */
    public void removeLootItem(Item item) {
        Objects.requireNonNull(item, "item cannot be null");
        if (availableLoot != null) {
            availableLoot.remove(item);
        }
    }

    /**
     * Returns the current merchant interaction mode.
     * 
     * @return the merchant mode
     */
    public MerchantMode getMerchantMode() {
        return merchantMode;
    }

    /**
     * Sets the merchant interaction mode.
     * 
     * @param mode the new merchant mode
     */
    public void setMerchantMode(MerchantMode mode) {
        this.merchantMode = Objects.requireNonNull(mode);
    }

    /**
     * Returns the currently active popup type.
     * 
     * @return the active popup type, or null if none
     */
    public PopupType getActivePopup() {
        return activePopup;
    }

    /**
     * Returns the item pending confirmation for sale.
     * 
     * @return the item to confirm sell, or null
     */
    public Item getSellConfirmItem() {
        return activePopup == PopupType.SELL_CONFIRM ? confirmItem : null;
    }

    /**
     * Returns the anchor position of the item pending confirmation for sale.
     * 
     * @return the anchor position, or null
     */
    public Position getSellConfirmAnchor() {
        return activePopup == PopupType.SELL_CONFIRM ? confirmAnchor : null;
    }

    /**
     * Opens the sell confirmation popup for an item.
     * 
     * @param item the item to sell
     * @param anchor the position of the item in the backpack
     */
    public void openSellConfirm(Item item, Position anchor) {
        this.activePopup = PopupType.SELL_CONFIRM;
        this.confirmItem = Objects.requireNonNull(item);
        this.confirmAnchor = Objects.requireNonNull(anchor);
    }

    /**
     * Closes the sell confirmation popup.
     */
    public void closeSellConfirm() {
        this.activePopup = null;
        this.confirmItem = null;
        this.confirmAnchor = null;
    }

    /**
     * Returns the currently selected item in the merchant shop.
     * 
     * @return the selected merchant item
     */
    public Item getSelectedMerchantItem() {
        return selectedMerchantItem;
    }

    /**
     * Sets the currently selected item in the merchant shop.
     * 
     * @param item the item to select
     */
    public void setSelectedMerchantItem(Item item) {
        this.selectedMerchantItem = item; // null is allowed to deselect
    }

    /**
     * Returns the item pending confirmation for discard.
     * 
     * @return the item to confirm discard, or null
     */
    public Item getDiscardConfirmItem() {
        return activePopup == PopupType.DISCARD_CONFIRM ? confirmItem : null;
    }

    /**
     * Returns the anchor position of the item pending confirmation for discard.
     * 
     * @return the anchor position, or null
     */
    public Position getDiscardConfirmAnchor() {
        return activePopup == PopupType.DISCARD_CONFIRM ? confirmAnchor : null;
    }

    /**
     * Opens the discard confirmation popup for an item.
     * 
     * @param item the item to discard
     * @param anchor the position of the item in the backpack
     */
    public void openDiscardConfirm(Item item, Position anchor) {
        this.activePopup = PopupType.DISCARD_CONFIRM;
        this.confirmItem = Objects.requireNonNull(item);
        this.confirmAnchor = Objects.requireNonNull(anchor);
    }

    /**
     * Closes the discard confirmation popup.
     */
    public void closeDiscardConfirm() {
        this.activePopup = null;
        this.confirmItem = null;
        this.confirmAnchor = null;
    }

    /**
     * Returns the number of cells remaining to be unlocked.
     * 
     * @return the number of cells to unlock
     */
    public int getCellsToUnlock() {
        return cellsToUnlock;
    }

    /**
     * Starts the cell unlock mode with the specified number of cells to unlock.
     * 
     * @param count the number of cells to unlock
     */
    public void startCellUnlockMode(int count) {
        this.cellsToUnlock = count;
        this.pendingUnlockMode = true;
    }

    /**
     * Unlocks a cell at the specified position if valid, and decrements the remaining count.
     * 
     * @param pos the position of the cell to unlock
     */
    public void unlockCellAt(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        if (backpack.unlockCell(pos)) {
            cellsToUnlock--;
            if (cellsToUnlock <= 0) {
                state = State.EXPLORATION;
            }
        }
    }

    /**
     * Ends the cell unlock mode immediately.
     */
    public void endCellUnlockMode() {
        this.state = State.EXPLORATION;
        this.cellsToUnlock = 0;
    }

    /**
     * Calculates the player's score based on the formula:
     * HP max + sum of equipment prices.
     * 
     * @return the calculated score
     */
    public int calculateScore() {
        int score = hero.getHp();

        // Sum all item prices in the backpack
        for (Item item : backpack.getItems().values()) {
            score += item.getPrice();
        }

        return score;
    }

}
