/*
 * Course: CS2852
 * Spring 2019
 * Lab 8 - Morse Code Encoder
 * Name: David Schulz
 * Created: 4/30/19
 */

package msoe.schulzd.lab8;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Runner class
 */
public class MorseEncoder {
    private static LookupTable<String, String> table;

    public static void main(String[] args) {
        table = new LookupTable<>();

        try {
            loadEncoder(new File("morsecode.txt").toPath());
        } catch (IOException e) {
            System.out.println("ERROR: Morse code file could not be found");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Morse code file could not be loaded");
            return;
        }

        Scanner in = new Scanner(System.in);

        System.out.print("Enter an input filename: ");
        File input = new File(in.nextLine());
        System.out.print("Enter an output filename: ");
        File output = new File(in.nextLine());

        in.close();

        try {
            encodeFile(input.toPath(), output);
        } catch (IOException e) {
            System.out.println("ERROR: Input/Output file could not be found");
            return;
        }

        System.out.println("Encoding complete!");
    }

    private static void loadEncoder(Path path) throws IOException, IllegalArgumentException {
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

            table.put(symbol, code);
        }

        in.close();
    }

    private static void encodeFile(Path inPath, File outFile) throws IOException {
        Scanner in = new Scanner(inPath);
        PrintWriter out = new PrintWriter(outFile);

        while (in.hasNextLine()) {
            String line = in.nextLine().toUpperCase();
            for (int i = 0; i < line.length(); i++) {
                char nextChar = line.charAt(i);
                String morse = table.get(String.valueOf(nextChar));
                if (morse != null) {
                    out.print(morse + " ");
                } else {
                    System.out.println("Warning: Skipping " + nextChar);
                }
            }
            out.println(table.get("\\n"));
        }

        in.close();
        out.close();
    }
}
