/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Performs or reverses adding digits to a currently existing number, if one exists in the front, or
 * begins creating a new number if one is not present.
 */
public class AppendDigitCommand extends CalculatorCommand {
    private char digit;
    private String prevDisplay;
    private boolean prevNewNumber;

    public AppendDigitCommand(Calculator c, char digit) {
        super(c);
        this.digit = digit;
    }

    public void execute() {
        prevDisplay = calculator.getDisplay();
        prevNewNumber = calculator.getNewNumber();
        calculator.appendDigit(digit);
    }

    public void unexecute() {
        calculator.setDisplay(prevDisplay);
        calculator.setReadyForNewNumber(prevNewNumber);
    }
}
