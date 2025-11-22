package fr.uge.backpackhero.model.enemy;

import fr.uge.backpackhero.model.Hero;

public class Attack implements EnemyAction {

    public void execute(Hero hero, Enemy enemy) {
        int damage = enemy.getAttack();
        int block = hero.getBlock();

        if (block >= damage) {
            hero.setBlock(block - damage);
        } else {
            hero.setBlock(0);
            hero.setHp(hero.getHp() - (damage - block));
        }
    }

}
