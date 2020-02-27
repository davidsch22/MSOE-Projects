/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

import java.util.Scanner;

/**
 * Interface to calculator, taking commands (see the following code) and converting them
 * to calculator operations.
 *
 * @author R. Hasker
 * @date Feb. 2018
 */
public class Main {

    /**
     * Display user-visible status of calculator. Shows stored memory value if it
     * is not 0.
     *
     * @param c calculator to display
     */
    public static void show(Calculator c) {
        String display = "Display: " + c.result();
        if (!c.getMemory().equals("0"))
            display += " [Mem: " + c.getMemory() + "]";
        System.out.println(display);
    }

    /**
     * Main loop driving calculator: turns input into calculator commands
     * This procedure is long because it encapsulates a large case statement.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\\s*"); // treat whitespace as a word separator
        Calculator calculator = new Calculator();
        History history = new History();

        boolean doContinue = true;
        while (doContinue && in.hasNext()) {
            String text = in.next();
            for (char c : text.toCharArray()) {
                if (Character.isDigit(c))
                    history.doCommand(new AppendDigitCommand(calculator, c));
                else {
                    switch (Character.toLowerCase(c)) {
                        case 'q':
                            System.out.println("Quit");
                            doContinue = false;
                            break;
                        case '+':
                            System.out.println("Add");
                            history.doCommand(new PlusCommand(calculator));
                            show(calculator);
                            break;
                        case '-':
                            System.out.println("Subtract");
                            history.doCommand(new MinusCommand(calculator));
                            show(calculator);
                            break;
                        case '*':
                            System.out.println("Multiply");
                            history.doCommand(new TimesCommand(calculator));
                            show(calculator);
                            break;
                        case ',':
                            history.doCommand(new EnterCommand(calculator));
                            show(calculator);
                            break;
                        case 's':
                            System.out.println("Save");
                            history.doCommand(new SaveCommand(calculator));
                            show(calculator);
                            break;
                        case 'r':
                            System.out.println("Recall");
                            history.doCommand(new RecallCommand(calculator));
                            show(calculator);
                            break;
                        case 'c':
                            System.out.println("Clear");
                            history.doCommand(new ClearCommand(calculator));
                            show(calculator);
                            break;
                        case 'u':
                            System.out.println("Undo");
                            if (history.nextToUndo() == null) break;
                            history.undo();
                            show(calculator);
                            break;
                        case 'd':
                            System.out.println("Redo");
                            if (history.nextToRedo() == null) break;
                            history.redo();
                            show(calculator);
                            break;
                        default:
                            if (!Character.isISOControl(c))
                                System.out.println("Unrecognized command `" + c + "'");
                    }
                }
            }
        }
        // end of loop over input
        System.out.println("Final result: " + calculator.result());
    }
}
