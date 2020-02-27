/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 5: Tourists
 * Author:     David Schulz
 * Date:       2 Feb 2019
 */
package lab5tourists.observers;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Observer for all of the museums.
 *
 * Creates the museum challenge pane on creation and then
 * adds the art to the pane when player tags the museum.
 */
public class MuseumObserver implements Observer {
    private VBox artPane;
    private boolean foundArt;

    public MuseumObserver(Pane pane) {
        artPane = new VBox();
        pane.getChildren().add(1, artPane);

        Label artHeader = new Label("Challenge: Find art");
        artPane.getChildren().add(artHeader);
        artPane.getChildren().add(new Label(""));
    }

    @Override
    public void update() {
        if (foundArt) return;
        foundArt = true;

        Image art = new Image("lab5tourists/img/wood-gatherer.png");
        ImageView artView = new ImageView(art);
        artView.setPreserveRatio(true);
        artView.setFitWidth(100);

        int secondLastIndex = artPane.getChildren().size() - 1;
        artPane.getChildren().add(secondLastIndex, new Label("Artistic works found:"));
        artPane.getChildren().add(secondLastIndex+1, artView);
    }

    @Override
    public void setPlate(String plate) {}
}
