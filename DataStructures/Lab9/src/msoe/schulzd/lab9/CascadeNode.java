/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 9 - Autocomplete Revisited
 * Name: David Schulz
 * Created: 5/9/19
 */

package msoe.schulzd.lab9;

import java.util.ArrayList;
import java.util.List;

/**
 * The node class for the Cascading ArrayLists strategy
 */
public class CascadeNode {
    private String character;
    private String word;
    private List<CascadeNode> nextChars;

    /**
     * The constructor for the first layer of ArrayList
     * @param letter The letter assigned to character
     */
    public CascadeNode(String letter) {
        character = letter;
        word = null;
        nextChars = new ArrayList<>();
    }

    /**
     * The constructor for the rest of the layers
     * @param fullWord The full word that is being created
     * @param wordLeft The characters that have yet to be added as layers
     */
    public CascadeNode(String fullWord, String wordLeft) {
        character = wordLeft.substring(0, 1);
        nextChars = new ArrayList<>();
        if (wordLeft.length() == 1) {
            word = fullWord;
        } else {
            word = null;
            nextChars.add(new CascadeNode(fullWord, wordLeft.substring(1)));
        }
    }

    /**
     * Used to start adding a whole word
     * @param word Word to be added
     */
    public void addWord(String word) {
        nextChars.add(new CascadeNode(word, word.substring(1)));
    }

    /**
     * Searches all words that the prefix fits in
     * @param prefix Part of the word the results need to match
     * @return A List of all words that were found in the search
     */
    public List<String> allThatBeginWith(String prefix) {
        List<String> results = new ArrayList<>();

        if (prefix.length() == 1) {
            results.addAll(getAllWords());
        } else {
            prefix = prefix.substring(1);
            for (CascadeNode node : nextChars) {
                if (node.getCharacter().equals(prefix.substring(0, 1))) {
                    results.addAll(node.allThatBeginWith(prefix));
                }
            }
        }

        return results;
    }

    /**
     * Gets all of the possible words from this node and its children
     * @return All words found
     */
    public List<String> getAllWords() {
        List<String> allWords = new ArrayList<>();

        if (word != null) {
            allWords.add(word);
        }
        for (CascadeNode node : nextChars) {
            allWords.addAll(node.getAllWords());
        }

        return allWords;
    }

    public String getCharacter() {
        return character;
    }
}
