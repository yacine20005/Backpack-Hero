package fr.uge.backpackhero.gui.handlers;

import java.util.Objects;
import com.github.forax.zen.ApplicationContext;
import fr.uge.backpackhero.gui.View;
import fr.uge.backpackhero.logic.GameState;
import fr.uge.backpackhero.logic.State;

/**
 * Handler for healer interactions.
 * Manages the healer prompt logic.
 */
public class HealerHandler {
    private final GameState state;
    private final View view;

    /**
     * Creates a new HealerHandler.
     * 
     * @param state the game state
     * @param view  the view to update
     */
    public HealerHandler(GameState state, View view) {
        this.state = Objects.requireNonNull(state);
        this.view = Objects.requireNonNull(view);
    }

    /**
     * Handles the acceptance of the healer's offer.
     * 
     * @param context the application context
     */
    public void handleHealerAccept(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getState() != State.HEALER_PROMPT)
            return;

        int cost = state.getHealerCost();
        int heal = state.getHealerHealAmount();

        if (!state.getBackpack().spendGold(cost)) {
            IO.println("Not enough gold.");
            view.draw(context);
            return;
        }

        var hero = state.getHero();
        int before = hero.getHp();
        hero.setHp(before + heal);

        state.closeHealerPrompt();
        view.draw(context);
    }

    /**
     * Handles the decline of the healer's offer.
     * 
     * @param context the application context
     */
    public void handleHealerDecline(ApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (state.getState() != State.HEALER_PROMPT)
            return;
        state.closeHealerPrompt();
        view.draw(context);
    }
}
