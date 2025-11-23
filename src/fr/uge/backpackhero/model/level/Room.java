package fr.uge.backpackhero.model.level;

import java.util.Objects;

public class Room {
    private final RoomType type;

    public Room(RoomType type) {
        this.type = Objects.requireNonNull(type);
    }

    public RoomType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
