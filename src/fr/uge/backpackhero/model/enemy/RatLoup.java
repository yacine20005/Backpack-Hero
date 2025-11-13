package fr.uge.backpackhero.model.enemy;

public class RatLoup extends Enemy {

    public RatLoup() {
        super("Rat Loup", 10, 1, 1);
    }

    public EnemyAction chooseAction() {
        int random = getRandom().nextInt(2);
        switch (random) {
            case 0: {
                return new Attack();
            }
            case 1: {
                return new Defend();
            }
        }
    }
}
