/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Lab 4: Decorators
 * Author:     Dr. Yoder and David Schulz
 * Date:       1/16/2020
 */
package schulzd;

import javafx.scene.canvas.Canvas;

public interface Network {
    void draw(Canvas canvas);
    int inputSize();
    int outputSize();
    int numLayers();
}
