package fr.uge.backpackhero.engine;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.model.GameState;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;

public class Controller {
    static void handleBackpackClick(ApplicationContext context, GameState state, PointerEvent pe) {
        int x = (int) (pe.location().x() / View.TILE_SIZE);
        int y = (int) ((pe.location().y() - View.TILE_SIZE) / View.TILE_SIZE);

        if (y < 0) {
            IO.println("Click out of backpack bounds");
        } else {
            IO.println("Click on the BACKPACK: slot (" + x + ", " + y + ")");
        }
    }

    static void handleDungeonClick(ApplicationContext context, GameState state, PointerEvent pe) {
        int x = (int) ((pe.location().x() - View.BACKPACK_PIXEL_WIDTH) / View.TILE_SIZE);
        int y = (int) (pe.location().y() / View.TILE_SIZE);
        var clickedPos = new Position(x, y);

        if (isMoveAllowed(state.getPosition(), clickedPos, state.getCurrentFloor())) {
            state.setPosition(clickedPos);
            View.draw(context, state.getCurrentFloor(), state.getPosition(), state.getHero());

            Room newRoom = state.getCurrentFloor().getRoom(clickedPos);
            if (newRoom != null) {
                if (newRoom.getType() == RoomType.ENEMY) {
                    System.out.println("A combat begins!");
                }
                if (newRoom.getType() == RoomType.EXIT) {
                    System.out.println("It's an exit, but we don't change floors.");
                }
            }
        }
    }

    static boolean isMoveAllowed(Position current, Position target, Floor floor) {
        if (floor.getRoom(target) == null) {
            return false;
        }

        int deltaX = Math.abs(current.x() - target.x());
        int deltaY = Math.abs(current.y() - target.y());

        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }
}
