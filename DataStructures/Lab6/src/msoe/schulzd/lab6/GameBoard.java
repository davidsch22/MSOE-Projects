/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 6 - Recursion
 * Name: David Schulz
 * Created: 4/11/19
 */

package msoe.schulzd.lab6;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * GameBoard class
 */
public class GameBoard {
    private String[][] grid;
    private AutoCompleter strategy;
    private long startTime;
    private long endTime;

    /**
     * GameBoard constructor
     * @param strategy List/Search strategy to use
     * @throws IllegalArgumentException thrown if given an invalid strategy
     */
    public GameBoard(AutoCompleter strategy) throws IllegalArgumentException {
        this.strategy = strategy;
    }

    /**
     * Reads the grid from a file and creates a matrix from it
     * @param path Path to given file
     * @throws IOException thrown if file can't be found
     */
    public void load(Path path) throws IOException {
        Scanner in = new Scanner(path);
        ArrayList<String> lines = new ArrayList<>();

        while (in.hasNextLine()) {
            lines.add(in.next());
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        grid = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = lines.get(i).substring(j, j+1);
            }
        }
    }

    /**
     * Finds all of the words that can be made from the grid
     * @return The List of all found words
     */
    public List<String> findWords() {
        startTime = System.nanoTime();

        System.out.print("Searching for words");

        List<String> wordsFound = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(".");
                wordsFound.addAll(recursiveSearch(i, j,
                        new boolean[grid.length][grid[0].length], ""));
            }
        }

        System.out.println("Done!");
        endTime = System.nanoTime();

        return wordsFound;
    }

    private List<String> recursiveSearch(int row, int col, boolean[][] visitedFlags,
                                         String partialWord) {
        List<String> wordsFound = new ArrayList<>();

        partialWord += grid[row][col].toLowerCase();
        visitedFlags[row][col] = true;

        final int maxLength = 15;
        if (partialWord.length() > maxLength) {
            return wordsFound;
        }

        List<String> matches = strategy.allThatBeginWith(partialWord);

        if (matches.isEmpty()) {
            return wordsFound;
        }

        if (strategy.contains(partialWord) && partialWord.length() >= 3) {
            wordsFound.add(partialWord);
        }

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && j >= 0 && i < grid.length && j < grid[0].length
                        && !visitedFlags[i][j]) {
                    wordsFound.addAll(recursiveSearch(i, j, copyFlagMatrix(visitedFlags),
                            partialWord));
                }
            }
        }
        return wordsFound;
    }

    /**
     * Calculates and formats the time it took to perform a word search
     * @return The time as a String
     */
    public String getLastSearchTime() {
        long time = endTime - startTime;
        String timeFormatted;

        final int nanosInMicros = 1000;
        final int nanosInMillis = 1000000;
        final int nanosInSeconds = 1000000000;

        if (time < nanosInMicros) {
            timeFormatted = time + " nanoseconds";
        } else if (time < nanosInMillis) {
            timeFormatted = (time / nanosInMicros) + " microseconds";
        } else if (time < nanosInSeconds) {
            timeFormatted = (time / nanosInMillis) + " milliseconds";
        } else {
            Date date = new Date(time / nanosInMillis);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            timeFormatted = formatter.format(date);
        }

        return timeFormatted;
    }

    private boolean[][] copyFlagMatrix(boolean[][] original) {
        boolean[][] copy = new boolean[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }
}
