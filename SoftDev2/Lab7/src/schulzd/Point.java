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
 * Point class
 */
public class Point extends Shape {
    /**
     * Point constructor
     * @param x The x coordinate of the Point
     * @param y The y coordinate of the Point
     * @param color The color of the Point
     */
    public Point(double x, double y, Color color) {
        super(x, y, color);
    }

    /**
     * The draw method for a Point
     * @param plotter The WinPlotterFX program being used
     */
    public void draw(WinPlotterFX plotter) {
        setPenColor(plotter);
        plotter.drawPoint(x, y);
    }
}
