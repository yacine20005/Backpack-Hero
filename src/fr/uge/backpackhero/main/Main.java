package fr.uge.backpackhero.main;

import java.awt.Color;
import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import fr.uge.backpackhero.model.Hero;
import fr.uge.backpackhero.model.level.Dungeon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;
import fr.uge.backpackhero.model.level.Room;
import fr.uge.backpackhero.model.level.RoomType;

public class Main {

    static class GameState {
        final Dungeon dungeon = new Dungeon();
        final Hero hero = new Hero(); 
        Position heroPosition = new Position(0, 0); 

        Floor getCurrentFloor() {
            return dungeon.getFloor(0); 
        }
    }

    public static void main(String[] args) {
        Application.run(Color.BLACK, Main::gameEntry);
    }

    private static void gameEntry(ApplicationContext context) {
        var state = new GameState();

        GameView.draw(context, state.getCurrentFloor(), state.heroPosition, state.hero);

        while (true) {
            var event = context.pollOrWaitEvent(10);
            if (event == null) {
                continue; 
            }

            if (event instanceof KeyboardEvent ke) {
                if (ke.action() == KeyboardEvent.Action.KEY_PRESSED && ke.key() == KeyboardEvent.Key.Q) {
                    break; 
                }
            }

            if (event instanceof PointerEvent pe) {
                if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
                    
                    var x = pe.location().x();

                    if (x < GameView.BACKPACK_PIXEL_WIDTH) {
                        handleBackpackClick(context, state, pe);
                    } else {
                        handleDungeonClick(context, state, pe);
                    }
                }
            }
        }
        
        System.out.println("Merci d'avoir joué !");
        context.dispose(); 
    }

    private static void handleBackpackClick(ApplicationContext context, GameState state, PointerEvent pe) {
        int x = (int) (pe.location().x() / GameView.TILE_SIZE);
        int y = (int) ((pe.location().y() - GameView.TILE_SIZE) / GameView.TILE_SIZE); 

        if (y < 0) {
            System.out.println("Clic sur le titre du sac");
        } else {
            System.out.println("Clic sur le SAC: case (" + x + ", " + y + ")");
        }
    }

    private static void handleDungeonClick(ApplicationContext context, GameState state, PointerEvent pe) {
        int x = (int) ((pe.location().x() - GameView.BACKPACK_PIXEL_WIDTH) / GameView.TILE_SIZE);
        int y = (int) (pe.location().y() / GameView.TILE_SIZE);
        var clickedPos = new Position(x, y);

        if (isMoveAllowed(state.heroPosition, clickedPos, state.getCurrentFloor())) {        
            state.heroPosition = clickedPos;
            GameView.draw(context, state.getCurrentFloor(), state.heroPosition, state.hero);

            Room newRoom = state.getCurrentFloor().getRoom(clickedPos);
            if (newRoom != null) {
                if (newRoom.getType() == RoomType.ENEMY) {
                    System.out.println("Un combat commence !");
                }
                if (newRoom.getType() == RoomType.EXIT) {
                    System.out.println("C'est une sortie, mais on ne change pas d'étage.");
                }
            }
        }
    }

    private static boolean isMoveAllowed(Position current, Position target, Floor floor) {
        if (floor.getRoom(target) == null) {
            return false;
        }

        int deltaX = Math.abs(current.x() - target.x());
        int deltaY = Math.abs(current.y() - target.y());

        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }
}