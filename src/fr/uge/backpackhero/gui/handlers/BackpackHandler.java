package fr.uge.backpackhero.gui.handlers;

import java.util.Objects;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.State;
import fr.uge.backpackhero.logic.PopupType;
import fr.uge.backpackhero.logic.MerchantMode;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.RoomType;
import fr.uge.backpackhero.model.item.Gold;
import fr.uge.backpackhero.model.item.Item;

/**
 * Handler for backpack interactions.
 * Manages item placement, rotation, and usage within the backpack.
 */
public class BackpackHandler {
    private final GameState state;
    private final View view;
    private final CombatHandler combatHandler;

    /**
     * Creates a new BackpackHandler.
     * 
     * @param state         the game state
     * @param view          the view to update
     * @param combatHandler the combat handler for item usage in combat
     */
    public BackpackHandler(GameState state, View view, CombatHandler combatHandler) {
        this.state = Objects.requireNonNull(state);
        this.view = Objects.requireNonNull(view);
        this.combatHandler = Objects.requireNonNull(combatHandler);
    }

    /**
     * Handles clicks on the backpack area.
     * 
     * @param context the application context
     * @param pointerEvent the pointer event representing the click
     */
    public void handleBackpackClick(ApplicationContext context, PointerEvent pointerEvent) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(pointerEvent, "pointerEvent cannot be null");

        if (state.isGameOver()) {
            return;
        }

        int x = (int) (pointerEvent.location().x() / 100);
        int y = (int) (pointerEvent.location().y() / 100);
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
            IO.println("Cannot use item in combat.");
            view.draw(context);
            return;
        }

        combatHandler.afterHeroAction(context);
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
            case Gold gold -> {
                IO.println("Cannot sell gold!");
                view.draw(context);
                return;
            }
            default -> {
            }
        }

        state.openSellConfirm(item, anchor.get());
        view.draw(context);
    }

    private void handleItemMovement(ApplicationContext context, Position pos) {
        var backpack = state.getBackpack();
        var currentAnchor = backpack.getAnchorAt(pos);

        // If no item is currently selected
        if (state.getSelectedItemAnchor() == null) {
            if (currentAnchor.isPresent()) {
                var anchor = currentAnchor.get(); 
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
                    IO.println("Cannot move item to this position.");
                }
                view.draw(context);
            }
        }
    }

    /**
     * Handles the rotation of the currently selected item.
     * 
     * @param context the application context
     */
    public void handleRotateItem(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        var selectedItemAnchor = state.getSelectedItemAnchor();
        if (selectedItemAnchor == null) {
            IO.println("No item selected.");
            return;
        }

        var backpack = state.getBackpack();
        if (!backpack.rotateItem(selectedItemAnchor)) {
            IO.println("Cannot rotate item in current position.");
        }
        view.draw(context);
    }

    private static boolean useItemInCombat(GameState state, Item item) {
        return state.getCombatEngine().useItem(state.getHero(), item);
    }

    /**
     * Handles the request to discard the currently selected item.
     * 
     * @param context the application context
     */
    public void handleDiscardItem(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        // Only block if another popup is already open or game is over
        if (state.getActivePopup() == PopupType.SELL_CONFIRM || state.getState() == State.HEALER_PROMPT
                || state.isGameOver() || state.isVictory()) {
            return;
        }

        var selectedAnchor = state.getSelectedItemAnchor();
        if (selectedAnchor == null) {
            IO.println("No item selected to discard.");
            return;
        }

        var item = state.getBackpack().getItems().get(selectedAnchor);
        if (item == null) {
            return;
        }
        // Prevent discarding Gold
        switch (item) {
            case Gold gold -> {
                IO.println("Cannot discard gold!");
                view.draw(context);
                return;
            }
            default -> {
            }
        }

        state.openDiscardConfirm(item, selectedAnchor);
        view.draw(context);
    }

    /**
     * Confirms the discard action.
     * 
     * @param context the application context
     */
    public void handleDiscardConfirmYes(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getActivePopup() != PopupType.DISCARD_CONFIRM)
            return;

        var item = state.getDiscardConfirmItem();
        var anchor = state.getDiscardConfirmAnchor();
        if (item != null && anchor != null) {
            state.getBackpack().removeItem(anchor);
            state.clearSelectedItem();
            IO.println("Discarded: " + item.getName());
        }
        state.closeDiscardConfirm();
        view.draw(context);
    }

    /**
     * Cancels the discard action.
     * 
     * @param context the application context
     */
    public void handleDiscardConfirmNo(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getActivePopup() != PopupType.DISCARD_CONFIRM)
            return;
        state.closeDiscardConfirm();
        view.draw(context);
    }
}
