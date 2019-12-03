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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Runner class
 */
public class WordSearchCLI {
    private static AutoCompleter strategy;
    private static GameBoard gameBoard;
    private static List<String> wordsFound;

    public static void main(String[] args) {
        String file1 = args[0];
        File gridFile = new File(file1);
        String wordsFile = args[1];
        String strat = args[2];

        if (strat.equals("SortedArrayList")) {
            strategy = new SortedAutoCompleter(new SortedArrayList<>());
        } else if (strat.equals("ArrayListIndexed")) {
            strategy = new IndexedAutoCompleter(new ArrayList<>());
        } else if (strat.equals("LinkedListIndexed")) {
            strategy = new IndexedAutoCompleter(new LinkedList<>());
        } else if (strat.equals("ArrayListIterated")) {
            strategy = new IteratedAutoCompleter(new ArrayList<>());
        } else if (strat.equals("LinkedListIterated")) {
            strategy = new IteratedAutoCompleter(new LinkedList<>());
        } else {
            System.out.println("ERROR: Invalid strategy name");
            return;
        }

        try {
            strategy.initialize(wordsFile);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Words file not found");
            return;
        }

        gameBoard = new GameBoard(strategy);
        try {
            gameBoard.load(gridFile.toPath());
        } catch (IOException e) {
            System.out.println("ERROR: Grid file not found");
            return;
        }

        wordsFound = gameBoard.findWords();
        for (String word : wordsFound) {
            System.out.println(word);
        }
        System.out.println("Number of Words Found: " + wordsFound.size());
        System.out.println("Time Required to Complete Search: " + gameBoard.getLastSearchTime());
    }
}
