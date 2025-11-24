package fr.uge.backpackhero.item;

public class Gold extends Item {

    private int amount;

    public Gold(int amount) {
        super("Gold");
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Gold amount cannot be negative");
        }
        this.amount = amount;
    }

    public void addAmount(int amountToAdd) {
        this.amount += amountToAdd;
    }
}
