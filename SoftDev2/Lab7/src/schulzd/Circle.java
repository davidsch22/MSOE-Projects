/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 7 - Shapes Revisited
 * Name: David Schulz
 * Created: 1/21/19
 */

package schulzd;

import edu.msoe.winplotterfx.WinPlotterFX;
import javafx.scene.paint.Color;

/**
 * Circle class
 */
public class Circle extends Shape {
    private final double radius;

    /**
     * Circle constructor
     * @param x The x coordinate of the center
     * @param y The y coordinate of the center
     * @param radius The radius of the Circle
     * @param color The color of the shape
     */
    public Circle(double x, double y, double radius, Color color) {
        super(x, y, color);
        if (radius < 0) {
            throw new IllegalArgumentException("Can't have a negative radius");
        } else {
            this.radius = radius;
        }
    }

    /**
     * The draw method for a Circle
     * @param plotter The WinPlotterFX program being used
     */
    public void draw(WinPlotterFX plotter) {
        setPenColor(plotter);
        plotter.moveTo(x + radius, y);
        for(double i = 0; i <= (2 * Math.PI); i += (1 / radius)) {
            plotter.drawTo(x + radius * Math.cos(i), y + radius * Math.sin(i));
        }
    }
}