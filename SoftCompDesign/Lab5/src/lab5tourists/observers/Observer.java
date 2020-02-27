/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 5: Tourists
 * Author:     David Schulz
 * Date:       2 Feb 2019
 */
package lab5tourists.observers;

/**
 * Simple interface for all Observer objects.
 *
 * The cars, buses, and museums use this to notify the challenge pane
 * about what specific collisions occurred.
 */
public interface Observer {
    void update();
    void setPlate(String plate);
}
