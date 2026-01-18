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

  /**
   * Rolls a random loot item based on the floor index.
   * 
   * @param floorIndex the index of the floor
   * @param rng        the random number generator
   * @return a random item appropriate for the floor
   */
  public static Item rollLootItem(int floorIndex, Random rng) {
    int r = rng.nextInt(100);

    if (floorIndex == 0) {
      if (r < 40)
        return Weapon.woodSword();
      if (r < 60)
        return Armor.woodenShield();
      if (r < 80)
        return Weapon.woodenBow();
      return ManaStone.smallManaStone();
    }
    if (floorIndex == 1) {
      if (r < 30)
        return Weapon.sturn();
      if (r < 55)
        return Armor.emeraldShield();
      if (r < 75)
        return Armor.luckypants();
      return ManaStone.bigManaStone();
    }
    if (r < 30)
      return Weapon.telesto();
    if (r < 55)
      return Weapon.montaintop();
    if (r < 75)
      return Weapon.lastWord();
    if (r < 90)
      return Armor.celestialnighthawk();
    return Armor.liarshandshake();
  }

  /**
   * Generates loot items from defeated enemies.
   * Each dead enemy drops 1 item, with a 25% chance of dropping a second item.
   * 
   * @param enemies    the list of enemies from the combat
   * @param floorIndex the current floor index
   * @return a list of loot items
   */
  public static List<Item> generateLootFromEnemies(List<Enemy> enemies, int floorIndex) {
    var loot = new ArrayList<Item>();
    var rng = new Random();

    for (var enemy : enemies) {
      if (!enemy.isAlive()) {
        // Always drop 1 item
        loot.add(rollLootItem(floorIndex, rng));
        // 25% chance to drop a second item
        if (rng.nextInt(100) < 25) {
          loot.add(rollLootItem(floorIndex, rng));
        }
      }
    }

    return loot;
  }

}
