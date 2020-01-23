package bee_simulator;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author johnsonn
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class Garden {
	private List<Entity> entities;
	private Flower badFlower;
	private Flower goodFlower;
    private List<Bee> bees;

	@FXML
	private Pane gardenPane;

	public Garden(){
		entities = new ArrayList<>();
        bees = new ArrayList<>();
	}

	@FXML
	public void initialize() {
		gardenPane.setStyle("-fx-background-color: linear-gradient(to bottom right, derive(forestgreen, 20%), derive(forestgreen, -40%));");
        bees.add(new NormalBee(new Image("file:bee-1.jpg"), gardenPane, new EnergyLevel(100), new Location(0, 0)));
        bees.add(new RandomBee(new Image("file:bee-2.jpg"), gardenPane, new EnergyLevel(100), new Location(0, 0)));
        goodFlower = new Flower(new Image("file:daisy.jpg"), gardenPane, new EnergyLevel(50), new Location(0, 0));
        badFlower = new Flower(new Image("file:nightshade.jpg"), gardenPane, new EnergyLevel(-50), new Location(0, 0));

        entities.addAll(bees);
        entities.add(goodFlower);
        entities.add(badFlower);
        setRandomPositions();

		goodFlower.render();
        badFlower.render();
		for (Bee bee: bees) {
		    bee.render();

			Random random = new Random();
		    int randFlower = random.nextInt(getFlowers().length);
		    bee.setTargetFlower(getFlowers()[randFlower]);
        }
	}

	@FXML
	public void tick(KeyEvent event) {
		if (event.getCode() == KeyCode.RIGHT) {
            bees.removeIf(bee -> !bee.tick());
			checkCollisions();
			goodFlower.render();
			badFlower.render();
		}
	}

	private void checkCollisions() {
        for (Bee bee : bees) {
			for (Bee otherBee : bees) {
				if (bee == otherBee) {
					continue;
				}
				if (bee.isColliding(otherBee)) {
					bee.getEnergyLevel().takeFrom(new EnergyLevel(-20));
				}
			}

			if (bee.isColliding(goodFlower)) {
				bee.energyLevel.takeFrom(goodFlower.energyLevel);
				bee.setTargetFlower(badFlower);
			} else if (bee.isColliding(badFlower)) {
				bee.energyLevel.takeFrom(badFlower.energyLevel);
				bee.setTargetFlower(goodFlower);
			}
		}

        for (Bee bee : bees) {
        	bee.render();

        	if (!bee.getEnergyLevel().isGreaterThanZero()) {
				bee.remove();
				bees.remove(bee);
			}
		}
	}

	public Flower[] getFlowers() {
	    return new Flower[] { badFlower, goodFlower };
    }

    public Scene createScene() {
		gardenPane.setPrefWidth(800);
	    gardenPane.setPrefHeight(600);
		Scene ret = new Scene(gardenPane);
	    ret.addEventHandler(KeyEvent.KEY_PRESSED, this::tick);
	    return ret;
    }

    private void setRandomPositions() {
		Random random = new Random();

		for (Entity entity : entities) {
			int xPos = random.nextInt(800 - Entity.ENTITY_SIZE_PX);
			int yPos = random.nextInt(600 - Entity.ENTITY_SIZE_PX);

			entity.getLocation().setX(xPos);
			entity.getLocation().setY(yPos);
		}
	}
}