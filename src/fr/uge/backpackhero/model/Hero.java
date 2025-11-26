package fr.uge.backpackhero.model;


public class Hero {

    private static final int MAX_HP = 40;
    private static final int MAX_ENERGY = 3;

    private int hp;
    private int energy;
    private int block;

    
    private final Backpack backpack;

    public Hero() {
        this.hp = MAX_HP;
        this.energy = MAX_ENERGY;
        this.block = 0;
       
        this.backpack = new Backpack(); 
    }

    
    public Backpack getBackpack() {
        return backpack;
    }

    public int getHp() {
        return hp;
    }

    public boolean isAlive() {
        return hp > 0;
    }
    
       
    public void setHp(int hp) {
        if (hp < 0) {
            hp = 0;
        } else if (hp > MAX_HP) {
            hp = MAX_HP;
        } else {
            this.hp = hp;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        if (energy < 0) {
            this.energy = 0;
        } else if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else {
            this.energy = energy;
        }
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        if (block < 0) {
            this.block = 0;
        } else {
            this.block = block;
        }
    }

    @Override
    public String toString() {
        return "Hero - HP=" + hp + "/" + MAX_HP + ", Energy=" + energy + ", Block=" + block;
    }
}