package bee_simulator;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * @author schulzd
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class NormalBee extends Bee {
    private Flower targetFlower;

	public NormalBee(Image image, Pane container, EnergyLevel energyLevel, Location location) {
        super(image, container, energyLevel, location);
    }

    public void move() {
        super.move();
        int xDir = Integer.compare(targetFlower.getLocation().getX(), getLocation().getX());
        int yDir = Integer.compare(targetFlower.getLocation().getY(), getLocation().getY());
        getLocation().setX(getLocation().getX() + (xDir * 25));
        getLocation().setY(getLocation().getY() + (yDir * 25));
    }

    public void setTargetFlower(Flower targetFlower) {
	    this.targetFlower = targetFlower;
    }
}