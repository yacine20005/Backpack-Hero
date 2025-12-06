package fr.uge.backpackhero.model;


public class Hero {

    private static final int MAX_HP = 40;
    private static final int MAX_ENERGY = 3;

    private int hp;
    private int energy;
    private int mana;
    private int block;

    public Hero() {
        this.hp = MAX_HP;
        this.energy = MAX_ENERGY;
        this.mana = 0;
        this.block = 0;
    }

    public int getHp() {
        return hp;
    }

    public boolean isAlive() {
        return hp > 0;
    }
    
       
    public void setHp(int hp) {
        this.hp = Math.min(Math.max(hp, 0), MAX_HP);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.min(Math.max(energy, 0), MAX_ENERGY);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(mana, 0);
    }

    public void refreshManaAmount(Backpack backpack) {
        this.mana = backpack.getMana();
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = Math.max(block, 0);
    }

    @Override
    public String toString() {
        return "Hero - HP=" + hp + "/" + MAX_HP + ", Energy=" + energy + ", Block=" + block;
    }
}