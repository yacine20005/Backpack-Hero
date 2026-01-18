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
    private final Path saveFile;

    private final List<ScoreEntry> entries;

    /**
     * Creates a new HallOfFame and loads existing scores from file.
     * 
     * @param saveFile the path to the file where scores are saved
     * @throws IOException if an I/O error occurs loading the scores
     */
    public HOF(Path saveFile) throws IOException {
        this.saveFile = Objects.requireNonNull(saveFile);
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
     * @throws IOException if an I/O error occurs saving the scores
     */
    public boolean addScore(String playerName, int score, int level) throws IOException {
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
     * @throws IOException if an I/O error occurs saving the scores
     */
    public void submitScore(String playerName, int score, int level) throws IOException {
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
     * 
     * @throws IOException if an I/O error occurs reading from the file
     */
    private void loadScores() throws IOException {
        if (!Files.exists(saveFile)) {
            return; // File doesn't exist yet, start with empty list
        }

        List<String> lines = Files.readAllLines(saveFile);
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
    }

    /**
     * Saves scores to the save file.
     * 
     * @throws IOException if an I/O error occurs writing to the file
     */
    private void saveScores() throws IOException {
        List<String> lines = entries.stream()
                .map(ScoreEntry::toSaveString)
                .collect(Collectors.toList());

        Files.write(saveFile, lines,
                StandardOpenOption.CREATE, // Create file if it doesn't exist
                StandardOpenOption.TRUNCATE_EXISTING); // Overwrite existing file
    }

    /**
     * Clears all scores from the Hall of Fame.
     * 
     * @throws IOException if an I/O error occurs saving the scores
     */
    public void clearAll() throws IOException {
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
