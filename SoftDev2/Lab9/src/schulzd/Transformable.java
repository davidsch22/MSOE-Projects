/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 9 - Image Manipulator (cont.)
 * Name: David Schulz
 * Created: 2/5/19
 */

package schulzd;

import javafx.scene.paint.Color;

/**
 * A functional interface that transforms the pixel color
 */
@FunctionalInterface
public interface Transformable {
    /**
     * Transforms the color of the selected pixel with the desired mode
     * @param y The row of pixels that is currently selected
     * @param color The original color of the selected pixel
     * @return The new color of the selected pixel
     */
    Color apply(int y, Color color);
}
