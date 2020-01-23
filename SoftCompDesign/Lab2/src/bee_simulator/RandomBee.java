package bee_simulator;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Random;

/**
 * @author schulzd
 * @version 1.0
 * @created 17-Dec-2019 15:12:27
 */
public class RandomBee extends Bee {
	public RandomBee(Image image, Pane container, EnergyLevel energyLevel, Location location) {
        super(image, container, energyLevel, location);
    }

    public void move() {
        super.move();

	    Random random = new Random();
        int randXDir = random.nextInt(2);
        if (randXDir == 0) randXDir = -1;
        int randYDir = random.nextInt(2);
        if (randYDir == 0) randYDir = -1;

        int randXAmt = random.nextInt(30) + 20;
        int randYAmt = random.nextInt(30) + 20;

        int newX = getLocation().getX() + (randXDir * randXAmt);
        int newY = getLocation().getY() + (randYDir * randYAmt);
        if (newX < 0 || newX > 800 - Entity.ENTITY_SIZE_PX) {
            newX = getLocation().getX() + (-randXDir * randXAmt);
        }
        if (newY < 0 || newY > 600 - Entity.ENTITY_SIZE_PX) {
            newY = getLocation().getY() + (-randYDir * randYAmt);
        }

        getLocation().setX(newX);
        getLocation().setY(newY);
    }
}