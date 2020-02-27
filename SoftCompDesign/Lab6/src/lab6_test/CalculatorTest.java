package lab6_test;

import lab6.Calculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Exercise Calculator methods.
 *
 * @author R. Hasker
 * @date Feb. 2018
 */
public class CalculatorTest {

    @Test
    public void calc_3_plus_4() {
        Calculator c = new Calculator();
        c.appendDigit('3');
        assertEquals("3", c.result());
        c.enter();
        c.appendDigit('4');
        assertEquals("4", c.result());
        c.plus();
        assertEquals("7", c.result());
    }

    @Test
    public void calc_678_plus_1803() {
        Calculator c = new Calculator();
        c.appendDigit('6');
        c.appendDigit('7');
        c.appendDigit('8');
        assertEquals("678", c.result());
        c.enter();
        c.appendDigit('1');
        c.appendDigit('8');
        c.appendDigit('0');
        c.appendDigit('3');
        assertEquals("1803", c.result());
        c.plus();
        assertEquals("" + (678 + 1803), c.result());
    }

    @Test
    public void calc_49_minus_00180() {
        Calculator c = new Calculator();
        c.appendDigit('4');
        c.appendDigit('9');
        c.enter();
        c.appendDigit('0');
        c.appendDigit('0');
        c.appendDigit('1');
        c.appendDigit('8');
        c.appendDigit('0');
        assertEquals("180", c.result());
        c.minus();
        assertEquals("" + (49 - 180), c.result());
    }

    @Test
    public void calc_12_times_37() {
        Calculator c = new Calculator();
        c.appendDigit('1');
        c.appendDigit('2');
        c.enter();
        c.appendDigit('3');
        c.appendDigit('7');
        c.times();
        assertEquals("" + (12 * 37), c.result());
    }

    @Test
    public void calc_multiply_by_0() {
        Calculator c = new Calculator();
        c.appendDigit('1');
        c.appendDigit('2');
        c.enter();
        c.appendDigit('0');
        c.times();
        assertEquals("0", c.result());
    }

    @Test
    public void simple_square() {
        Calculator c = new Calculator();
        c.appendDigit('1');
        c.appendDigit('2');
        c.enter();
        c.times();
        assertEquals("144", c.result());
    }

    @Test
    public void calc_3_plus_4_times_10() {
        Calculator c = new Calculator();
        c.appendDigit('3');
        c.enter();
        c.appendDigit('4');
        c.plus();
        assertEquals("7", c.result());
        assertEquals("7", c.getAccumulator());
        c.appendDigit('1');
        c.appendDigit('0');
        assertEquals("10", c.result());
        assertEquals("7", c.getAccumulator());
        c.times();
        assertEquals("70", c.getAccumulator());
        assertEquals("70", c.result()); // NOT 43!
    }

    @Test
    public void store_and_reuse_45() {
        Calculator c = new Calculator();
        c.appendDigit('4');
        c.appendDigit('5');
        c.saveToMemory();
        c.recallFromMemory();
        c.times();
        assertEquals("" + (45 * 45), c.result());
    }

    @Test
    public void calc_with_memory_mix1() {
        // 71 (save) - 34 * 2 + (recall) * (recall)
        Calculator c = new Calculator();
        c.appendDigit('7');
        c.appendDigit('1');
        c.saveToMemory();
        c.appendDigit('3');
        c.appendDigit('4');
        c.minus();
        c.appendDigit('2');
        c.times();
        c.enter();
        c.recallFromMemory();
        c.plus();
        c.recallFromMemory();
        c.times();
        assertEquals("" + (
              (((71 - 34) * 2) + 71) * 71
        ), c.result());
    }

    @Test
    public void clear_all() {
        Calculator c = new Calculator();
        c.appendDigit('8');
        c.saveToMemory();
        c.appendDigit('5');
        assertEquals("5", c.getDisplay());
        assertEquals("8", c.getMemory());
        assertEquals("8", c.getAccumulator());
        c.clear();
        assertEquals("0", c.getDisplay());
        assertEquals("0", c.getMemory());
        assertEquals("0", c.getAccumulator());
        c.appendDigit('9');
        c.appendDigit('2');
        assertEquals("92", c.result());
    }

    @Test
    public void set_state_normal_case() {
        Calculator c = new Calculator();
        c.appendDigit('1');
        c.saveToMemory();
        c.appendDigit('2');
        c.enter();
        c.appendDigit('3');
        assertEquals("1", c.getMemory());
        assertEquals("2", c.getAccumulator());
        assertEquals("3", c.getDisplay());
        assert c.getNewNumber() == false;
        c.setDisplay("22");
        c.setMemory("33");
        c.setAccumulator("44");
        c.setReadyForNewNumber(true);
        assertEquals("33", c.getMemory());
        assertEquals("44", c.getAccumulator());
        assertEquals("22", c.getDisplay());
        assert c.getNewNumber() == true;
    }

    @Test
    public void set_state_to_nulls_and_bad_values() {
        Calculator c = new Calculator();
        c.setDisplay("22");
        c.setMemory("33");
        c.setAccumulator("44");
        c.setReadyForNewNumber(true);
        assertEquals("22", c.getDisplay());
        assertEquals("33", c.getMemory());
        assertEquals("44", c.getAccumulator());
        assert c.getNewNumber() == true;
        c.setDisplay(null);
        c.setMemory(null);
        c.setAccumulator(null);
        c.setReadyForNewNumber(false);
        assertEquals("22", c.getDisplay());
        assertEquals("33", c.getMemory());
        assertEquals("44", c.getAccumulator());
        assert c.getNewNumber() == false;
        c.setDisplay("1a");
        c.setMemory("2a");
        c.setAccumulator("3a");
        c.setReadyForNewNumber(true);
        assertEquals("22", c.getDisplay());
        assertEquals("33", c.getMemory());
        assertEquals("44", c.getAccumulator());
        assert c.getNewNumber() == true;
    }
}