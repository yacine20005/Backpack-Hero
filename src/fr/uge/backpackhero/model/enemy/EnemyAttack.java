package fr.uge.backpackhero.model.enemy;

import fr.uge.backpackhero.model.Hero;

public class EnemyAttack implements EnemyAction {

    public void execute(Hero hero, Enemy enemy) {
        int damage = enemy.getAttack();
        int heroBlock = hero.getBlock();

        if (heroBlock >= damage) {
            hero.setBlock(heroBlock - damage);
        } else {
            hero.setBlock(0);
            hero.setHp(hero.getHp() - (damage - heroBlock));
        }
    }

}
