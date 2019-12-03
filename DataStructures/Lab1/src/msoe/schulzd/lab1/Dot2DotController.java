/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 1 - Dot 2 Dot Generator
 * Name: David Schulz
 * Created: 3/6/19
 */

package msoe.schulzd.lab1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * The FXML file's controller
 */
public class Dot2DotController implements Initializable {
    @FXML
    Canvas canvas;
    @FXML
    MenuItem linesButton;
    @FXML
    MenuItem dotsButton;

    private Picture picture;
    private GraphicsContext gc;

    /**
     * Runs when the FXML file is initialized
     * @param location The location of the file
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
    }

    /**
     * Reads a .dot file and draws the dots and lines on the canvas
     * @param event The event that caused the method to run
     */
    public void open(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a .dot File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("DOT files (*.dot)", "*.dot"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }

        picture = new Picture();
        try {
            picture.load(file.toPath());
        } catch (FileNotFoundException e) {
            throwAlert("File Error", "The file could not be found");
            return;
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            throwAlert("File Error", "The file is not formatted correctly");
            return;
        }

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        linesButton.setDisable(false);
        dotsButton.setDisable(false);

        picture.drawDots(canvas);
        picture.drawLines(canvas);
    }

    /**
     * Closes the application
     * @param event The event that caused the method to run
     */
    public void close(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Draws the dots from the .dot file on the canvas
     * @param event The event that caused the method to run
     */
    public void drawDots(ActionEvent event) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.drawDots(canvas);
    }

    /**
     * Draws the lines between dots on the canvas
     * @param event The event that caused the method to run
     */
    public void drawLines(ActionEvent event) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.drawLines(canvas);
    }

    /**
     * Creates and displays an Alert if an exception is caught
     * @param header The alert header
     * @param message The alert message
     */
    public void throwAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
