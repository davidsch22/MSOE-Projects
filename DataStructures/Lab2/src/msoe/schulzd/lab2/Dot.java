/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 2 - Connect the Dots Generator
 * Name: David Schulz
 * Created: 3/10/19
 */

package msoe.schulzd.lab2;

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

    /**
     * Calculates how important the Dot is when accurately creating the Picture
     * @param previous The previous Dot in the order
     * @param next The next Dot in the order
     * @return The Dot's critical value
     */
    public double calculateCriticalValue(Dot previous, Dot next) {
        double previousToCurrent = Math.sqrt(Math.pow(x - previous.x, 2) +
                Math.pow(y - previous.y, 2));
        double currentToNext = Math.sqrt(Math.pow(next.x - x, 2) +
                Math.pow(next.y - y, 2));
        double previousToNext = Math.sqrt(Math.pow(next.x - previous.x, 2) +
                Math.pow(next.y - previous.y, 2));
        return previousToCurrent + currentToNext - previousToNext;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
