/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Performs or reverses a multiplication operation on the calculator.
 */
public class TimesCommand extends CalculatorCommand {
    private String prevDisplay;
    private String prevAccumulator;
    private boolean prevNewNumber;

    public TimesCommand(Calculator c) {
        super(c);
    }

    public void execute() {
        prevDisplay = calculator.getDisplay();
        prevAccumulator = calculator.getAccumulator();
        prevNewNumber = calculator.getNewNumber();
        calculator.times();
    }

    public void unexecute() {
        calculator.setDisplay(prevDisplay);
        calculator.setAccumulator(prevAccumulator);
        calculator.setReadyForNewNumber(prevNewNumber);
    }
}
