/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Performs or reverses the enter command for the calculator.
 */
public class EnterCommand extends CalculatorCommand {
    private String prevAccumulator;
    private boolean prevNewNumber;

    public EnterCommand(Calculator c) {
        super(c);
    }

    public void execute() {
        prevAccumulator = calculator.getAccumulator();
        prevNewNumber = calculator.getNewNumber();
        calculator.enter();
    }

    public void unexecute() {
        calculator.setAccumulator(prevAccumulator);
        calculator.setReadyForNewNumber(prevNewNumber);
    }
}
