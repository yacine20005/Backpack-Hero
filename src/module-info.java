/**
 * Module definition for the Backpack Hero game.
 */
module fr.uge.backpackhero {
	exports fr.uge.backpackhero;
	exports fr.uge.backpackhero.logic;
	exports fr.uge.backpackhero.gui;
	exports fr.uge.backpackhero.model.entity;
	exports fr.uge.backpackhero.model.item;
	exports fr.uge.backpackhero.model.level;
	exports fr.uge.backpackhero.model.loot;
	exports fr.uge.backpackhero.model.score;

	requires java.desktop;
	requires transitive zen;
}