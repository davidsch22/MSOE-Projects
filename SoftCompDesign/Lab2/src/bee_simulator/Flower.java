package bee_simulator;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * @author schulzd
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class Flower extends Entity {
	public Flower(Image image, Pane container, EnergyLevel energyLevel, Location location) {
        super(image, container, energyLevel, location);
    }
}