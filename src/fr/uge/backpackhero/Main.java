package fr.uge.backpackhero;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.gui.Controller;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;

/**
 * Main class to start the Backpack Hero game.
 * 
 * @author @Naniiiii944
 */
public class Main {

    private Main() {
        // Private constructor to prevent warnings
    }

    private final static int EVENT_POLL_TIMEOUT_MS = 10;

    /**
     * Main method to launch the game.
     * 
     * @param args command line arguments (not used)
     */

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
                if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
                    if (ke.key() == KeyboardEvent.Key.Q) {
                        break;
                    }
                    if (ke.key() == KeyboardEvent.Key.R) {
                        Controller.handleRotateItem(context, state);
                    }
                }
            }

            if (event instanceof PointerEvent pe) {
                if (pe.action() == PointerEvent.Action.POINTER_DOWN) {

                    if (state.isHealerPromptOpen()) {
                        Controller.handleHealerPromptClick(context, state, pe);
                        continue;
                    }
               
                    if (Controller.handleMerchantClick(state, pe)) {
                        View.draw(context, state);
                        continue;
                    }

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
