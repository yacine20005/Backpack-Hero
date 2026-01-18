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
    private PopupType activePopup = null;  // null or SELL_CONFIRM, DISCARD_CONFIRM
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
    private boolean pendingUnlockMode = false;  // True if unlock mode should start after loot screen
    
    // Merchant system
    private MerchantMode merchantMode = MerchantMode.BUY;
    
    // Healer system
    private Position healerReturnPos = null;
    private int healerHealAmount = 0;
    private int healerCost = 0;
    
    // Loot system
    private List<Item> availableLoot = null;

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

    public void clearSelectedItem() {
        this.selectedItemAnchor = null;
        this.selectedItem = null;
    }

    public void openHealerPrompt(Position returnPos, int healAmount, int cost) {
        this.state = State.HEALER_PROMPT;
        this.healerReturnPos = Objects.requireNonNull(returnPos);
        this.healerHealAmount = healAmount;
        this.healerCost = cost;
    }

    public void closeHealerPrompt() {
        this.state = State.EXPLORATION;
        this.healerReturnPos = null;
        this.healerHealAmount = 0;
        this.healerCost = 0;
    }

    public Position getHealerReturnPos() {
        return healerReturnPos;
    }

    public int getHealerHealAmount() {
        return healerHealAmount;
    }

    public int getHealerCost() {
        return healerCost;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean value) {
        this.gameOver = value;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean value) {
        this.victory = value;
    }

    public List<Item> getAvailableLoot() {
        return availableLoot;
    }

    public void openLootScreen(List<Item> loot) {
        this.state = State.LOOT_SCREEN;
        this.availableLoot = new ArrayList<>(Objects.requireNonNull(loot));
        this.selectedLootItem = null;
    }

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

    public Item getSelectedLootItem() {
        return selectedLootItem;
    }

    public void setSelectedLootItem(Item item) {
        this.selectedLootItem = Objects.requireNonNull(item, "item cannot be null");
    }

    public void removeLootItem(Item item) {
        Objects.requireNonNull(item, "item cannot be null");
        if (availableLoot != null) {
            availableLoot.remove(item);
        }
    }

    public MerchantMode getMerchantMode() {
        return merchantMode;
    }

    public void setMerchantMode(MerchantMode mode) {
        this.merchantMode = Objects.requireNonNull(mode);
    }

    public PopupType getActivePopup() {
        return activePopup;
    }

    public Item getSellConfirmItem() {
        return activePopup == PopupType.SELL_CONFIRM ? confirmItem : null;
    }

    public Position getSellConfirmAnchor() {
        return activePopup == PopupType.SELL_CONFIRM ? confirmAnchor : null;
    }

    public void openSellConfirm(Item item, Position anchor) {
        this.activePopup = PopupType.SELL_CONFIRM;
        this.confirmItem = Objects.requireNonNull(item);
        this.confirmAnchor = Objects.requireNonNull(anchor);
    }

    public void closeSellConfirm() {
        this.activePopup = null;
        this.confirmItem = null;
        this.confirmAnchor = null;
    }

    public Item getSelectedMerchantItem() {
        return selectedMerchantItem;
    }

    public void setSelectedMerchantItem(Item item) {
        this.selectedMerchantItem = Objects.requireNonNull(item, "item cannot be null");
    }

    public Item getDiscardConfirmItem() {
        return activePopup == PopupType.DISCARD_CONFIRM ? confirmItem : null;
    }

    public Position getDiscardConfirmAnchor() {
        return activePopup == PopupType.DISCARD_CONFIRM ? confirmAnchor : null;
    }

    public void openDiscardConfirm(Item item, Position anchor) {
        this.activePopup = PopupType.DISCARD_CONFIRM;
        this.confirmItem = Objects.requireNonNull(item);
        this.confirmAnchor = Objects.requireNonNull(anchor);
    }

    public void closeDiscardConfirm() {
        this.activePopup = null;
        this.confirmItem = null;
        this.confirmAnchor = null;
    }

    public int getCellsToUnlock() {
        return cellsToUnlock;
    }

    public void startCellUnlockMode(int count) {
        this.cellsToUnlock = count;
        this.pendingUnlockMode = true;
    }

    public void unlockCellAt(Position pos) {
        Objects.requireNonNull(pos, "pos cannot be null");
        if (backpack.unlockCell(pos)) {
            cellsToUnlock--;
            if (cellsToUnlock <= 0) {
                state = State.EXPLORATION;
            }
        }
    }

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
