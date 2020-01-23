package bee_simulator;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * @author schulzd
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class Bee extends Entity {
	public Bee(Image image, Pane container, EnergyLevel energyLevel, Location location) {
		super(image, container, energyLevel, location);
	}

	public void move(){
		energyLevel.tick();
	}

	public boolean tick() {
		move();
		if (!energyLevel.isGreaterThanZero()) {
			remove();
			return false;
		}
		return true;
	}

	public void setTargetFlower(Flower flower) {
		// Do nothing, overrided by NormalBee
	}
}