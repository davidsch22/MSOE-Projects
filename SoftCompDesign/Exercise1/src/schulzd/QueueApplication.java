/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Exercise 1
 * Author:     David Schulz
 * Date:       12/6/2019
 */

package schulzd;

import java.util.ArrayList;

public class QueueApplication {
    public static void main(String[] args) {
        QueueInterface todo = new SimpleQueueAdapter();
        todo.add("this");
        if ( todo.isEmpty() )
            System.out.println("Expected non-empty list.");
        todo.add("that");
        todo.add(".");
        if ( ! todo.get().equals("this") )
            System.out.println("Expected `this'");
        if ( ! todo.get().equals("that") )
            System.out.println("Expected `that'");
        for(int i = 0; i < 5; ++i)
            todo.add("do something #" + i);
        if ( ! todo.get().equals(".") )
            System.out.println("Expected `.'");
        for(int i = 0; i < 5; ++i) {
            String nextItem = todo.get();
            if ( !nextItem.equals("do something #" + i) )
                System.out.println("Missing `do something #" + i + "'.");
        }
        if ( ! todo.isEmpty() )
            System.out.println("Expected empty list.");
        System.out.println("All tests passed.");
    }
}