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
 * LabeledTriangle class
 */
public class LabeledTriangle extends Triangle {
    private final String name;

    /**
     * LabeledTriangle constructor
     * @param x The x coordinate of the bottom left corner
     * @param y The y coordinate of the bottom left corner
     * @param base The length of the base
     * @param height The length of the height
     * @param color The color of the shape
     * @param name The text on the label
     */
    public LabeledTriangle(double x, double y, double base, double height,
                           Color color, String name) {
        super(x, y, base, height, color);
        this.name = name;
    }

    /**
     * The draw method for a Labeled Triangle
     * @param plotter The WinPlotterFX program being used
     */
    public void draw(WinPlotterFX plotter) {
        setPenColor(plotter);
        super.draw(plotter);
        final double xFactor = 3.5;
        final double yFactor = 3;
        plotter.printAt(x + (base / xFactor), y + (height / yFactor), name);
    }
}