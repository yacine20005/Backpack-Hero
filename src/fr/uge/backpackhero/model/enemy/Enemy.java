package fr.uge.backpackhero.model.enemy;

import java.util.Objects;

public class Enemy {
	private final String name;
	private int hp;
	private int attack;
	private int defense;
	private int block;
	private final int maxHp;
	private final int goldDrop;

	public Enemy(String name, int maxHp, int attack, int defense, int goldDrop) {
		this.name = Objects.requireNonNull(name);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.goldDrop = goldDrop;
		this.block = 0;
	}

	public static Enemy ratLoup() {
		return new Enemy("Rat Loup", 10, 1, 1, 6);
	}

	public static Enemy petitRatLoup() {
		return new Enemy("Petit Rat Loup", 5, 1, 0, 3);
	}

	public int getHp() {
		return hp;
	}

	public boolean isAlive() {
		return hp > 0;
	}

	public void setHp(int hp) {
		if (hp < 0)
			hp = 0;
		if (hp > maxHp)
			hp = maxHp;
		this.hp = hp;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public int getGoldDrop() {
		return goldDrop;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Enemy - Name=" + name + ", HP=" + hp + "/" + maxHp + ", Attack=" + attack + ", Defense=" + defense
				+ ", Block=" + block;
	}
}
