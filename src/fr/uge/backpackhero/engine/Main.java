package fr.uge.backpackhero.engine;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.model.GameState;

public class Main {

    private final static int EVENT_POLL_TIMEOUT_MS = 10;

    public static void main(String[] args) {
        Application.run(Color.BLACK, Main::gameEntry);
    }

    private static void gameEntry(ApplicationContext context) {
        var state = new GameState();

        View.draw(context, state);

        while (true) {
            var event = context.pollOrWaitEvent(EVENT_POLL_TIMEOUT_MS);
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

                    if (x < View.BACKPACK_PIXEL_WIDTH) {
                        Controller.handleBackpackClick(context, state, pe);
                    } else {
                        Controller.handleDungeonClick(context, state, pe);
                    }
                }
            }
        }

        System.out.println("Thanks for playing !");
        context.dispose();
    }
}
