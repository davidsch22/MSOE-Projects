/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 6 - Recursion
 * Name: David Schulz
 * Created: 4/11/19
 */

package msoe.schulzd.lab6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used when searching with the index algorithm
 */
public class IndexedAutoCompleter implements AutoCompleter {
    private List<String> dictionary;
    private long startTime;
    private long endTime;

    /**
     * Indexed constructor
     * @param emptyList An empty List that determines ArrayList or LinkedList
     */
    public IndexedAutoCompleter(List<String> emptyList) {
        dictionary = emptyList;
    }

    /**
     * Reads the given file and fills the List with what's read
     * @param filename The file name/path
     * @throws FileNotFoundException Thrown if file is not found
     * @throws IndexOutOfBoundsException Thrown if csv file is not correct
     */
    public void initialize(String filename) throws FileNotFoundException,
            IndexOutOfBoundsException {
        Scanner in = new Scanner(new File(filename));

        if (dictionary instanceof ArrayList) {
            dictionary = new ArrayList<>();
        } else {
            dictionary = new LinkedList<>();
        }

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
    }

    /**
     * Searches all words that the prefix fits in
     * @param prefix Part of the word the results need to match
     * @return A List of all words that were found in the search
     */
    public List<String> allThatBeginWith(String prefix) {
        startTime = System.nanoTime();

        List<String> results;
        if (dictionary instanceof ArrayList) {
            results = new ArrayList<>();
        } else {
            results = new LinkedList<>();
        }

        if (!prefix.equals("")) {
            for (int i = 0; i < dictionary.size(); i++) {
                String word = dictionary.get(i);
                if (word.length() >= prefix.length() && word.substring(0,
                        prefix.length()).equals(prefix)) {
                    results.add(word);
                }
            }
        }

        endTime = System.nanoTime();

        return results;
    }

    /**
     * Checks if target is contained in the AutoCompleter object
     * @param target The target to check
     * @return Whether target is contained
     * @throws IllegalStateException if initialize() has not yet been called
     */
    public boolean contains(String target) throws IllegalStateException {
        if (!target.equals("")) {
            for (int i = 0; i < dictionary.size(); i++) {
                if (target.equals(dictionary.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public long getLastOperationTime() {
        return endTime - startTime;
    }
}
