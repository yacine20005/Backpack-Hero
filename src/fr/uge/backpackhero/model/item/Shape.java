package fr.uge.backpackhero.model.item;

import fr.uge.backpackhero.model.level.Position;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

public record Shape(List<Position> cells) {

    public Shape {
        Objects.requireNonNull(cells, "cells cannot be null");
        if (cells.isEmpty()) {
            throw new IllegalArgumentException("Shape must have at least one cell");
        }
        cells = List.copyOf(cells);
    }

    // 1x1
    public static final Shape SINGLE = new Shape(List.of(
            new Position(0, 0)));

    // Verical 2 cases
    public static final Shape VERTICAL_2 = new Shape(List.of(
            new Position(0, 0),
            new Position(0, 1)));

    // Verical 3 cases (épée)
    public static final Shape VERTICAL_3 = new Shape(List.of(
            new Position(0, 0),
            new Position(0, 1),
            new Position(0, 2)));

    // Horizontal 2 cases
    public static final Shape HORIZONTAL_2 = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0)));

    // Horizontal 3 cases
    public static final Shape HORIZONTAL_3 = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0),
            new Position(2, 0)));

    // L shape
    public static final Shape L_SHAPE = new Shape(List.of(
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 1)));

    // L shape reversed
    public static final Shape L_SHAPE_REVERSED = new Shape(List.of(
            new Position(1, 0),
            new Position(0, 1),
            new Position(1, 1)));

    // Square 2x2
    public static final Shape SQUARE_2X2 = new Shape(List.of(
            new Position(0, 0), 
            new Position(1, 0),
            new Position(0, 1), 
            new Position(1, 1)));

    // T shape
    public static final Shape T_SHAPE = new Shape(List.of(
            new Position(1, 0),
            new Position(0, 1),
            new Position(1, 1),
            new Position(2, 1)));

    // T shape reversed
    public static final Shape T_SHAPE_REVERSED = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0),
            new Position(2, 0),
            new Position(1, 1)));

    
    public static final Shape CROSS = new Shape(List.of(
            new Position(1, 0),
            new Position(0, 1), 
            new Position(1, 1), 
            new Position(2, 1),
            new Position(1, 2)));

    
    public Set<Position> absolutePositions(Position anchor) {
        Objects.requireNonNull(anchor, "anchor cannot be null");
        var result = new HashSet<Position>();
        for (var cell : cells) {
            result.add(new Position(anchor.x() + cell.x(), anchor.y() + cell.y()));
        }
        return result;
    }

    private static Shape normalize(List<Position> positions) {
        int minX = positions.stream().mapToInt(Position::x).min().orElse(0);
        int minY = positions.stream().mapToInt(Position::y).min().orElse(0);
        var normalized = positions.stream()
                .map(p -> new Position(p.x() - minX, p.y() - minY))
                .toList();
        return new Shape(normalized);
    }

    public Shape rotate90() {
        var rotated = cells.stream()
                .map(p -> new Position(-p.y(), p.x()))
                .toList();
        return normalize(rotated);
    }

    public Shape rotate180() {
        return rotate90().rotate90();
    }

    public Shape rotate270() {
        return rotate90().rotate90().rotate90();
    }

    public int width() {
        return cells.stream().mapToInt(Position::x).max().orElse(0) + 1;
    }

    public int height() {
        return cells.stream().mapToInt(Position::y).max().orElse(0) + 1;
    }

    public int size() {
        return cells.size();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (cells.contains(new Position(x, y))) {
                    sb.append("[X]");
                } else {
                    sb.append("   ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
