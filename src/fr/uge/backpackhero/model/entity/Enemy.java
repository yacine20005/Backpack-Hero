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
	 * Creates a Rat Wolf enemy.
	 * 
	 * @return a new Rat Wolf enemy instance
	 */
	public static Enemy ratWolf() {
		return new Enemy("Rat Wolf", 10, 1, 3, 6, 3);
	}

	/**
	 * Creates a Small Rat Wolf enemy.
	 * 
	 * @return a new Small Rat Wolf enemy instance
	 */
	public static Enemy smallRatWolf() {
		return new Enemy("Small Rat Wolf", 5, 1, 0, 3, 2);
	}

	/**
	 * Creates a Goblin enemy.
	 * 
	 * @return a new Goblin enemy instance
	 */
	public static Enemy goblin() {
		return new Enemy("Goblin", 18, 8, 3, 8, 5);
	}

	/**
	 * Creates a Goblin Chief enemy.
	 * 
	 * @return a new Goblin Chief enemy instance
	 */
	public static Enemy goblinChief() {
		return new Enemy("Goblin Chief", 22, 9, 5, 10, 7);
	}

	/**
	 * Creates a Demon enemy.
	 * 
	 * @return a new Demon enemy instance
	 */
	public static Enemy demon() {
		return new Enemy("Demon", 30, 12, 7, 15, 10);
	}

	/**
	 * Creates a Demon King enemy.
	 * 
	 * @return a new Demon King enemy instance
	 */
	public static Enemy demonKing() {
		return new Enemy("Demon King", 35, 14, 9, 20, 15);
	}

	/**
	 * Creates a Frog Wizard enemy.
	 * 
	 * @return a new Frog Wizard enemy instance
	 */
	public static Enemy frogWizard() {
		return new Enemy("Frog Wizard", 25, 10, 5, 12, 8);
	}

	/**
	 * Creates a Living Shadow enemy.
	 * 
	 * @return a new Living Shadow enemy instance
	 */
	public static Enemy livingShadow() {
		return new Enemy("Living Shadow", 20, 11, 4, 10, 7);
	}

	/**
	 * Creates a Bee Queen enemy.
	 * 
	 * @return a new Bee Queen enemy instance
	 */
	public static Enemy beeQueen() {
		return new Enemy("Bee Queen", 40, 13, 8, 25, 12);
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
