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
import java.util.List;
import java.util.Scanner;

/**
 * Class used when searching a SortedArrayList
 */
public class SortedAutoCompleter implements AutoCompleter {
    private SortedArrayList<String> dictionary;
    private long startTime;
    private long endTime;

    /**
     * Sorted constructor
     * @param emptyList The SortedArrayList that will be filled and used
     */
    public SortedAutoCompleter(SortedArrayList<String> emptyList) {
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

        List<String> results = new SortedArrayList<>();

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
            return dictionary.contains(target);
        }
        return false;
    }

    public long getLastOperationTime() {
        return endTime - startTime;
    }
}
