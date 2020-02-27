/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 5: Tourists
 * Author:     Dr. Yoder and David Schulz
 * Date:       13 Jan 2019
 */
package lab5tourists.mobileEntities;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lab5tourists.CityMap;

/**
 * The main character in the game.
 *
 * This person walks straight toward the Goal circle (see inner class).
 */
public class Person extends MobileEntity {
    private static final Image SMILEY_IMAGE = new Image(CityMap.class.getResource("img/smiley.png")
            .toString());
    public static final int PERSON_MAX_SPEED = 2;
    private Goal goal;

    public Person(CityMap cityMap, Goal goal) {
        super(cityMap, SMILEY_IMAGE);
        setName("You");
        stepSize = new Point2D(0,0);
        this.goal = goal;
    }

    /**
     * Step toward the goal circle.
     */
    @Override
    protected void step() {
        Point2D direction = goal.getTarget().subtract(getLocation());
        if(Math.hypot(direction.getX(), direction.getY()) < PERSON_MAX_SPEED) {
            // last step to goal
            stepSize = direction;
        } else {
            // approaching goal
            stepSize = direction.multiply(PERSON_MAX_SPEED /
                    Math.hypot(direction.getX(), direction.getY()));
        }
        super.step();
    }

    public void notifyObservers(MobileEntity subject, CityMap cityMap) { }

    /**
     * The person's Goal - the circular shape on the map that the person approaches.
     */
    public static class Goal {

        public static final int RADIUS = 100;
        private final Circle circularGoal;

        /**
         * Adds a new draggable circle to the map to serve as the goal toward which the person
         * moves
         * @param cityMap the map to which this is added
         */
        public Goal(CityMap cityMap) {
            circularGoal = new Circle(RADIUS);
            circularGoal.setStroke(Color.GREEN);
            circularGoal.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.2));
            circularGoal.relocate(0, 0);

            MouseGestures mg = new MouseGestures();
            mg.makeDraggable(circularGoal);

            cityMap.addNodeToMap(circularGoal);
        }

        /**
         * @return the center of the circular target
         */
        public Point2D getTarget() {
            return new Point2D(circularGoal.getCenterX(), circularGoal.getCenterY())
                    .add(RADIUS, RADIUS);
        }
    }

    /**
     * This inner class manages drag gestures on the person's Goal. From [1].
     *
     * [1] https://stackoverflow.com/questions/27999430/javafx-clickable-line-on-canvas
     */
    private static class MouseGestures {

        private double orgSceneX, orgSceneY;
        private double orgTranslateX, orgTranslateY;

        /** Make the Circle object draggable */
        private void makeDraggable(Circle circle) {
            circle.setOnMousePressed(circleOnMousePressedEventHandler);
            circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        }

        private EventHandler<MouseEvent> circleOnMousePressedEventHandler =
                (MouseEvent event) -> {
                    orgSceneX = event.getSceneX();
                    orgSceneY = event.getSceneY();

                    if (event.getSource() instanceof Circle) {
                        Circle p = ((Circle) (event.getSource()));

                        orgTranslateX = p.getCenterX();
                        orgTranslateY = p.getCenterY();
                    } else {
                        throw new IllegalArgumentException("Unknown type of object to drag!");
                    }
                };

        private EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
                (MouseEvent event) -> {
                    double offsetX = event.getSceneX() - orgSceneX;
                    double offsetY = event.getSceneY() - orgSceneY;

                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    if (event.getSource() instanceof Circle) {
                        Circle p = ((Circle) (event.getSource()));

                        p.setCenterX(newTranslateX);
                        p.setCenterY(newTranslateY);
                    } else {
                        throw new IllegalArgumentException("Unknown type of object to drag!");
                    }
                };
    }

}
