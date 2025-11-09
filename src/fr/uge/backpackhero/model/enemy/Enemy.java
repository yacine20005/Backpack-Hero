package fr.uge.backpackhero.model.enemy;

import java.util.Random;

public abstract class Enemy {
	  private final String name;
	  private int hp;
	  private final int MAX_HP;
	  protected final Random randomnum = new Random();

	  private Enemy(String name, int maxHp) {
	    this.name = name;
	    this.MAX_HP = maxHp;
	    this.hp = maxHp;
	  }
	  public String name() { 
		  return name; 
	  }
	    
	  public int getHp() { 	
		  return hp; 
	  }
	   
	  public void setHp(int hp) {
	        if (hp < 0) hp = 0;
	        if (hp > MAX_HP) hp = MAX_HP;
	        this.hp = hp;
	  }
	  
	  public Random getRandom() {
	        return randomnum;
	  }
	   
	  public abstract EnemyAction chooseAction(); 
	}
