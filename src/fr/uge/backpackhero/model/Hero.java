package fr.uge.backpackhero.model;

public class Hero {
    
    private int hp;
    private static final int MAX_HP = 40;

    public Hero() {
        this.hp = MAX_HP;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0) hp = 0;
        else if (hp > MAX_HP) hp = MAX_HP;
        else this.hp = hp;
    }
}
