package fr.uge.backpackhero.model;

import java.util.Objects;

/**
 * Represents an enemy in the game with attributes such as health points, attack, defense, block, and gold drop.
 * 
 * @author Yacine
 */
public class Enemy {
	private final String name;
	private int hp;
	private int attack;
	private int defense;
	private int block;
	private final int maxHp;
	private final int goldDrop;

	/**
	 * Creates a new Enemy with the specified attributes.
	 * 
	 * @param name the name of the enemy
	 * @param maxHp the maximum health points of the enemy
	 * @param attack the attack value of the enemy
	 * @param defense the defense value of the enemy
	 * @param goldDrop the amount of gold dropped by the enemy upon defeat
	 */
	public Enemy(String name, int maxHp, int attack, int defense, int goldDrop) {
		this.name = Objects.requireNonNull(name);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.goldDrop = goldDrop;
		this.block = 0;
	}

	/**
	 * Creates a Rat Loup enemy.
	 * 
	 * @return a new Rat Loup enemy instance
	 */
	public static Enemy ratLoup() {
		return new Enemy("Rat Loup", 10, 1, 1, 6);
	}

	/**
	 * Creates a Petit Rat Loup enemy.
	 * 
	 * @return a new Petit Rat Loup enemy instance
	 */
	public static Enemy petitRatLoup() {
		return new Enemy("Petit Rat Loup", 5, 1, 0, 3);
	}

	/**
	 * Returns the current health points of the enemy.
	 * 
	 * @return the current health points
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Checks if the enemy is alive (hp > 0).
	 * 
	 * @return true if the enemy is alive, false otherwise
	 */
	public boolean isAlive() {
		return hp > 0;
	}

	/**
	 * Sets the health points of the enemy.
	 * 
	 * @param hp the new health points value
	 */
	public void setHp(int hp) {
		this.hp = Math.min(Math.max(hp, 0), maxHp);
	}

	/**
	 * Returns the defense value of the enemy.
	 * 
	 * @return the defense value
	 */
	public int getDefense() {
		return defense;
	}

	/**
	 * Sets the defense value of the enemy.
	 * 
	 * @param defense the new defense value
	 */
	public void setDefense(int defense) {
		this.defense = Math.max(defense, 0);
	}

	/**
	 * Returns the attack value of the enemy.
	 * 
	 * @return the attack value
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * Sets the attack value of the enemy.
	 * 
	 * @param attack the new attack value
	 */
	public void setAttack(int attack) {
		this.attack = Math.max(attack, 0);
	}

	/**
	 * Returns the block value of the enemy.
	 * 
	 * @return the block value
	 */
	public int getBlock() {
		return block;
	}

	/**
	 * Sets the block value of the enemy.
	 * 
	 * @param block the new block value
	 */
	public void setBlock(int block) {
		this.block = Math.max(block, 0);
	}

	/**
	 * Returns the amount of gold dropped by the enemy upon defeat.
	 * 
	 * @return the gold drop amount
	 */
	public int getGoldDrop() {
		return goldDrop;
	}

	/**
	 * Returns the name of the enemy.
	 * 
	 * @return the name of the enemy
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a string representation of the enemy.
	 * 
	 * @return a string representation of the enemy
	 */
	@Override
	public String toString() {
		return "Enemy - Name=" + name + ", HP=" + hp + "/" + maxHp + ", Attack=" + attack + ", Defense=" + defense
				+ ", Block=" + block;
	}
}
