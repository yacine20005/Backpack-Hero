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
    Backpack backpack = new Backpack(5, 3);
    CombatEngine combatEngine = new CombatEngine();
    private Position selectedItemAnchor = null;
    private Item selectedItem = null;
    private boolean healerPromptOpen = false;
    private boolean lootScreenOpen = false;
    private Position healerReturnPos = null;
    private int healerHealAmount = 0;
    private int healerCost = 0;
    private List<Item> availableLoot = null;
    private Item selectedLootItem = null;
    private String merchantMode = "BUY"; // "BUY" or "SELL"
    private Item sellConfirmItem = null;
    private Position sellConfirmAnchor = null;
    private Item selectedMerchantItem = null;
    private Item discardConfirmItem = null;
    private Position discardConfirmAnchor = null;

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
     * Checks if the game is currently in combat.
     * 
     * @return true if the game is in combat, false otherwise
     */
    public boolean isInCombat() {
        return combatEngine.isInCombat();
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
        this.selectedItemAnchor = anchor;
        this.selectedItem = item;
    }

    public void clearSelectedItem() {
        this.selectedItemAnchor = null;
        this.selectedItem = null;
    }

    public boolean isHealerPromptOpen() {
        return healerPromptOpen;
    }

    public void openHealerPrompt(Position returnPos, int healAmount, int cost) {
        this.healerPromptOpen = true;
        this.healerReturnPos = Objects.requireNonNull(returnPos);
        this.healerHealAmount = healAmount;
        this.healerCost = cost;
    }

    public void closeHealerPrompt() {
        this.healerPromptOpen = false;
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

    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean value) {
        this.gameOver = value;
    }

    public boolean isLootScreenOpen() {
        return lootScreenOpen;
    }

    public List<Item> getAvailableLoot() {
        return availableLoot;
    }

    public void openLootScreen(List<Item> loot) {
        this.lootScreenOpen = true;
        this.availableLoot = new ArrayList<>(Objects.requireNonNull(loot));
        this.selectedLootItem = null;
    }

    public void closeLootScreen() {
        this.lootScreenOpen = false;
        this.availableLoot = null;
        this.selectedLootItem = null;
    }

    public Item getSelectedLootItem() {
        return selectedLootItem;
    }

    public void setSelectedLootItem(Item item) {
        this.selectedLootItem = item;
    }

    public void removeLootItem(Item item) {
        if (availableLoot != null) {
            availableLoot.remove(item);
        }
    }

    public String getMerchantMode() {
        return merchantMode;
    }

    public void setMerchantMode(String mode) {
        this.merchantMode = Objects.requireNonNull(mode);
    }

    public Item getSellConfirmItem() {
        return sellConfirmItem;
    }

    public Position getSellConfirmAnchor() {
        return sellConfirmAnchor;
    }

    public void openSellConfirm(Item item, Position anchor) {
        this.sellConfirmItem = Objects.requireNonNull(item);
        this.sellConfirmAnchor = Objects.requireNonNull(anchor);
    }

    public void closeSellConfirm() {
        this.sellConfirmItem = null;
        this.sellConfirmAnchor = null;
    }

    public boolean isSellConfirmOpen() {
        return sellConfirmItem != null;
    }

    public Item getSelectedMerchantItem() {
        return selectedMerchantItem;
    }

    public void setSelectedMerchantItem(Item item) {
        this.selectedMerchantItem = item;
    }

    public Item getDiscardConfirmItem() {
        return discardConfirmItem;
    }

    public Position getDiscardConfirmAnchor() {
        return discardConfirmAnchor;
    }

    public void openDiscardConfirm(Item item, Position anchor) {
        this.discardConfirmItem = Objects.requireNonNull(item);
        this.discardConfirmAnchor = Objects.requireNonNull(anchor);
    }

    public void closeDiscardConfirm() {
        this.discardConfirmItem = null;
        this.discardConfirmAnchor = null;
    }

    public boolean isDiscardConfirmOpen() {
        return discardConfirmItem != null;
    }

}
