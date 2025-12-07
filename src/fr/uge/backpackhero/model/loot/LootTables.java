package fr.uge.backpackhero.model.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.uge.backpackhero.model.entity.Enemy;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.ManaStone;
import fr.uge.backpackhero.model.item.Weapon;

/**
 * Provides loot tables for different floors and enemy drops in the game.
 * This class contains methods to retrieve possible treasure items based on the
 * floor index
 * and to get loot dropped by enemies after combat.
 * 
 * TODO : We should maybe abandon this class in favor of a implementation that
 * use the Enemy class and generate the loot directly from it.
 * 
 * @author @Naniiiii944
 */
public final class LootTables {
  private LootTables() {
  }

  /**
   * Returns a list of possible treasure items for the given floor index.
   * 
   * @param floorIndex the index of the floor
   * @param rng        a random number generator
   * @return a list of possible treasure items
   */
  public static List<Item> treasureChoices(int floorIndex, Random rng) {
    var res = new ArrayList<Item>();
    switch (floorIndex) {
      case 0 -> { // FLOOR 1
        res.add(Weapon.woodSword());
        res.add(Armor.woodenShield());
      }
      case 1 -> { // FLOOR 2
        res.add(Weapon.montaintop());
        res.add(Armor.liarshandshake());
        res.add(ManaStone.smallManaStone());
      }
      case 2 -> { // FLOOR 3
        res.add(Weapon.telesto());
        res.add(ManaStone.bigManaStone());
      }
      default -> {
        // No items for unknown floors
      }
    }
    return res;
  }

  /**
   * Returns the amount of gold for the given floor index.
   * 
   * @param floorIndex the index of the floor
   * @return the amount of gold available
   */
  public static int treasureGold(int floorIndex) {
    return switch (floorIndex) {
      case 0 -> 5;
      case 1 -> 10;
      case 2 -> 15;
      default -> 3;
    };
  }

  /**
   * Returns the amount of gold dropped by the given enemy after combat.
   * 
   * @param enemy the enemy that was defeated
   * @return the amount of gold dropped
   */
  public static int combatGold(Enemy enemy) {
    return enemy.getGoldDrop();
  }

}
