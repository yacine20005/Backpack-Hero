package fr.uge.backpackhero.model.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.uge.backpackhero.item.Item;
import fr.uge.backpackhero.item.magic.MagicItem;
import fr.uge.backpackhero.item.magic.ManaStone;
import fr.uge.backpackhero.item.misc.Gold;
import fr.uge.backpackhero.item.protection.Armor;
import fr.uge.backpackhero.item.protection.Shield;
import fr.uge.backpackhero.item.weapon.WeaponMelee;
import fr.uge.backpackhero.item.weapon.WeaponRanged;
import fr.uge.backpackhero.model.enemy.Enemy;
import fr.uge.backpackhero.model.enemy.PetitRatLoup;
import fr.uge.backpackhero.model.enemy.RatLoup;

public final class LootTables {
  private LootTables() {}

  
  public static List<Item> treasureChoices(int floorIndex, Random rng) {
    var res = new ArrayList<Item>();
    switch (floorIndex) {
      case 0 -> { // FLOOR 1
        res.add(new WeaponMelee("Sword", 7, 1));       
        res.add(new Shield("Wooden Shield", 6, 1));     
        res.add(new Gold(5));          
      }
      case 1 -> { // FLOOR 2
        res.add(new WeaponRanged("Dart", 6, 1));        
        res.add(new Armor("Leather Armor", 2, 0));      
        res.add(new ManaStone("Small Mana Stone", 3));  
      }
      case 2 -> { // FLOOR 3
        res.add(new MagicItem("Magic Wand", 2, 8));     
        res.add(new ManaStone("Big Mana Stone", 5));    
        res.add(new Gold(15));        
      }
      default -> res.add(new Gold(3));
    }
    return res;
  }
  
  public static List<Item> combatLoot(Enemy enemy) {
	    var res = new ArrayList<Item>();
	    if (enemy instanceof PetitRatLoup) {
	      res.add(new Gold(3));   
	    } else if (enemy instanceof RatLoup) {
	      res.add(new Gold(6));   
	    } else {
	      res.add(new Gold(4));   
	    }
	    return res;
	  }

}
