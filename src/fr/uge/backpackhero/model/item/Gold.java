package fr.uge.backpackhero.model.item;

public class Gold extends Item {

    private int amount;

    public Gold(int amount, Shape shape) {
        super("Gold", shape);
        this.amount = amount;
    }

    public Gold(int amount) {
        this(amount, Shape.SINGLE);
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
