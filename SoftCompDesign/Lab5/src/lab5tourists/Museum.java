/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 5: Tourists
 * Author:     Dr. Yoder and David Schulz
 * Date:       13 Jan 2019
 */
package lab5tourists;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lab5tourists.mobileEntities.MobileEntity;
import lab5tourists.observers.Observer;

/**
 * A rectangular area representing an art museum.
 *
 * The Museum is similar to a MobileEntity, except that it does not move
 * and it has a rectangular rather than circular tag area.
 */
public class Museum implements Taggable {
    public static final int MUSEUM_WIDTH = 40;
    public static final int MUSEUM_HEIGHT = 50;
    public static final int MUSEUM_LEFT_CORNER = 500-64;
    public static final int MUSEUM_TOP_CORNER = 250+3;
    private final Rectangle area;

    private Observer observer;

    public Museum(CityMap cityMap) {
        area = new Rectangle(MUSEUM_WIDTH, MUSEUM_HEIGHT);
        area.setStroke(Color.RED);
        area.setFill(Color.RED.deriveColor(1, 1, 1, 0.2));
        area.relocate(MUSEUM_LEFT_CORNER, MUSEUM_TOP_CORNER);

        cityMap.addNodeToMap(area);
        cityMap.addTaggableToMap(this);
    }

    /**
     * Determines if point falls within the rectangular region shown on the map
     * @param location The location to check
     * @return true of within region
     */
    @Override
    public boolean isTagged(Point2D location) {
        return location.getX() > MUSEUM_LEFT_CORNER
            && location.getY() > MUSEUM_TOP_CORNER
            && location.getX() < MUSEUM_LEFT_CORNER + MUSEUM_WIDTH
            && location.getY() < MUSEUM_TOP_CORNER + MUSEUM_HEIGHT;
    }

    /**
     * Respond to being tagged by another entity.
     * @param entity The entity performing the tagging.
     */
    @Override
    public void taggedBy(MobileEntity entity) {
        if (entity.getName().equals("You")) {
            notifyObserver();
        }
    }

    public void attach(Observer obs) {
        observer = obs;
    }

    public void detach(Observer obs) {
        if (observer == obs) {
            observer = null;
        }
    }

    private void notifyObserver() {
        if (observer != null) {
            observer.update();
        }
    }

    /**
     * @return a unique description of this Museum, including a hashcode
     */
    @Override
    public String toString() {
        return "Museum "+Integer.toHexString(super.hashCode());
    }
}
