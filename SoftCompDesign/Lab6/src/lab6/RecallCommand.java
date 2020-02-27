/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Performs or reverses a recall from memory on the calculator.
 */
public class RecallCommand extends CalculatorCommand {
    private String prevDisplay;
    private boolean prevNewNumber;

    public RecallCommand(Calculator c) {
        super(c);
    }

    public void execute() {
        prevDisplay = calculator.getDisplay();
        prevNewNumber = calculator.getNewNumber();
        calculator.recallFromMemory();
    }

    public void unexecute() {
        calculator.setDisplay(prevDisplay);
        calculator.setReadyForNewNumber(prevNewNumber);
    }
}
