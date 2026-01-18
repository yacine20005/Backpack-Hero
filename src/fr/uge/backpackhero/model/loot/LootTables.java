package fr.uge.backpackhero.model.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
   * Generates a list of random loot items for the given floor.
   * This is the central method used for all loot generation (combat, treasures,
   * merchant).
   * 
   * @param floorIndex the index of the floor
   * @param count      the number of items to generate
   * @param rng        a random number generator
   * @return a list of random items
   */
  public static List<Item> generateLoot(int floorIndex, int count, Random rng) {
    var loot = new ArrayList<Item>();
    for (int i = 0; i < count; i++) {
      loot.add(rollLootItem(floorIndex, rng));
    }
    return loot;
  }

  /**
   * Returns the amount of gold for the given floor index.
   * 
   * @param floorIndex the index of the floor
   * @return the amount of gold available
   */
  public static int goldForFloor(int floorIndex) {
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
    Objects.requireNonNull(enemy, "enemy cannot be null");
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
      // Floor 0: COMMON and UNCOMMON
      if (r < 25)
        return Weapon.woodenSword();
      if (r < 40)
        return Weapon.woodenBow();
      if (r < 55)
        return Armor.woodenShield();
      if (r < 70)
        return Weapon.ironSword();
      if (r < 85)
        return Weapon.ironBow();
      return Armor.ironShield();
    }
    if (floorIndex == 1) {
      // Floor 1: UNCOMMON, RARE and EPIC
      if (r < 20)
        return Weapon.ironSword();
      if (r < 35)
        return Weapon.goldenSword();
      if (r < 50)
        return Weapon.goldenBow();
      if (r < 65)
        return Armor.goldenShield();
      if (r < 80)
        return Weapon.diamondSword();
      if (r < 90)
        return Armor.diamondShield();
      return ManaStone.bigManaStone();
    }
    // Floor 2: EPIC and EXOTIC
    if (r < 20)
      return Weapon.diamondSword();
    if (r < 35)
      return Weapon.diamondBow();
    if (r < 50)
      return Armor.diamondShield();
    if (r < 65)
      return Weapon.sturn();
    if (r < 75)
      return Weapon.redDeath();
    if (r < 85)
      return Weapon.jadeRabbit();
    if (r < 92)
      return Armor.luckypants();
    if (r < 96)
      return Armor.celestialnighthawk();
    return Weapon.telesto();
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
    Objects.requireNonNull(enemies, "enemies cannot be null");
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