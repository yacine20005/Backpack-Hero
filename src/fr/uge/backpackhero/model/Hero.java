package fr.uge.backpackhero.model;

/**
 * Represents the hero character in the game with attributes such as health
 * points, energy, mana, and block.
 * The hero can take damage, use energy, and manage mana for abilities.
 * 
 * @author Yacine
 */
public class Hero {

    private static final int MAX_HP = 40;
    private static final int MAX_ENERGY = 3;

    private int hp;
    private int energy;
    private int mana;
    private int block;

    /**
     * Creates a new Hero with default attributes.
     * The hero starts with maximum HP, full energy, zero mana and no block.
     */
    public Hero() {
        this.hp = MAX_HP;
        this.energy = MAX_ENERGY;
        this.mana = 0;
        this.block = 0;
    }

    /**
     * Returns the current health points of the hero.
     * 
     * @return the current health points
     */
    public int getHp() {
        return hp;
    }

    /**
     * Checks if the hero is alive (hp > 0).
     * 
     * @return true if the hero is alive, false otherwise
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * Sets the health points of the hero, ensuring it stays within valid bounds.
     * 
     * @param hp the new health points value
     */
    public void setHp(int hp) {
        this.hp = Math.min(Math.max(hp, 0), MAX_HP);
    }

    /**
     * Returns the current energy of the hero.
     * 
     * @return the current energy
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the energy of the hero, ensuring it stays within valid bounds.
     * 
     * @param energy the new energy value
     */
    public void setEnergy(int energy) {
        this.energy = Math.min(Math.max(energy, 0), MAX_ENERGY);
    }

    /**
     * Returns the current mana of the hero.
     * 
     * @return the current mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the mana of the hero, ensuring it is not negative.
     * 
     * @param mana the new mana value
     */
    public void setMana(int mana) {
        this.mana = Math.max(mana, 0);
    }

    /**
     * Refreshes the mana amount of the hero based on the contents of the backpack.
     * 
     * @param backpack the backpack containing mana items
     */
    public void refreshManaAmount(Backpack backpack) {
        this.mana = backpack.getMana();
    }

    /**
     * Returns the current block value of the hero.
     * 
     * @return the current block value
     */
    public int getBlock() {
        return block;
    }

    /**
     * Sets the block value of the hero, ensuring it is not negative.
     * 
     * @param block the new block value
     */
    public void setBlock(int block) {
        this.block = Math.max(block, 0);
    }

    /**
     * Returns a string representation of the hero's current state.
     * 
     * @return a string representation of the hero
     */
    @Override
    public String toString() {
        return "Hero - HP=" + hp + "/" + MAX_HP + ", Energy=" + energy + ", Block=" + block;
    }
}