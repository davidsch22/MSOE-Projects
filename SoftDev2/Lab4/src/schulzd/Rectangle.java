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
 * Rectangle class
 */
public class Rectangle extends Shape {
    protected final double height;
    protected final double width;

    /**
     * Rectangle constructor
     * @param x The x coordinate of the bottom left corner
     * @param y The y coordinate of the bottom left corner
     * @param width The width of the Rectangle
     * @param height The height of the Rectangle
     * @param color The color of the shape
     */
    public Rectangle(double x, double y, double width, double height, Color color) {
        super(x, y, color);
        this.width = width;
        this.height = height;
    }

    /**
     * The draw method for a Rectangle
     * @param plotter The WinPlotterFX program being used
     */
    public void draw(WinPlotterFX plotter) {
        setPenColor(plotter);
        plotter.moveTo(x, y);
        plotter.drawTo(x + width, y);
        plotter.drawTo(x + width, y + height);
        plotter.drawTo(x, y + height);
        plotter.drawTo(x, y);
    }
}