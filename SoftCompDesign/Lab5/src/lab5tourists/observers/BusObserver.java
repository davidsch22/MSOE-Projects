/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 5: Tourists
 * Author:     David Schulz
 * Date:       2 Feb 2019
 */
package lab5tourists.observers;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Observer for all of the buses.
 *
 * Creates the BUS challenge pane on first instance creation, but keeps it hidden.
 * Then shows it when the player tags a bus.
 */
public class BusObserver implements Observer {
    private static boolean initialized;

    public static VBox busPane;
    public static boolean busHit;

    public BusObserver(Pane pane) {
        if (initialized) return;
        initialized = true;

        busPane = new VBox();
        pane.getChildren().add(busPane);
    }

    @Override
    public void update() {
        if (busHit) return;
        busHit = true;

        busPane.getChildren().add(new Label("Challenge: Find all the letters in BUS"));
        busPane.getChildren().add(new Label("Goal: BUS"));
        busPane.getChildren().add(new Label("Found: ***"));
        busPane.getChildren().add(new Label(""));
    }

    @Override
    public void setPlate(String plate) { }
}
