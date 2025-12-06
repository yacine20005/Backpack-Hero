package fr.uge.backpackhero.model;

import java.util.List;
import java.util.Objects;

import fr.uge.backpackhero.model.level.Dungeon;
import fr.uge.backpackhero.model.level.Floor;
import fr.uge.backpackhero.model.level.Position;

public class GameState {

    final Dungeon dungeon = new Dungeon();
    int floor = 0;
    final Hero hero = new Hero();
    Backpack backpack = new Backpack(5, 3);
    Position position = new Position(0, 0);
    boolean inCombat = false;
    CombatEngine combatEngine = new CombatEngine(); 
    List<Enemy> currentEnemies;                    

    public Dungeon getDungeon() {
        return dungeon;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setPosition(Position position) {
        this.position = Objects.requireNonNull(position);
    }

    public Position getPosition() {
        return position;
    }

    public Hero getHero() {
        return hero;
    }

    public Floor getCurrentFloor() {
        return dungeon.getFloor(floor);
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
        this.currentEnemies = Objects.requireNonNull(enemies);
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
