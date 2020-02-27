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
 * Observer for all of the cars.
 *
 * Creates the MSOE challenge pane on first instance creation and then
 * finds any letters that are in both the goal and the plate
 * of the car that the CarObserver instance is connected to.
 *
 * Also handles finding letters for the bus challenge once it is activated.
 */
public class CarObserver implements Observer {
    private static VBox carPane;
    private static String goal;
    private static String carFound;
    private static boolean initialized;

    private static boolean isComplete;
    private static Label carFoundLabel;
    private String carPlate;

    private static String busFound;
    private static boolean isBusComplete;

    public CarObserver(Pane pane, String goal) {
        if (initialized) return;
        initialized = true;

        CarObserver.goal = goal;
        carFound = "";
        for (int i = 0; i < goal.length(); i++) {
            carFound += "*";
        }
        busFound = "***";

        carPane = new VBox();
        pane.getChildren().add(carPane);

        carFoundLabel = new Label("Found: " + carFound);
        carPane.getChildren().add(new Label("Challenge: Find all the letters in " + goal));
        carPane.getChildren().add(new Label("Goal: " + goal));
        carPane.getChildren().add(carFoundLabel);
        carPane.getChildren().add(new Label(""));
    }

    @Override
    public void update() {
        if (!isComplete) {
            char[] foundChars = carFound.toCharArray();

            for (int i = 0; i < goal.length(); i++) {
                if (foundChars[i] == '*' && carPlate.contains(goal.charAt(i) + "")) {
                    foundChars[i] = goal.charAt(i);
                }
            }

            carFound = String.valueOf(foundChars);
            int foundIndex = carPane.getChildren().indexOf(carFoundLabel);
            carFoundLabel = new Label("Found: " + carFound);
            carPane.getChildren().set(foundIndex, carFoundLabel);

            if (!carFound.contains("*")) {
                isComplete = true;
                carPane.getChildren().add(foundIndex + 1, new Label("MSOE CHALLENGE COMPLETED"));
            }
        }

        if (BusObserver.busHit && !isBusComplete) {
            busUpdate();
        }
    }

    @Override
    public void setPlate(String name) {
        int splitIndex = name.indexOf(":") + 1;
        carPlate = name.substring(splitIndex);
    }

    private void busUpdate() {
        String busGoal = "BUS";
        char[] foundChars = busFound.toCharArray();

        for (int i = 0; i < busGoal.length(); i++) {
            if (foundChars[i] == '*' && carPlate.contains(busGoal.charAt(i) + "")) {
                foundChars[i] = busGoal.charAt(i);
            }
        }

        busFound = String.valueOf(foundChars);
        int foundIndex = 2;
        BusObserver.busPane.getChildren().set(foundIndex, new Label("Found: " + busFound));

        if (!busFound.contains("*")) {
            isBusComplete = true;
            BusObserver.busPane.getChildren().add(foundIndex + 1, new Label("BUS CHALLENGE COMPLETED"));
        }
    }
}
