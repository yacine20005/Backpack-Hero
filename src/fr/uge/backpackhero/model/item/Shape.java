package fr.uge.backpackhero.model.item;

import fr.uge.backpackhero.model.level.Position;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents the shape of an item in the backpack.
 * The shape is defined by a list of relative positions that the item occupies.
 * The shape can be rotated to fit different orientations in the backpack.
 * 
 * @param cells the list of relative positions that define the shape
 * 
 */
public record Shape(List<Position> cells) {

    /**
     * Creates a new Shape with the given list of cells.
     * 
     * @param cells the list of relative positions that define the shape
     */
    public Shape {
        Objects.requireNonNull(cells, "cells cannot be null");
        if (cells.isEmpty()) {
            throw new IllegalArgumentException("Shape must have at least one cell");
        }
        cells = List.copyOf(cells);
    }

    /** A single 1x1 cell shape. */
    public static final Shape SINGLE = new Shape(List.of(
            new Position(0, 0)));

    /** A vertical shape spanning 2 cells. */
    public static final Shape VERTICAL_2 = new Shape(List.of(
            new Position(0, 0),
            new Position(0, 1)));

    /** A vertical shape spanning 3 cells (sword-like). */
    public static final Shape VERTICAL_3 = new Shape(List.of(
            new Position(0, 0),
            new Position(0, 1),
            new Position(0, 2)));

    /** A horizontal shape spanning 2 cells. */
    public static final Shape HORIZONTAL_2 = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0)));

    /** A horizontal shape spanning 3 cells. */
    public static final Shape HORIZONTAL_3 = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0),
            new Position(2, 0)));

    /** An L-shaped configuration. */
    public static final Shape L_SHAPE = new Shape(List.of(
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 1)));

    /** A reversed L-shaped configuration. */
    public static final Shape L_SHAPE_REVERSED = new Shape(List.of(
            new Position(1, 0),
            new Position(0, 1),
            new Position(1, 1)));

    /** A square 2x2 shape. */
    public static final Shape SQUARE_2X2 = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0),
            new Position(0, 1),
            new Position(1, 1)));

    /** A square 3x3 shape. */
    public static final Shape SQUARE_3X3 = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0),
            new Position(2, 0),
            new Position(0, 1),
            new Position(1, 1),
            new Position(2, 1),
            new Position(0, 2),
            new Position(1, 2),
            new Position(2, 2)));

    /** A reversed T-shaped configuration. */
    public static final Shape T_SHAPE_REVERSED = new Shape(List.of(
            new Position(0, 0),
            new Position(1, 0),
            new Position(2, 0),
            new Position(1, 1)));

    /** A cross-shaped (plus sign) configuration. */
    public static final Shape CROSS = new Shape(List.of(
            new Position(1, 0),
            new Position(0, 1),
            new Position(1, 1),
            new Position(2, 1),
            new Position(1, 2)));

    /**
     * Returns the absolute positions of the shape based on an anchor position.
     * In other words, it translates the positions of the shape to be relative to
     * the anchor so that we have the actual occupied positions in the backpack for
     * example.
     * 
     * @param anchor the anchor position to translate the shape's positions
     * @return a set of absolute positions based on the anchor position
     */
    public Set<Position> getAbsolutePositions(Position anchor) {
        Objects.requireNonNull(anchor, "anchor cannot be null");
        var result = new HashSet<Position>();
        for (var cell : cells) {
            result.add(new Position(anchor.x() + cell.x(), anchor.y() + cell.y()));
        }
        return result;
    }

    /**
     * Normalizes a list of positions to create a Shape.
     * The normalization process shifts the positions so that the minimum x and y
     * coordinates are at (0, 0).
     * It can be useful after rotating a shape to ensure the shape is properly
     * aligned.
     * 
     * @param positions the list of positions to normalize
     * @return a new Shape with normalized positions
     */
    private static Shape normalize(List<Position> positions) {
        Objects.requireNonNull(positions, "positions cannot be null");
        if (positions.isEmpty()) {
            throw new IllegalArgumentException("positions cannot be empty");
        }
        int minX = positions.stream()
                .mapToInt(Position::x)
                .min()
                .orElse(0);
        int minY = positions.stream()
                .mapToInt(Position::y)
                .min()
                .orElse(0);
        var normalized = positions.stream()
                .map(p -> new Position(p.x() - minX, p.y() - minY))
                .toList();
        return new Shape(normalized);
    }

    /**
     * Rotates the shape 90 degrees clockwise.
     * 
     * @return a new Shape rotated 90 degrees clockwise
     */
    public Shape rotate90() {
        var rotated = cells.stream()
                .map(p -> new Position(p.y(), -p.x()))
                .toList();
        return normalize(rotated);
    }

    /**
     * Rotates the shape 180 degrees clockwise.
     * 
     * @return a new Shape rotated 180 degrees clockwise
     */
    public Shape rotate180() {
        return rotate90().rotate90();
    }

    /**
     * Rotates the shape 270 degrees clockwise.
     * 
     * @return a new Shape rotated 270 degrees clockwise
     */
    public Shape rotate270() {
        return rotate90().rotate90().rotate90();
    }

    /**
     * Returns the width of the shape.
     * 
     * @return the width of the shape
     */
    public int getWidth() {
        return cells.stream()
                .mapToInt(Position::x)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Returns the height of the shape.
     * 
     * @return the height of the shape
     */
    public int getHeight() {
        return cells.stream()
                .mapToInt(Position::y)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Returns a string representation of the shape.
     * 
     * @return a string representation of the shape
     */
    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
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
