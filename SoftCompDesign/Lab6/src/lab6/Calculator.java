/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

import java.math.BigInteger;

/**
 * Mirrors standard three-function calculator (add, subtract, multiply, with memory)
 * All calculations use arbitrary-sized integers. Does not allow entering negative
 * numbers, but negatives are supported.
 *
 * @author R. Hasker
 * @date Feb. 2018
 */
public class Calculator {
    private BigInteger display = BigInteger.ZERO;       // value shown on display
    private BigInteger accumulator = BigInteger.ZERO;   // current computed result
    private BigInteger memory = BigInteger.ZERO;        // stored value
    private boolean newNumber = true; // if true, next digit clears the display for new entry

    public Calculator() {
    }

    /**
     * Currently computed result
     *
     * @return Result as a string
     */
    public String result() {
        return display.toString();
    }

    // get operations: return strings so clients cannot change state by mistake

    public String getDisplay() {
        return result();
    }

    public String getAccumulator() {
        return accumulator.toString();
    }

    public String getMemory() {
        return memory.toString();
    }

    /**
     * Return state of newNumber; this is intended just for restoring state, so does not
     * follow the is convention
     *
     * @return whether or not next digit entered will clear the display
     */
    public boolean getNewNumber() {
        return newNumber;
    }

    /**
     * Set state of calculator display to newText. If the field is null or not numeric,
     * the display is not changed.
     *
     * @param newText Text to be displayed as the current calculator value
     */
    public void setDisplay(String newText) {
        if (newText != null && newText.matches("-?\\d+"))
            display = new BigInteger(newText);
    }

    /**
     * Set state of calculator memory to newText. If the field is null or not numeric,
     * memory is not changed.
     *
     * @param newText Text for integer to be stored in memory
     */
    public void setMemory(String newText) {
        if (newText != null && newText.matches("-?\\d+"))
            memory = new BigInteger(newText);
    }

    /**
     * Set state of calculator accumulator to newText. If the field is null or not numeric,
     * the accumulator is not changed.
     *
     * @param newText Text for integer capturing accumulated value
     */
    public void setAccumulator(String newText) {
        if (newText != null && newText.matches("-?\\d+"))
            accumulator = new BigInteger(newText);
    }

    /**
     * Set calculator so that the next digit will be the start of a new number.
     * This is useful after calculating a result.
     *
     * @param readyForNewNumber Does next digit added clear the display?
     */
    public void setReadyForNewNumber(boolean readyForNewNumber) {
        newNumber = readyForNewNumber;
    }

    /**
     * Mirrors standard clear-all button on calculator: set display, accumulator,
     * and memory to 0.
     */
    public void clear() {
        display = BigInteger.ZERO;
        accumulator = BigInteger.ZERO;
        memory = BigInteger.ZERO;
        newNumber = true;
    }

    /**
     * If character represents an (ASCII) digit, add that digit to the end of the display.
     *
     * @param c Character to append
     */
    public void appendDigit(char c) {
        if (Character.isDigit(c)) {
            if (newNumber)
                display = BigInteger.ZERO;
            newNumber = false;
            BigInteger biDigit = new BigInteger("" + c);
            display = display.multiply(BigInteger.TEN).add(biDigit);
        }
    }

    /**
     * Enters a number to allow another number to be entered. Only one number
     * can be entered at a time, so a second enter command will clear the first.
     */
    public void enter() {
        accumulator = display;
        newNumber = true;
    }

    /**
     * Perform the add operation and set to accept more numbers (as
     * opposed to adding them at the end of previous numbers).
     */
    public void plus() {
        display = accumulator = accumulator.add(display);
        newNumber = true;
    }

    /**
     * Perform the subtraction operation, where the displayed number is
     * subtracted from the previous result. 3,4- will compute -1.
     * Also sets calculator to accept new numbers.
     */
    public void minus() {
        display = accumulator = accumulator.subtract(display);
        newNumber = true;
    }

    /**
     * Perform the multiplication operation and set up for entering more numbers.
     */
    public void times() {
        display = accumulator = accumulator.multiply(display);
        newNumber = true;
    }

    /**
     * Like enter, but also stores current display in memory.
     */
    public void saveToMemory() {
        memory = display;
        accumulator = display;
        newNumber = true;
    }

    /**
     * Pull saved value from memory, but if user starts typing digits then start using them instead
     */
    public void recallFromMemory() {
        display = memory;
        newNumber = true;
    }
}
