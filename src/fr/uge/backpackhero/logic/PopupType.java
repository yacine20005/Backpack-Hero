package fr.uge.backpackhero.logic;

/**
 * Enum representing the different types of confirmation popups that can overlay the game.
 * These are not game states but UI overlays that appear on top of the current state.
 * 
 */
public enum PopupType {
    /** Popup to confirm selling an item. */
    SELL_CONFIRM,
    /** Popup to confirm discarding an item. */
    DISCARD_CONFIRM
}
