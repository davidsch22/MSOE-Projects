/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Exercise 1
 * Author:     David Schulz
 * Date:       12/6/2019
 */

package schulzd;

import java.util.ArrayList;

public class SimpleQueueAdapter implements QueueInterface {
    private SimpleLinkedList<String> items = new SimpleLinkedList<>();

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void add(String nextItem) {
        items.addFirst(nextItem);
    }

    @Override
    public void dump() {
        System.out.println(items.toString());
    }

    @Override
    public String get() {
        if (isEmpty())
            return null;
        else
            return items.last();
    }
}
