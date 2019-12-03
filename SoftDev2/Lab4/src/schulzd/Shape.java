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
 * Shape class
 */
public abstract class Shape {
    private Color color;
    protected final double x;
    protected final double y;

    /**
     * Shape constructor
     * @param x The x coordinate of the bottom left corner/center
     * @param y The y coordinate of the bottom left corner/center
     * @param color The color of the shape
     */
    public Shape(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * The draw method for all shapes
     * @param plotter The WinPlotterFX program being used
     */
    public abstract void draw(WinPlotterFX plotter);

    /**
     * Sets the pen color on the WinPlotterFX program
     * @param plotter The WinPlotterFX program being used
     */
    public void setPenColor(WinPlotterFX plotter) {
        plotter.setPenColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
