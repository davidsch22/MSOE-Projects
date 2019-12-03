/*
 * Course: CS2852
 * Spring 2019
 * Lab 7 - Morse Code Decoder
 * Name: David Schulz
 * Created: 4/25/19
 */

package msoe.schulzd.lab7;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Runner class
 */
public class MorseDecoder {
    private static MorseTree morseTree;

    public static void main(String[] args) {
        morseTree = new MorseTree();

        try {
            loadDecoder(new File("morsecode.txt").toPath());
        } catch (IOException e) {
            System.out.println("ERROR: Morse code file could not be found");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Morse code file could not be loaded");
            return;
        }

        TreePrinter.print(morseTree.getRoot());

        Scanner in = new Scanner(System.in);
        System.out.print("Enter an input filename: ");
        File input = new File(in.nextLine());
        System.out.print("Enter an output filename: ");
        File output = new File(in.nextLine());

        try {
            decodeFile(input.toPath(), output);
        } catch (IOException e) {
            System.out.println("ERROR: Input/Output file could not be found");
            return;
        }

        System.out.println("Decoding complete!");
    }

    private static void loadDecoder(Path path) throws IOException, IllegalArgumentException {
        Scanner in = new Scanner(path);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String symbol;
            String code;

            if (line.contains("\\n")) {
                symbol = line.substring(0, 2);
                if (line.indexOf(" ") > 1) {
                    code = line.substring(2, line.indexOf(" "));
                } else {
                    code = line.substring(2);
                }
            } else {
                symbol = line.substring(0, 1);
                if (line.indexOf(" ") > 1) {
                    code = line.substring(1, line.indexOf(" "));
                } else {
                    code = line.substring(1);
                }
            }

            morseTree.add(symbol, code);
        }

        in.close();
    }

    private static void decodeFile(Path inPath, File outFile) throws IOException {
        Scanner in = new Scanner(inPath);
        PrintWriter out = new PrintWriter(outFile);

        while (in.hasNext()) {
            String readCode = in.next();
            String decoded = (String)morseTree.decode(readCode);
            if (decoded != null) {
                if (decoded.equals("\\n")) {
                    out.println();
                } else {
                    out.print(decoded);
                }
            }
        }

        in.close();
        out.close();
    }
}
