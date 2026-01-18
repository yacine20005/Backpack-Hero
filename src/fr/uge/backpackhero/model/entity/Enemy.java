package fr.uge.backpackhero.model.entity;

import java.util.Objects;

/**
 * Represents an enemy in the game with attributes such as health points,
 * attack, defense, block, and gold drop.
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
	private final int xpDrop;

	/**
	 * Creates a new Enemy with the specified attributes.
	 * 
	 * @param name     the name of the enemy
	 * @param maxHp    the maximum health points of the enemy
	 * @param attack   the attack value of the enemy
	 * @param defense  the defense value of the enemy
	 * @param goldDrop the amount of gold dropped by the enemy upon defeat
	 * @param xpDrop   the amount of XP dropped by the enemy upon defeat
	 */
	public Enemy(String name, int maxHp, int attack, int defense, int goldDrop, int xpDrop) {
		this.name = Objects.requireNonNull(name);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.goldDrop = goldDrop;
		this.xpDrop = xpDrop;
		this.block = 0;
	}

	/**
	 * Creates a Rat Loup enemy.
	 * 
	 * @return a new Rat Loup enemy instance
	 */
	public static Enemy ratLoup() {
		return new Enemy("Rat Loup", 10, 1, 3, 6, 3);
	}

	/**
	 * Creates a Petit Rat Loup enemy.
	 * 
	 * @return a new Petit Rat Loup enemy instance
	 */
	public static Enemy petitRatLoup() {
		return new Enemy("Petit Rat Loup", 5, 1, 0, 3, 2);
	}

	public static Enemy Gobelin() {
		return new Enemy("Gobelin", 18, 8, 3, 8, 5);
	}

	public static Enemy ChefGobelins() {
		return new Enemy("Chef gobelin", 22, 9, 5, 10, 7);
	}

	public static Enemy Demon() {
		return new Enemy("Demon", 30, 12, 7, 15, 10);
	}

	public static Enemy RoiDemon() {
		return new Enemy("Roi Demon", 35, 14, 9, 20, 15);
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
	 * Returns the amount of XP dropped by the enemy upon defeat.
	 * 
	 * @return the XP drop amount
	 */
	public int getXpDrop() {
		return xpDrop;
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
