/**
 * Main module for the Backpack Hero game.
 * Contains engine, model, and test packages for the rogue-like dungeon crawler.
 */
module fr.uge.backpackhero {
	exports fr.uge.backpackhero;
	exports fr.uge.backpackhero.logic;
	exports fr.uge.backpackhero.gui;
	exports fr.uge.backpackhero.model.entity;
	exports fr.uge.backpackhero.model.item;
	exports fr.uge.backpackhero.model.level;
	exports fr.uge.backpackhero.model.loot;

	requires java.desktop;
	requires transitive zen;
}