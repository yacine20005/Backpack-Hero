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
 * 
 * @author @Naniiiii944
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

    public void handleBackpackClick(ApplicationContext context, PointerEvent pointerEvent) {
        backpackHandler.handleBackpackClick(context, pointerEvent);
    }

    public void handleRotateItem(ApplicationContext context) {
        backpackHandler.handleRotateItem(context);
    }

    public void handleDiscardItem(ApplicationContext context) {
        backpackHandler.handleDiscardItem(context);
    }

    public void handleDiscardConfirmYes(ApplicationContext context) {
        backpackHandler.handleDiscardConfirmYes(context);
    }

    public void handleDiscardConfirmNo(ApplicationContext context) {
        backpackHandler.handleDiscardConfirmNo(context);
    }

    // Combat Operations

    public void handleEndTurn(ApplicationContext context) {
        combatHandler.handleEndTurn(context);
    }

    // Dungeon Operations

    public void handleDungeonClick(ApplicationContext context, PointerEvent pointerEvent) {
        dungeonHandler.handleDungeonClick(context, pointerEvent);
    }

    // Healer Operations

    public void handleHealerAccept(ApplicationContext context) {
        healerHandler.handleHealerAccept(context);
    }

    public void handleHealerDecline(ApplicationContext context) {
        healerHandler.handleHealerDecline(context);
    }

    // Loot Operations

    public void handleLootContinue(ApplicationContext context) {
        lootHandler.handleLootContinue(context);
    }

    public boolean handleLootScreenClick(ApplicationContext context, PointerEvent pe) {
        return lootHandler.handleLootScreenClick(context, pe);
    }

    public void handleLootItemSelection(ApplicationContext context, int index) {
        lootHandler.handleLootItemSelection(context, index);
    }

    // Merchant Operations

    public boolean handleMerchantClick(PointerEvent event) {
        return merchantHandler.handleMerchantClick(event);
    }

    public void handleSellConfirmYes() {
        merchantHandler.handleSellConfirmYes();
    }

    public void handleSellConfirmNo() {
        merchantHandler.handleSellConfirmNo();
    }

    public void handleMerchantItemSelection(ApplicationContext context, int index) {
        merchantHandler.handleMerchantItemSelection(context, index);
    }
}