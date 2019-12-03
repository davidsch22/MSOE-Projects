/*
 * Course: CS1021
 * Winter 2018
 * Lab: Lab2
 * Name: David Schulz
 * Created: 12/6/18
 */

package schulzd;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that holds the ArrayList of references
 * @author David Schulz
 */
public class ReferenceHolder {
    private ArrayList<Reference> references;

    /**
     * The ReferenceHolder constructor
     */
    public ReferenceHolder() {
        references = new ArrayList<>();
    }

    /**
     * Adds a book reference to the references ArrayList
     * @param book The book object to be added
     */
    public void addReference(Book book) {
        references.add(book);
    }

    /**
     * Adds an article reference to the references ArrayList
     * @param article The article object to be added
     */
    public void addReference(Article article) {
        references.add(article);
    }

    /**
     * Finds the desired reference in the ArrayList and prompts it for an update
     * @param uniqueID The unique ID of the desired reference
     * @param in The Scanner class used for user input
     */
    public void updateReference(String uniqueID, Scanner in) {
        for (Reference r : references) {
            if (r.getMyUniqueID().equalsIgnoreCase(uniqueID)) {
                r.promptForUpdate(in);
            }
        }
    }

    /**
     * Get the entire list of references in the proper BibTeX format
     * @return The string containing the whole list
     */
    public String toString() {
        String result = "";
        for (Reference r : references) {
            result += r.toString();
        }
        return result;
    }
}
