package fr.uge.backpackhero.model.level;

public enum RoomType {
    CORRIDOR("C"),
    ENEMY("E"),
    TREASURE("T"),
    MERCHANT("M"),
    HEALER("H"),
    EXIT("X");

    private final String symbol;

    RoomType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
