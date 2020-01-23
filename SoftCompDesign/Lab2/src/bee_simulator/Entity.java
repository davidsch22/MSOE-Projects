package bee_simulator;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author johnsonn
 * @version 1.0
 * @created 17-Dec-2019 15:12:26
 */
public class Entity {
    private final ImageView imageView;
    private final Pane container;
    private Location location;
    protected EnergyLevel energyLevel;
    private EnergyLevel startingLevel;
    private Rectangle grayBar;
    private Rectangle healthBar;

    public static final int ENTITY_SIZE_PX = 80;
    private static final int HEALTH_BAR_PADDING_PX = 5;
    private static final int HEALTH_BAR_HEIGHT_PX = 10;
    private static final int HEALTH_BAR_WIDTH_PX = (ENTITY_SIZE_PX - (2 * HEALTH_BAR_PADDING_PX));
    private static final int HEALTH_BAR_HEIGHT_OFFSET_PX = ENTITY_SIZE_PX - HEALTH_BAR_PADDING_PX - HEALTH_BAR_HEIGHT_PX;

	public Entity(Image image, Pane container, EnergyLevel energyLevel, Location location) {
	    this.container = container;
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(ENTITY_SIZE_PX);
        this.imageView.setFitHeight(ENTITY_SIZE_PX);
        container.getChildren().add(this.imageView);
        this.grayBar = new Rectangle(HEALTH_BAR_WIDTH_PX, HEALTH_BAR_HEIGHT_PX, Color.GRAY);
        container.getChildren().add(this.grayBar);
        this.healthBar = new Rectangle(HEALTH_BAR_WIDTH_PX, HEALTH_BAR_HEIGHT_PX, Color.GREEN);
        container.getChildren().add(this.healthBar);
        this.location = location;
        this.energyLevel = energyLevel;
        this.startingLevel = new EnergyLevel(energyLevel);
	}

	public void render() {
        healthBar.setWidth(HEALTH_BAR_WIDTH_PX * Math.abs((double) energyLevel.getLevel() / startingLevel.getLevel()));
        if (energyLevel.isGreaterThanZero()) {
            healthBar.setFill(Color.GREEN);
        } else {
            healthBar.setFill(Color.RED);
        }
        imageView.relocate(location.getX(), location.getY());
        grayBar.relocate(location.getX() + HEALTH_BAR_PADDING_PX, location.getY() + HEALTH_BAR_HEIGHT_OFFSET_PX);
        healthBar.relocate(location.getX() + HEALTH_BAR_PADDING_PX, location.getY() + HEALTH_BAR_HEIGHT_OFFSET_PX);
    }

    public boolean isColliding(Entity otherEntity) {
	    double radius = (imageView.getFitHeight() + imageView.getFitWidth()) / 4;
	    boolean coll =  location.isColliding(otherEntity.location, radius * 2);
	    if (coll && this instanceof Bee && otherEntity instanceof Bee) {
            System.out.println("Bee Collision");
        }
	    return coll;
    }

    protected void remove() {
        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(this.imageView.opacityProperty(), 1.0),
                new KeyValue(this.healthBar.opacityProperty(), 1.0),
                new KeyValue(this.grayBar.opacityProperty(), 1.0)
        );
        KeyFrame endFrame = new KeyFrame(Duration.seconds(1),
                new KeyValue(this.imageView.opacityProperty(), 0.0),
                new KeyValue(this.healthBar.opacityProperty(), 0.0),
                new KeyValue(this.grayBar.opacityProperty(), 0.0)
        );
        Timeline timeline = new Timeline(startFrame, endFrame);
        timeline.setOnFinished(actionEvent -> container.getChildren().removeAll(this.imageView, this.healthBar, this.grayBar));
        timeline.play();
    }

    public EnergyLevel getEnergyLevel() {
        return energyLevel;
    }

    public Location getLocation() {
        return location;
    }
}