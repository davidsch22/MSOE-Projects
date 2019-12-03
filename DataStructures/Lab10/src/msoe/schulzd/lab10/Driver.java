/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 10
 * Name: David Schulz
 * Created: 5/16/19
 */

package msoe.schulzd.lab10;

import javax.swing.JFrame;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        /* PROBLEM 1 */
        System.out.println("Problem 1");

        FileDialog fd = new FileDialog(new JFrame(), "Choose a text file", FileDialog.LOAD);
        fd.setFile("*.txt");
        fd.setVisible(true);

        String filename = fd.getDirectory();
        filename += fd.getFile();

        if (filename.contains(".txt")) {
            File file = new File(filename);
            Scanner in;

            try {
                in = new Scanner(file);
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: File could not be found");
                return;
            }

            List<CloudNode> words = new SortedArrayList<>();
            while (in.hasNext()) {
                String nextWord = in.next();
                boolean foundWord = false;

                for (CloudNode node : words) {
                    if (nextWord.compareTo(node.getWord()) < 0) {
                        break;
                    }
                    if (nextWord.equals(node.getWord())) {
                        node.addCount();
                        foundWord = true;
                    }
                }
                if (!foundWord) {
                    words.add(new CloudNode(nextWord));
                }
            }
            in.close();

            for (CloudNode node : words) {
                System.out.println(node.getCount() + " " + node.getWord());
            }
        }

        System.exit(0);
    }
}
