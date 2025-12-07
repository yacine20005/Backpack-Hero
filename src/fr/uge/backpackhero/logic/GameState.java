package fr.uge.backpackhero.logic;

import java.util.Objects;

import fr.uge.backpackhero.model.entity.Hero;
import fr.uge.backpackhero.model.item.Backpack;
import fr.uge.backpackhero.model.level.Dungeon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;

/**
 * GameState manages the overall state of the game, including the dungeon, hero, backpack, current position, and combat status.
 * It provides methods to access and modify these components as the game progresses.
 * 
 * @author Yacine
 */
public class GameState {

    final Dungeon dungeon = new Dungeon();
    int floor = 0;
    Position position = new Position(0, 0);
    final Hero hero = new Hero();
    Backpack backpack = new Backpack(5, 3);
    CombatEngine combatEngine = new CombatEngine();

    /**
     * Creates a new GameState with default initial values.
     */
    public GameState() {
        // Default constructor
    }

    /**
     * Returns the dungeon of the game.
     * 
     * @return the dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * Returns the current floor number.
     * 
     * @return the current floor number
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Sets the current floor number.
     * 
     * @param floor the new floor number
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Sets the current position of the hero.
     * 
     * @param position the new position of the hero
     */
    public void setPosition(Position position) {
        this.position = Objects.requireNonNull(position);
    }

    /**
     * Returns the current position of the hero.
     * 
     * @return the current position of the hero
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the hero of the game.
     * 
     * @return the hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Returns the current floor object.
     * 
     * @return the current floor
     */
    public Floor getCurrentFloor() {
        return dungeon.getFloor(floor);
    }

    /**
     * Advances the game to the next floor and resets the hero's position.
     * For simplicity, the hero's position is reset to (0,0) on the new floor but we will maybe modify that later when the map generation algorithm will be implemented.
     */
    public void exitFloor() {
        this.floor++;
        this.position = new Position(0, 0);
    }

    /**
     * Returns the backpack of the hero.
     * 
     * @return the backpack
     */
    public Backpack getBackpack() {
        return backpack;
    }

    /**
     * Returns the combat engine of the game.
     * 
     * @return the combat engine
     */
    public CombatEngine getCombatEngine() {   
        return combatEngine;                  
    }                                         

    /**
     * Checks if the game is currently in combat.
     * 
     * @return true if the game is in combat, false otherwise
     */
    public boolean isInCombat() {
        return combatEngine.isInCombat();                      
    }                                         
}
