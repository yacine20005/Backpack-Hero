package fr.uge.backpackhero.model.enemy;

import java.util.Random;

public abstract class Enemy {
	private final String name;
	private int hp;
	private int attack;
	private int defense;
	private int block;
	private final int MAX_HP;
	protected final Random randomnum = new Random();

	protected Enemy(String name, int maxHp, int attack, int defense) {
		this.name = name;
		this.MAX_HP = maxHp;
		this.hp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.block = 0;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if (hp < 0)
			hp = 0;
		if (hp > MAX_HP)
			hp = MAX_HP;
		this.hp = hp;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int block) {
		if (block < 0)
			block = 0;
		else
			this.defense = block;
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

	public Random getRandom() {
		return randomnum;
	}

	@Override
	public String toString() {
		return "Enemy - Name=" + name + ", HP=" + hp + "/" + MAX_HP + ", Attack=" + attack + ", Defense=" + defense + ", Block="
				+ block;
	}

	public abstract EnemyAction chooseAction();
}
