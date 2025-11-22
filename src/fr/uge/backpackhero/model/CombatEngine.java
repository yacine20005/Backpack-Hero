package fr.uge.backpackhero.model;

import fr.uge.backpackhero.model.enemy.Enemy;
import fr.uge.backpackhero.model.enemy.EnemyAction;

public class CombatEngine {

    public void startTurn(Hero hero) {
        hero.setEnergy(3);
        hero.setBlock(0); // We reset the block at the start of the turn like in Slay the Spire 
        // TODO : verify if it's correct in Backpack Hero
    }

    public void enemyTurn(Hero hero, Enemy enemy) {
        EnemyAction action = enemy.chooseAction();
        action.execute(hero, enemy);
    }
}
