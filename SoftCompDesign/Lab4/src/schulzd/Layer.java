/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Lab 4: Decorators
 * Author:     Dr. Yoder and David Schulz
 * Date:       1/16/2020
 */
package schulzd;

public class Layer extends NetworkLayer {
    public Layer(int numNodes) {
        super(null, numNodes);
    }

    public Layer(NetworkLayer previousLayer) {
        super(previousLayer, -1);
    }

    public Layer(NetworkLayer previousLayer, int numNodes) {
        super(previousLayer, numNodes);
    }
}
