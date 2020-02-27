/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

/**
 * Abstract class to provide a guideline for specific concrete command classes.
 */
public abstract class CalculatorCommand {
    protected Calculator calculator;

    public CalculatorCommand(Calculator c) {
        calculator = c;
    }

    /**
     * Perform operation on calculator.
     */
    public abstract void execute();

    /**
     * Restore calculator to the state before performing this operation.
     */
    public abstract void unexecute();
}
