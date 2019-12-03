/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 1 - Dot 2 Dot Generator
 * Name: David Schulz
 * Created: 3/6/19
 */

package msoe.schulzd.lab1;

/**
 * Dot class
 */
public class Dot {
    private double x;
    private double y;

    /**
     * Dot constructor
     * @param x The x coordinate of the dot
     * @param y The y coordinate of the dot
     */
    public Dot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
