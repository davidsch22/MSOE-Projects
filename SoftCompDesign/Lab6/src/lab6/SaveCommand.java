/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Performs or reverses a save to memory operation on the calculator.
 */
public class SaveCommand extends CalculatorCommand {
    private String prevMemory;
    private String prevAccumulator;
    private boolean prevNewNumber;

    public SaveCommand(Calculator c) {
        super(c);
    }

    public void execute() {
        prevMemory = calculator.getMemory();
        prevAccumulator = calculator.getAccumulator();
        prevNewNumber = calculator.getNewNumber();
        calculator.saveToMemory();
    }

    public void unexecute() {
        calculator.setMemory(prevMemory);
        calculator.setAccumulator(prevAccumulator);
        calculator.setReadyForNewNumber(prevNewNumber);
    }
}
