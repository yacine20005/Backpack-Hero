package fr.uge.backpackhero.model.enemy;

public class PetitRatLoup extends Enemy {

    public PetitRatLoup() {
        super("Petit Rat Loup", 5, 1, 0);
    }

    @Override
    public EnemyAction chooseAction() {
        int random = getRandom().nextInt(2);
        switch (random) {
            case 0:
                return new Attack();
            case 1:
                return new Defend();
            default:
                throw new IllegalStateException("Unexpected value: " + random);
        }
    }
}
