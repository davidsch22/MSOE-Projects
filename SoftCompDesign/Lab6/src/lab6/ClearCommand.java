/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Performs or reverses clearing the calculator.
 */
public class ClearCommand extends CalculatorCommand {
    private String prevDisplay;
    private String prevAccumulator;
    private String prevMemory;
    private boolean prevNewNumber;

    public ClearCommand(Calculator c) {
        super(c);
    }

    public void execute() {
        prevDisplay = calculator.getDisplay();
        prevAccumulator = calculator.getAccumulator();
        prevMemory = calculator.getMemory();
        prevNewNumber = calculator.getNewNumber();
        calculator.clear();
    }

    public void unexecute() {
        calculator.setDisplay(prevDisplay);
        calculator.setAccumulator(prevAccumulator);
        calculator.setMemory(prevMemory);
        calculator.setReadyForNewNumber(prevNewNumber);
    }
}
