package fr.uge.backpackhero.model.enemy;

import fr.uge.backpackhero.model.Hero;

public interface EnemyAction {

    void execute(Hero hero, Enemy enemy);
    
}
