package fr.uge.backpackhero.model.score;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Manages the Hall of Fame, storing the top 3 scores.
 * Uses java.nio for file operations as required by the project specifications.
 * 
 * @author Yacine
 */
public class HOF {

    private static final int MAX_ENTRIES = 3;
    private static final Path SAVE_FILE = Path.of("halloffame.txt");

    private final List<ScoreEntry> entries;

    /**
     * Creates a new HallOfFame and loads existing scores from file.
     */
    public HOF() {
        this.entries = new ArrayList<>();
        loadScores();
    }

    /**
     * Adds a new score entry to the Hall of Fame.
     * Only keeps the top MAX_ENTRIES scores.
     * 
     * @param playerName the name of the player
     * @param score      the score achieved
     * @param level      the level reached
     * @return true if the score made it into the top 3, false otherwise
     */
    public boolean addScore(String playerName, int score, int level) {
        Objects.requireNonNull(playerName, "Player name cannot be null");

        ScoreEntry newEntry = new ScoreEntry(playerName, score, level);
        entries.add(newEntry);
        Collections.sort(entries); // Sort by score descending

        boolean isInHOF = entries.indexOf(newEntry) < MAX_ENTRIES;
        if (entries.size() > MAX_ENTRIES) {
            entries.subList(MAX_ENTRIES, entries.size()).clear(); // Remove scores below MAX_ENTRIES
        }

        saveScores();
        return isInHOF;
    }

    /**
     * Submits a score and displays the result to the console.
     * 
     * @param playerName the name of the player
     * @param score      the score achieved
     * @param level      the level reached
     */
    public void submitScore(String playerName, int score, int level) {
        boolean madeIt = addScore(playerName, score, level);

        if (madeIt) {
            System.out.println("Congratulations! You made it to the Hall of Fame!");
            System.out.println("Score: " + score + " (Level " + level + ")");
        } else {
            System.out.println("Final Score: " + score + " (Level " + level + ")");
            System.out.println("Minimum score for Hall of Fame: " + getMinimumScore());
        }
        System.out.println("\n" + this.toString());
    }

    /**
     * Returns an unmodifiable list of the top scores.
     * 
     * @return the list of score entries
     */
    public List<ScoreEntry> getTopScores() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Checks if a score would make it into the Hall of Fame.
     * 
     * @param score the score to check
     * @return true if the score would be in top 3
     */
    public boolean wouldMakeHallOfFame(int score) {
        if (entries.size() < MAX_ENTRIES) {
            return true;
        }
        return score > getMinimumScore();
    }

    /**
     * Returns the minimum score currently in the Hall of Fame.
     * Returns 0 if the Hall of Fame is not full.
     * 
     * @return the minimum score in the Hall of Fame
     */
    public int getMinimumScore() {
        if (entries.size() < MAX_ENTRIES) {
            return 0;
        }
        return entries.get(MAX_ENTRIES - 1).score();
    }

    /**
     * Loads scores from the save file using java.nio.
     */
    private void loadScores() {
        if (!Files.exists(SAVE_FILE)) {
            return; // File doesn't exist yet, start with empty list
        }

        try {
            List<String> lines = Files.readAllLines(SAVE_FILE);
            entries.clear();

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    ScoreEntry entry = ScoreEntry.fromSaveString(line);
                    entries.add(entry);
                } catch (IllegalArgumentException e) { // In case of a malformed line that would crash with the
                                                       // fromSaveString call
                    System.err.println("Skipping invalid score entry: " + line);
                }
            }

            Collections.sort(entries);

            // Ensure we only keep top MAX_ENTRIES
            if (entries.size() > MAX_ENTRIES) {
                entries.subList(MAX_ENTRIES, entries.size()).clear();
            }

        } catch (IOException e) {
            System.err.println("Failed to load Hall of Fame: " + e.getMessage());
        }
    }

    /**
     * Saves scores to the save file.
     */
    private void saveScores() {
        try {
            List<String> lines = entries.stream()
                    .map(ScoreEntry::toSaveString)
                    .collect(Collectors.toList());

            Files.write(SAVE_FILE, lines,
                    StandardOpenOption.CREATE, // Create file if it doesn't exist
                    StandardOpenOption.TRUNCATE_EXISTING); // Overwrite existing file

        } catch (IOException e) {
            System.err.println("Failed to save Hall of Fame: " + e.getMessage());
        }
    }

    /**
     * Clears all scores from the Hall of Fame.
     */
    public void clearAll() {
        entries.clear();
        saveScores();
    }

    @Override
    public String toString() {
        if (entries.isEmpty()) {
            return "Hall of Fame is empty.";
        }

        StringBuilder sb = new StringBuilder("HALL OF FAME\n");
        for (int i = 0; i < entries.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, entries.get(i)));
        }
        return sb.toString();
    }
}
