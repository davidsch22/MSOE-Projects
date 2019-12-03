/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 4 - Auto Complete
 * Name: David Schulz
 * Created: 3/27/19
 */

package msoe.schulzd.lab4;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Interface for the auto completer
 */
public interface AutoCompleter {
    /**
     * Reads the given file and fills the List with what's read
     * @param filename The file name/path
     * @throws FileNotFoundException Thrown if file is not found
     */
    void initialize(String filename) throws FileNotFoundException;

    /**
     * Searches all words that the prefix fits in
     * @param prefix Part of the word the results need to match
     * @return A List of all words that were found in the search
     */
    List<String> allThatBeginWith(String prefix);

    /**
     * Calculates the time it took for the last operation to complete
     * @return The time (in nanoseconds)
     */
    long getLastOperationTime();
}
