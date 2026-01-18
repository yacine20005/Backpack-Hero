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

                    if (ke.key() == KeyboardEvent.Key.Z && state.isGameOver()) {
                        state = new GameState();
                        View.draw(context, state);
                    }

                    // Handle Healer prompt with Y/N
                    if (state.isHealerPromptOpen()) {
                        if (ke.key() == KeyboardEvent.Key.Y) {
                            Controller.handleHealerAccept(context, state);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.N) {
                            Controller.handleHealerDecline(context, state);
                            continue;
                        }
                    }

                    // Handle Sell confirmation with Y/N
                    if (state.isSellConfirmOpen()) {
                        if (ke.key() == KeyboardEvent.Key.Y) {
                            Controller.handleSellConfirmYes(state);
                            View.draw(context, state);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.N) {
                            Controller.handleSellConfirmNo(state);
                            View.draw(context, state);
                            continue;
                        }
                    }

                    // Handle Loot screen with C (Continue)
                    if (state.isLootScreenOpen()) {
                        if (ke.key() == KeyboardEvent.Key.C) {
                            Controller.handleLootContinue(context, state);
                            continue;
                        }
                        // Handle number keys 1-9 for item selection
                        int itemIndex = getNumberKeyIndex(ke.key());
                        if (itemIndex >= 0) {
                            Controller.handleLootItemSelection(context, state, itemIndex);
                            continue;
                        }
                    }

                    // Handle Merchant mode switching with B/S
                    var currentRoom = state.getCurrentFloor().getRoom(state.getPosition());
                    if (currentRoom.getType() == fr.uge.backpackhero.model.level.RoomType.MERCHANT) {
                        if (ke.key() == KeyboardEvent.Key.B) {
                            state.setMerchantMode("BUY");
                            View.draw(context, state);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.S) {
                            state.setMerchantMode("SELL");
                            View.draw(context, state);
                            continue;
                        }
                        // Handle number keys 1-9 for merchant item selection in BUY mode
                        if (state.getMerchantMode().equals("BUY")) {
                            int itemIndex = getNumberKeyIndex(ke.key());
                            if (itemIndex >= 0) {
                                Controller.handleMerchantItemSelection(context, state, itemIndex);
                                continue;
                            }
                        }
                    }
                }
            }

            if (event instanceof PointerEvent pe) {
                if (pe.action() == PointerEvent.Action.POINTER_DOWN) {

                    // Handle sell confirmation popup with priority
                    if (state.isSellConfirmOpen()) {
                        Controller.handleMerchantClick(state, pe);
                        View.draw(context, state);
                        continue;
                    }

                    var x = pe.location().x();

                    // If loot screen is open we handle clicks on loot screen and backpack
                    // simultaneously
                    if (state.isLootScreenOpen()) {
                        if (x < View.BACKPACK_PIXEL_WIDTH) {
                            Controller.handleBackpackClick(context, state, pe);
                        } else {
                            Controller.handleLootScreenClick(context, state, pe);
                        }
                        continue;
                    }

                    if (Controller.handleMerchantClick(state, pe)) {
                        View.draw(context, state);
                        continue;
                    }

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

    /**
     * Converts keyboard keys (A,Z,E,R,T,Y,U,I,O,P) to array indices (0-9).
     * Returns -1 if the key is not one of these keys.
     * Uses AZERTY keyboard layout top row for intuitive item selection.
     */
    private static int getNumberKeyIndex(KeyboardEvent.Key key) {
        return switch (key) {
            case A -> 0;
            case Z -> 1;
            case E -> 2;
            case R -> 3;
            case T -> 4;
            case Y -> 5;
            case U -> 6;
            case I -> 7;
            case O -> 8;
            case P -> 9;
            default -> -1;
        };
    }
}
