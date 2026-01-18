package fr.uge.backpackhero.gui.handlers;

import java.util.Objects;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.State;
import fr.uge.backpackhero.logic.PopupType;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.RoomType;

public class DungeonHandler {
    private final GameState state;
    private final View view;

    public DungeonHandler(GameState state, View view) {
        this.state = Objects.requireNonNull(state);
        this.view = Objects.requireNonNull(view);
    }

    /**
     * Handles clicks on the dungeon grid.
     * 
     * @param context the application context
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

    private static boolean isMoveAllowed(Position current, Position target, Floor floor) {
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
}
