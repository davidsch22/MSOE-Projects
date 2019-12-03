/*
 * Course: CS1011
 * Winter 2019
 * Lab 5 - Game of Life
 * Name: David Schulz
 * Created: 1/9/2019
 */

package schulzd;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * The controller for the FXML file
 */
public class Lab5Controller implements Initializable {
    @FXML
    Pane gamePane;
    @FXML
    TextField aliveCells;
    @FXML
    TextField deadCells;
    @FXML
    Button randomizeButton;
    @FXML
    Button iterateButton;
    LifeGrid lifeGrid;

    /**
     * The FXML file's initializer
     * @param location The location of the FXML file
     * @param resources The resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert gamePane != null :
                "fx:id=\"gamePane\" was not injected: check your FXML file 'game.fxml'.";
        lifeGrid = new LifeGrid(gamePane);
    }

    /**
     * Handles the toggling between alive and dead of a single cell
     * @param mouse The cursor that caused the event
     */
    public void cellHandler(MouseEvent mouse) {
        final int cellSize = 10;
        lifeGrid.toggleCell((int)(mouse.getX() / cellSize), (int)(mouse.getY() / cellSize));
        updateCounts();
    }

    /**
     * Handles the randomizing of the cells when prompted
     */
    public void randomizeHandler() {
        lifeGrid.randomize();
        updateCounts();
    }

    /**
     * Handles the iteration of the game when prompted
     */
    public void iterateHandler() {
        lifeGrid.iterate();
        updateCounts();
    }

    private void updateCounts() {
        lifeGrid.trackCells();
        aliveCells.setText(Integer.toString(lifeGrid.getAliveCells()));
        deadCells.setText(Integer.toString(lifeGrid.getDeadCells()));
    }
}
