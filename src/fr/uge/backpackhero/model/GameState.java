package fr.uge.backpackhero.model;

import java.util.List;                    

import fr.uge.backpackhero.model.level.Dungeon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;

public class GameState {

    final Dungeon dungeon = new Dungeon();
    final Hero hero = new Hero();
    Backpack backpack = new Backpack(5, 3);
    Position position = new Position(0, 0);
    boolean inCombat = false;

    CombatEngine combatEngine = new CombatEngine(); 
    List<Enemy> currentEnemies;                    

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Hero getHero() {
        return hero;
    }

    public Floor getCurrentFloor() {
        return dungeon.getFloor(0);
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public CombatEngine getCombatEngine() {   
        return combatEngine;                  
    }                                         

    public boolean isInCombat() {             
        return inCombat;                      
    }                                         

    public void startCombat(List<Enemy> enemies) {
        this.currentEnemies = enemies;              
        this.inCombat = true;                       
    }                                     
    
    public void endCombat() {                
        this.currentEnemies = null;          
        this.inCombat = false;                
    }                                         

    public List<Enemy> getCurrentEnemies() { 
        return currentEnemies;                
    }                                       
}
