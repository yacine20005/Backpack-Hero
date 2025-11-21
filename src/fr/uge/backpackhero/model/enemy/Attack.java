package fr.uge.backpackhero.model.enemy;

import fr.uge.backpackhero.model.Hero;

public class Attack implements EnemyAction {

    public void execute(Hero hero, Enemy enemy) {
        hero.setHp(hero.getHp() - enemy.getAttack());
    }

}
