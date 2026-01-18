package fr.uge.backpackhero.logic;

/**
 * Enum representing the different states of the game.
 * 
 * @author Yacine
 */

public enum State {
    /** The player is exploring the dungeon. */
    EXPLORATION,
    /** The player is in combat with enemies. */
    COMBAT,
    /** The player is viewing the loot screen. */
    LOOT_SCREEN,
    /** The player is interacting with a merchant. */
    MERCHANT,
    /** The player is interacting with a healer. */
    HEALER_PROMPT,
    /** The player is choosing cells to unlock in the backpack. */
    CELL_UNLOCK
}
