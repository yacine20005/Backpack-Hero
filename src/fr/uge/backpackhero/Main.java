package fr.uge.backpackhero;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;

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
import fr.uge.backpackhero.model.score.HOF;

/**
 * Main class to start the Backpack Hero game.
 * 
 * @author @Naniiiii944
 */
public class Main {

    private Main() {
        // Private constructor to prevent warnings
    }

    private static final String SAVE_FILE_PATH = "halloffame.txt";
    private final static int EVENT_POLL_TIMEOUT_MS = 10;
    private static final HOF HALL_OF_FAME = initializeHOF();
    private static String playerName = "Player";

    /**
     * Initializes the Hall of Fame, handling potential I/O errors.
     * 
     * @return a new HOF instance, or null if initialization fails
     */
    private static HOF initializeHOF() {
        try {
            return new HOF(Path.of(SAVE_FILE_PATH));
        } catch (IOException e) {
            IO.println("Failed to load Hall of Fame: " + e.getMessage());
            IO.println("Starting with empty Hall of Fame.");
            return null;
        }
    }

    /**
     * Main method to launch the game.
     * 
     * @param args command line arguments: args[0] = player name (optional)
     */

    public static void main(String[] args) {
        if (args.length > 0 && !args[0].isBlank()) {
            playerName = args[0];
        }
        Application.run(Color.BLACK, Main::gameEntry);
    }

    private static void gameEntry(ApplicationContext context) {
        var state = new GameState();
        var view = new View(state);
        var controller = new Controller(state, view);
        boolean scoreSubmitted = false;

        view.draw(context);

        while (true) {
            // Check for game over or victory and submit score once
            if ((state.isGameOver() || state.isVictory()) && !scoreSubmitted) {
                int score = state.calculateScore();
                int level = state.getHero().getLevel();
                if (HALL_OF_FAME != null) {
                    try {
                        HALL_OF_FAME.submitScore(playerName, score, level);
                    } catch (IOException e) {
                        IO.println("Failed to save score to Hall of Fame: " + e.getMessage());
                    }
                }
                scoreSubmitted = true;
            }

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
                        controller.handleRotateItem(context);
                    }

                    // Handle End Turn in combat with X
                    if (ke.key() == KeyboardEvent.Key.X && state.getState() == State.COMBAT
                            && state.getState() != State.LOOT_SCREEN) {
                        controller.handleEndTurn(context);
                        continue;
                    }

                    // Handle Cycle Enemy Target in combat with CTRL
                    if ((ke.key() == KeyboardEvent.Key.CTRL)
                            && state.getState() == State.COMBAT && state.getState() != State.LOOT_SCREEN) {
                        state.getCombatEngine().cycleEnemyTarget();
                        IO.println("Switched target to: " + state.getCombatEngine().getSelectedEnemy().getName());
                        view.draw(context);
                        continue;
                    }

                    if (ke.key() == KeyboardEvent.Key.Z && (state.isGameOver() || state.isVictory())) {
                        state = new GameState();
                        scoreSubmitted = false;
                        view.draw(context);
                    }

                    // Handle Healer prompt with Y/N
                    if (state.getState() == State.HEALER_PROMPT) {
                        if (ke.key() == KeyboardEvent.Key.Y) {
                            controller.handleHealerAccept(context);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.N) {
                            controller.handleHealerDecline(context);
                            continue;
                        }
                    }

                    // Handle Sell confirmation with Y/N
                    if (state.getActivePopup() == PopupType.SELL_CONFIRM) {
                        if (ke.key() == KeyboardEvent.Key.Y) {
                            controller.handleSellConfirmYes();
                            view.draw(context);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.N) {
                            controller.handleSellConfirmNo();
                            view.draw(context);
                            continue;
                        }
                    }

                    // Handle Discard confirmation with Y/N
                    if (state.getActivePopup() == PopupType.DISCARD_CONFIRM) {
                        if (ke.key() == KeyboardEvent.Key.Y) {
                            controller.handleDiscardConfirmYes(context);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.N) {
                            controller.handleDiscardConfirmNo(context);
                            continue;
                        }
                    }

                    // Handle Discard item with D key
                    if (ke.key() == KeyboardEvent.Key.D) {
                        controller.handleDiscardItem(context);
                        continue;
                    }

                    // Handle Loot screen with C (Continue)
                    if (state.getState() == State.LOOT_SCREEN) {
                        if (ke.key() == KeyboardEvent.Key.C) {
                            controller.handleLootContinue(context);
                            continue;
                        }
                        // Handle number keys 1-9 for item selection
                        int itemIndex = getNumberKeyIndex(ke.key());
                        if (itemIndex >= 0) {
                            controller.handleLootItemSelection(context, itemIndex);
                            continue;
                        }
                    }

                    // Handle Merchant mode switching with B/S
                    var currentRoom = state.getCurrentFloor().getRoom(state.getPosition());
                    if (currentRoom.getType() == fr.uge.backpackhero.model.level.RoomType.MERCHANT) {
                        if (ke.key() == KeyboardEvent.Key.B) {
                            state.setMerchantMode(MerchantMode.BUY);
                            view.draw(context);
                            continue;
                        }
                        if (ke.key() == KeyboardEvent.Key.S) {
                            state.setMerchantMode(MerchantMode.SELL);
                            view.draw(context);
                            continue;
                        }
                        // Handle number keys 1-9 for merchant item selection in BUY mode
                        if (state.getMerchantMode() == MerchantMode.BUY) {
                            int itemIndex = getNumberKeyIndex(ke.key());
                            if (itemIndex >= 0) {
                                controller.handleMerchantItemSelection(context, itemIndex);
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
                        controller.handleMerchantClick(pe);
                        view.draw(context);
                        continue;
                    }

                    var x = pe.location().x();

                    // If loot screen is open we handle clicks on loot screen and backpack
                    // simultaneously
                    if (state.getState() == State.LOOT_SCREEN) {
                        if (x < View.BACKPACK_PIXEL_WIDTH) {
                            controller.handleBackpackClick(context, pe);
                        } else {
                            controller.handleLootScreenClick(context, pe);
                        }
                        continue;
                    }

                    if (controller.handleMerchantClick(pe)) {
                        view.draw(context);
                        continue;
                    }

                    if (x < View.BACKPACK_PIXEL_WIDTH) {
                        controller.handleBackpackClick(context, pe);
                    } else {
                        controller.handleDungeonClick(context, pe);
                    }
                }
            }
        }

        IO.println("Thanks for playing !");
        context.dispose();
    }

    /**
     * Returns the Hall of Fame instance.
     * 
     * @return the Hall of Fame
     */
    public static HOF getHallOfFame() {
        return HALL_OF_FAME;
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
