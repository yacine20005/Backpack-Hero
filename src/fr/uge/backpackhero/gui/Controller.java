package fr.uge.backpackhero.gui;

import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.gui.handlers.BackpackHandler;
import fr.uge.backpackhero.gui.handlers.CombatHandler;
import fr.uge.backpackhero.gui.handlers.DungeonHandler;
import fr.uge.backpackhero.gui.handlers.HealerHandler;
import fr.uge.backpackhero.gui.handlers.LootHandler;
import fr.uge.backpackhero.gui.handlers.MerchantHandler;
import fr.uge.backpackhero.logic.GameState;

/**
 * Controller class that delegates user interactions to specialized handlers.
 * Follows the Single Responsibility Principle by delegating each concern to a
 * specific handler.
 */
public class Controller {

    private final GameState state;
    private final View view;

    // Specialized handlers
    private final BackpackHandler backpackHandler;
    private final CombatHandler combatHandler;
    private final DungeonHandler dungeonHandler;
    private final HealerHandler healerHandler;
    private final LootHandler lootHandler;
    private final MerchantHandler merchantHandler;

    /**
     * Creates a new Controller instance with all specialized handlers.
     * 
     * @param state the game state to control
     * @param view  the view to update after actions
     */
    public Controller(GameState state, View view) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
        this.view = Objects.requireNonNull(view, "view cannot be null");

        // Initialize handlers in correct order (some depend on others)
        this.combatHandler = new CombatHandler(state, view);
        this.backpackHandler = new BackpackHandler(state, view, combatHandler);
        this.dungeonHandler = new DungeonHandler(state, view);
        this.healerHandler = new HealerHandler(state, view);
        this.lootHandler = new LootHandler(state, view);
        this.merchantHandler = new MerchantHandler(state, view);
    }

    // Backpack Operations

    /**
     * Handles clicks on the backpack area.
     * 
     * @param context the application context
     * @param pointerEvent the pointer event representing the click
     */
    public void handleBackpackClick(ApplicationContext context, PointerEvent pointerEvent) {
        backpackHandler.handleBackpackClick(context, pointerEvent);
    }

    /**
     * Handles the rotation of the currently selected item.
     * 
     * @param context the application context
     */
    public void handleRotateItem(ApplicationContext context) {
        backpackHandler.handleRotateItem(context);
    }

    /**
     * Handles the request to discard the currently selected item.
     * 
     * @param context the application context
     */
    public void handleDiscardItem(ApplicationContext context) {
        backpackHandler.handleDiscardItem(context);
    }

    /**
     * Confirms the discard action.
     * 
     * @param context the application context
     */
    public void handleDiscardConfirmYes(ApplicationContext context) {
        backpackHandler.handleDiscardConfirmYes(context);
    }

    /**
     * Cancels the discard action.
     * 
     * @param context the application context
     */
    public void handleDiscardConfirmNo(ApplicationContext context) {
        backpackHandler.handleDiscardConfirmNo(context);
    }

    // Combat Operations

    /**
     * Ends the player's turn in combat.
     * 
     * @param context the application context
     */
    public void handleEndTurn(ApplicationContext context) {
        combatHandler.handleEndTurn(context);
    }

    // Dungeon Operations

    /**
     * Handles clicks on the dungeon area (outside the backpack).
     * 
     * @param context the application context
     * @param pointerEvent the pointer event representing the click
     */
    public void handleDungeonClick(ApplicationContext context, PointerEvent pointerEvent) {
        dungeonHandler.handleDungeonClick(context, pointerEvent);
    }

    // Healer Operations

    /**
     * Accepts the healer's offer.
     * 
     * @param context the application context
     */
    public void handleHealerAccept(ApplicationContext context) {
        healerHandler.handleHealerAccept(context);
    }

    /**
     * Declines the healer's offer.
     * 
     * @param context the application context
     */
    public void handleHealerDecline(ApplicationContext context) {
        healerHandler.handleHealerDecline(context);
    }

    // Loot Operations

    /**
     * Continues from the loot screen, closing it.
     * 
     * @param context the application context
     */
    public void handleLootContinue(ApplicationContext context) {
        lootHandler.handleLootContinue(context);
    }

    /**
     * Handles clicks on the loot screen.
     * 
     * @param context the application context
     * @param pe the pointer event representing the click
     * @return true if the click was handled, false otherwise
     */
    public boolean handleLootScreenClick(ApplicationContext context, PointerEvent pe) {
        return lootHandler.handleLootScreenClick(context, pe);
    }

    /**
     * Handles the selection of an item in the loot screen.
     * 
     * @param context the application context
     * @param index the index of the selected item
     */
    public void handleLootItemSelection(ApplicationContext context, int index) {
        lootHandler.handleLootItemSelection(context, index);
    }

    // Merchant Operations

    /**
     * Handles clicks on the merchant interface.
     * 
     * @param event the pointer event representing the click
     * @return true if the click was handled by the merchant interface, false otherwise
     */
    public boolean handleMerchantClick(PointerEvent event) {
        return merchantHandler.handleMerchantClick(event);
    }

    /**
     * Confirms the sale of an item to the merchant.
     */
    public void handleSellConfirmYes() {
        merchantHandler.handleSellConfirmYes();
    }

    /**
     * Cancels the sale of an item to the merchant.
     */
    public void handleSellConfirmNo() {
        merchantHandler.handleSellConfirmNo();
    }

    /**
     * Handles the selection of an item in the merchant's shop.
     * 
     * @param context the application context
     * @param index the index of the selected item
     */
    public void handleMerchantItemSelection(ApplicationContext context, int index) {
        merchantHandler.handleMerchantItemSelection(context, index);
    }
}