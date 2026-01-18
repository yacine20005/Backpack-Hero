package fr.uge.backpackhero.gui.handlers;

import java.util.ArrayList;
import java.util.Objects;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.State;
import fr.uge.backpackhero.logic.PopupType;
import fr.uge.backpackhero.model.level.RoomType;
import fr.uge.backpackhero.model.item.Item;

public class MerchantHandler {
    private final GameState state;
    private final View view;

    public MerchantHandler(GameState state, View view) {
        this.state = Objects.requireNonNull(state);
        this.view = Objects.requireNonNull(view);
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
}
