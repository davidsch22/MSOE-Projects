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
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The AutoCompleter strategy that uses an AVL tree
 */
public class AVLTreeCompleter implements AutoCompleter {
    private AVLTree<String> dictionary;
    private long startTime;
    private long endTime;

    /**
     * Constructor that copies the dictionary from the previously used strategy
     * @param copyList The copied dictionary List
     */
    public AVLTreeCompleter(List<String> copyList) {
        startTime = System.nanoTime();
        dictionary = new AVLTree<>();

        for (String word : copyList) {
            dictionary.add(word);
        }

        //TreePrinter.print(dictionary.root);

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

        dictionary = new AVLTree<>();

        while (in.hasNextLine()) {
            if (filename.contains(".txt")) {
                dictionary.add(in.nextLine());
            } else if (filename.contains(".csv")) {
                String line = in.nextLine();
                if (!line.contains(",")) {
                    throw new IndexOutOfBoundsException();
                } else {
                    dictionary.add(line.substring(line.indexOf(",") + 1));
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
            results = allThatBeginWith(dictionary.root, prefix);
        }

        endTime = System.nanoTime();

        return results;
    }

    private List<String> allThatBeginWith(AVLNode current, String prefix) {
        List<String> results = new ArrayList<>();

        if (current != null) {
            String currentWord = current.data.toString();
            boolean longEnough = currentWord.length() >= prefix.length();

            if (longEnough && currentWord.substring(0, prefix.length()).equals(prefix)) {
                results.add(current.data.toString());
            }

            if (prefix.compareTo(currentWord) < 0) {
                results.addAll(allThatBeginWith(current.left, prefix));
                results.addAll(allThatBeginWith(current.right, prefix));
            } else if (prefix.compareTo(currentWord) > 0) {
                results.addAll(allThatBeginWith(current.right, prefix));
            }
        }

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
        return dictionary.getAllWordsInOrder();
    }
}
