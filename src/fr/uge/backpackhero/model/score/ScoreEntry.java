package fr.uge.backpackhero.model.score;

import java.util.Objects;

/**
 * Represents a score entry in the Hall of Fame.
 * Each entry contains a player name, score, and level reached.
 * 
 * @author Yacine
 */
public record ScoreEntry(String playerName, int score, int level)
        implements Comparable<ScoreEntry> {

    /**
     * Creates a new ScoreEntry.
     * 
     * @param playerName the name of the player
     * @param score      the score achieved
     * @param level      the level reached
     */
    public ScoreEntry {
        Objects.requireNonNull(playerName, "Player name cannot be null");
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        if (level < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }
    }

    /**
     * Compares this entry with another by score (descending order).
     * Higher scores come first.
     * 
     * @param other the other score entry to compare to
     * @return negative if this score is higher, positive if lower, 0 if equal
     */
    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(other.score, this.score); // Make comparing reversed
    }

    /**
     * Converts this entry to a saveable string format.
     * Format: playerName|score|level
     * 
     * @return a string representation for saving
     */
    public String toSaveString() {
        return String.format("%s|%d|%d", playerName, score, level);
    }

    /**
     * Creates a ScoreEntry from a saved string.
     * 
     * @param line the saved string
     * @return a ScoreEntry instance
     * @throws IllegalArgumentException if the format is invalid
     */
    public static ScoreEntry fromSaveString(String line) {
        Objects.requireNonNull(line, "Line cannot be null");
        String[] parts = line.split("\\|"); // to escape the pipe character created in the toSaveString method
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid score entry format: " + line);
        }

        try {
            String name = parts[0];
            int score = Integer.parseInt(parts[1]);
            int level = Integer.parseInt(parts[2]);

            return new ScoreEntry(name, score, level);
        } catch (NumberFormatException e) { // In case score or level are not valid integers
            throw new IllegalArgumentException("Failed to parse score entry: " + line, e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s - Score: %d, Level: %d", playerName, score, level);
    }
}
