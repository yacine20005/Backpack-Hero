package fr.uge.backpackhero.model.level;

import java.util.Objects;

public enum RoomType {
    CORRIDOR("C"),
    ENEMY("E"),
    TREASURE("T"),
    MERCHANT("M"),
    HEALER("H"),
    EXIT("X");

    private final String symbol;
    

    RoomType(String symbol) {
        this.symbol = Objects.requireNonNull(symbol, "symbol cannot be null");
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
