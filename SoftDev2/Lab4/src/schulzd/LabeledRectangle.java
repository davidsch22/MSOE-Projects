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
 * LabeledRectangle class
 */
public class LabeledRectangle extends Rectangle {
    private final String name;

    /**
     * LabeledRectangle constructor
     * @param x The x coordinate of the bottom left corner
     * @param y The y coordinate of the bottom left corner
     * @param width The width of the Rectangle
     * @param height The height of the Rectangle
     * @param color The color of the shape
     * @param name The text on the label
     */
    public LabeledRectangle(double x, double y, double width, double height,
                            Color color, String name) {
        super(x, y, width, height, color);
        this.name = name;
    }

    /**
     * The draw method for a Labeled Rectangle
     * @param plotter The WinPlotterFX program being used
     */
    public void draw(WinPlotterFX plotter) {
        setPenColor(plotter);
        super.draw(plotter);
        final double xFactor = 3;
        final double yFactor = 2;
        plotter.printAt(x + (width / xFactor), y + (height / yFactor), name);
    }
}