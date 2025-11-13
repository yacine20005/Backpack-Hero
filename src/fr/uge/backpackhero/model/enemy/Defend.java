package fr.uge.backpackhero.model.enemy;

import fr.uge.backpackhero.model.Hero;

public class Defend implements EnemyAction {

    public void execute(Hero hero, Enemy enemy) {
        enemy.setBlock(enemy.getDefense());
    }

}
