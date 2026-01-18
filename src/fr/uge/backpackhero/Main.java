package fr.uge.backpackhero;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.backpackhero.gui.Controller;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.MerchantMode;
import fr.uge.backpackhero.logic.PopupType;
import fr.uge.backpackhero.logic.State;

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

                    // Handle End Turn in combat with X
                    if (ke.key() == KeyboardEvent.Key.X && state.getState() == State.COMBAT && state.getState() != State.LOOT_SCREEN) {
                        Controller.handleEndTurn(context, state);
                        continue;
                    }

                    // Handle Cycle Enemy Target in combat with CTRL
                    if ((ke.key() == KeyboardEvent.Key.CTRL)
                            && state.getState() == State.COMBAT && state.getState() != State.LOOT_SCREEN) {
                        state.getCombatEngine().cycleEnemyTarget();
                        System.out
                                .println("Switched target to: " + state.getCombatEngine().getSelectedEnemy().getName());
                        View.draw(context, state);
                        continue;
                    }

                    if (ke.key() == KeyboardEvent.Key.Z && (state.isGameOver() || state.isVictory())) {
                        state = new GameState();
                        View.draw(context, state);
                    }

                    // Handle Healer prompt with Y/N
                    if (state.getState() == State.HEALER_PROMPT) {
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
                    if (state.getActivePopup() == PopupType.SELL_CONFIRM) {
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

                    // Handle Discard confirmation with Y/N
                    if (state.getActivePopup() == PopupType.DISCARD_CONFIRM) {
                        if (ke.key() == KeyboardEvent.Key.Y) {
                            Controller.handleDiscardConfirmYes(context, state);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.N) {
                            Controller.handleDiscardConfirmNo(context, state);
                            continue;
                        }
                    }

                    // Handle Discard item with D key
                    if (ke.key() == KeyboardEvent.Key.D) {
                        Controller.handleDiscardItem(context, state);
                        continue;
                    }

                    // Handle Loot screen with C (Continue)
                    if (state.getState() == State.LOOT_SCREEN) {
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
                            state.setMerchantMode(MerchantMode.BUY);
                            View.draw(context, state);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.S) {
                            state.setMerchantMode(MerchantMode.SELL);
                            View.draw(context, state);
                            continue;
                        }
                        // Handle number keys 1-9 for merchant item selection in BUY mode
                        if (state.getMerchantMode() == MerchantMode.BUY) {
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
                    if (state.getActivePopup() == PopupType.SELL_CONFIRM) {
                        Controller.handleMerchantClick(state, pe);
                        View.draw(context, state);
                        continue;
                    }

                    var x = pe.location().x();

                    // If loot screen is open we handle clicks on loot screen and backpack
                    // simultaneously
                    if (state.getState() == State.LOOT_SCREEN) {
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
