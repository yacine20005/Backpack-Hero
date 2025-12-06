package fr.uge.backpackhero.model.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.uge.backpackhero.model.Enemy;
import fr.uge.backpackhero.model.item.Armor;
import fr.uge.backpackhero.model.item.Gold;
import fr.uge.backpackhero.model.item.Item;
import fr.uge.backpackhero.model.item.ManaStone;
import fr.uge.backpackhero.model.item.Shape;
import fr.uge.backpackhero.model.item.Weapon;

/**
 * Provides loot tables for different floors and enemy drops in the game.
 * This class contains methods to retrieve possible treasure items based on the floor index
 * and to get loot dropped by enemies after combat.
 * 
 * TODO : We should maybe abandon this class in favor of a implementation that use the Enemy class and generate the loot directly from it.
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
   * @param rng a random number generator
   * @return a list of possible treasure items
   */
  public static List<Item> treasureChoices(int floorIndex, Random rng) {
    var res = new ArrayList<Item>();
    switch (floorIndex) {
      case 0 -> { // FLOOR 1
        res.add(new Weapon("Sword", 7, 1, 0, Shape.VERTICAL_3));
        res.add(new Armor("Wooden Shield", 6, 1, Shape.SQUARE_2X2));
        res.add(new Gold(5));
      }
      case 1 -> { // FLOOR 2
        res.add(new Weapon("Dart", 6, 1, 0, Shape.VERTICAL_2));
        res.add(new Armor("Leather Armor", 2, 0, Shape.VERTICAL_2));
        res.add(new ManaStone("Small Mana Stone", 3));
      }
      case 2 -> { // FLOOR 3
        res.add(new Weapon("Magic Wand", 2, 0, 8, Shape.VERTICAL_2));
        res.add(new ManaStone("Big Mana Stone", 5, Shape.HORIZONTAL_2));
        res.add(new Gold(15));
      }
      default -> res.add(new Gold(3));
    }
    return res;
  }

  /**
   * Returns a list of items dropped by the given enemy after combat.
   * 
   * @param enemy the enemy that was defeated
   * @return a list of items dropped by the enemy
   */
  public static List<Item> combatLoot(Enemy enemy) {
    var res = new ArrayList<Item>();
    res.add(new Gold(enemy.getGoldDrop()));
    return res;
  }

}
