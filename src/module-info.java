/**
 * Main module for the Backpack Hero game.
 * Contains engine, model, and test packages for the rogue-like dungeon crawler.
 */
module fr.uge.backpackhero {
	exports fr.uge.backpackhero.engine;
	exports fr.uge.backpackhero.model.item;
	exports fr.uge.backpackhero.model;
	exports fr.uge.backpackhero.model.level;
	exports fr.uge.backpackhero.model.loot;
	exports fr.uge.backpackhero.test;


	requires java.desktop;
	requires zen;
}