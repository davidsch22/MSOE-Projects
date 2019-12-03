/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 3 - A Christmas Wish...
 * Name: David Schulz
 * Created: 12/19/18
 */
package schulzd;

import edu.msoe.winplotterfx.WinPlotterFX;
import javafx.scene.paint.Color;

/**
 * Triangle class
 */
public class Triangle extends Shape {
    protected final double base;
    protected final double height;

    /**
     * Triangle constructor
     * @param x The x coordinate of the bottom left corner
     * @param y The y coordinate of the bottom left corner
     * @param base The length of the base
     * @param height The length of the height
     * @param color The color of the shape
     */
    public Triangle(double x, double y, double base, double height, Color color) {
        super(x, y, color);
        this.base = base;
        this.height = height;
    }

    /**
     * The draw method for a Triangle
     * @param plotter The WinPlotterFX program being used
     */
    public void draw(WinPlotterFX plotter) {
        setPenColor(plotter);
        plotter.moveTo(x, y);
        plotter.drawTo(x + base, y);
        plotter.drawTo(x + (base / 2), y + height);
        plotter.drawTo(x, y);
    }
}