package fr.uge.backpackhero.model.item;

/**
 * Represents an armor item that can be equipped by the hero.
 * Armor provides protection and has an associated energy cost.
 * 
 * @author Yacine
 */
public class Armor extends Item {

    private final int protection;
    private final int energyCost;

    /**
     * Creates a new Armor item.
     * 
     * @param name the name of the armor
     * @param protection the amount of protection provided by the armor
     * @param energyCost the energy cost to use the armor
     * @param shape the shape of the armor item in the backpack
     */
    
    public Armor(String name, int protection, int energyCost, Shape shape) {
        
    	super(name, shape);
    	
    	if (protection < 0 || energyCost < 0) {
            throw new IllegalArgumentException("Protection and energy cost must be non-negative");
        }
        
        this.protection = protection;
        this.energyCost = energyCost;
    }

    /**
     * Returns the protection value of the armor.
     * 
     * @return the protection value
     */
    public int getProtection() {
        return protection;
    }

    /**
     * Returns the energy cost to use the armor.
     * 
     * @return the energy cost
     */
    public int getEnergyCost() {
        return energyCost;
    }
}
