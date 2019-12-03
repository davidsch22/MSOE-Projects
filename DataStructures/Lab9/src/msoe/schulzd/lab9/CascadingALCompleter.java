/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 9 - Autocomplete Revisited
 * Name: David Schulz
 * Created: 5/9/19
 */

package msoe.schulzd.lab9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The AutoCompleter strategy that uses layers of nodes containing characters and ArrayLists
 */
public class CascadingALCompleter implements AutoCompleter {
    private List<CascadeNode> dictionary;
    private long startTime;
    private long endTime;

    /**
     * Initializes this strategy without the need to reload the original file
     * by copying the dictionary from the previously used strategy
     * @param copyList The dictionary from the previous strategy
     */
    public CascadingALCompleter(List<String> copyList) {
        startTime = System.nanoTime();
        dictionary = new ArrayList<>();

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabet.length(); i++) {
            String letter = alphabet.substring(i, i+1);
            dictionary.add(new CascadeNode(letter));
        }

        for (String nextWord : copyList) {
            for (CascadeNode node : dictionary) {
                if (node.getCharacter().equals(nextWord.substring(0, 1))) {
                    node.addWord(nextWord);
                }
            }
        }

        endTime = System.nanoTime();
    }

    /**
     * Reads the given file and fills the List with what's read
     * @param filename The file name/path
     * @throws FileNotFoundException Thrown if file is not found
     */
    @Override
    public void initialize(String filename) throws FileNotFoundException {
        startTime = System.nanoTime();

        Scanner in = new Scanner(new File(filename));

        dictionary = new ArrayList<>();

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabet.length(); i++) {
            String letter = alphabet.substring(i, i+1);
            dictionary.add(new CascadeNode(letter));
        }

        while (in.hasNextLine()) {
            String nextWord;
            if (filename.contains(".txt")) {
                nextWord = in.nextLine();
            } else if (filename.contains(".csv")) {
                String line = in.nextLine();
                if (!line.contains(",")) {
                    throw new IndexOutOfBoundsException();
                } else {
                    nextWord = line.substring(line.indexOf(",") + 1);
                }
            } else {
                throw new IllegalArgumentException();
            }

            for (CascadeNode node : dictionary) {
                if (node.getCharacter().equals(nextWord.substring(0, 1))) {
                    node.addWord(nextWord);
                }
            }
        }

        in.close();

        endTime = System.nanoTime();
    }

    /**
     * Searches all words that the prefix fits in
     * @param prefix Part of the word the results need to match
     * @return A List of all words that were found in the search
     */
    @Override
    public List<String> allThatBeginWith(String prefix) {
        startTime = System.nanoTime();

        List<String> results = new ArrayList<>();

        if (!prefix.equals("")) {
            for (CascadeNode node : dictionary) {
                if (node.getCharacter().equals(prefix.substring(0, 1))) {
                    results = node.allThatBeginWith(prefix);
                }
            }
        }

        endTime = System.nanoTime();

        return results;
    }

    /**
     * Calculates the time it took for the last operation to complete
     * @return The time (in nanoseconds)
     */
    @Override
    public long getLastOperationTime() {
        return endTime - startTime;
    }

    /**
     * Gets the list of words being used to search
     * @return List of words
     */
    @Override
    public List<String> getDictionary() {
        List<String> allWords = new ArrayList<>();

        for (CascadeNode node : dictionary) {
            allWords.addAll(node.getAllWords());
        }

        return allWords;
    }
}
