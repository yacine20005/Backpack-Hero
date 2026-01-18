package fr.uge.backpackhero.gui.handlers;

import java.util.Objects;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.State;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;
import fr.uge.backpackhero.model.item.Item;

/**
 * Handler for loot interactions.
 * Manages item selection and collection from the loot screen.
 */
public class LootHandler {
    private final GameState state;
    private final View view;

    /**
     * Creates a new LootHandler.
     * 
     * @param state the game state
     * @param view  the view to update
     */
    public LootHandler(GameState state, View view) {
        this.state = Objects.requireNonNull(state);
        this.view = Objects.requireNonNull(view);
    }

    /**
     * Handles the continue action from the loot screen, closing it.
     * 
     * @param context the application context
     */
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

    /**
     * Handles clicks on the loot screen to prevent interaction with underlying elements.
     * 
     * @param context the application context
     * @param pe the pointer event representing the click
     * @return true if the click was handled/consumed, false otherwise
     */
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

    /**
     * Handles the selection of a loot item by index.
     * 
     * @param context the application context
     * @param index the index of the selected item
     */
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
                IO.println("Deselected loot item: " + selectedItem.getName());
            } else {
                state.setSelectedLootItem(selectedItem);
                IO.println("Selected loot item: " + selectedItem.getName());
            }
            view.draw(context);
        }
    }
}
