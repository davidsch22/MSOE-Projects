/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Exercise 1
 * Author:     David Schulz
 * Date:       12/6/2019
 */

package schulzd;

public interface QueueInterface {
    boolean isEmpty();
    void add(String nextItem);
    void dump();
    String get();
}
