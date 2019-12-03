/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 2 - Connect the Dots Generator
 * Name: David Schulz
 * Created: 3/10/19
 */

package msoe.schulzd.lab2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    @FXML
    MenuItem removeButton;
    @FXML
    MenuItem saveButton;
    @FXML
    Label dotCount;

    private FileChooser fileChooser;
    private Picture originalPicture;
    private Picture picture;
    private GraphicsContext gc;

    /**
     * Runs when the FXML file is initialized
     * @param location The location of the file
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("DOT files (*.dot)", "*.dot"));
        gc = canvas.getGraphicsContext2D();
    }

    /**
     * Reads a .dot file and draws the dots and lines on the canvas
     * @param event The event that caused the method to run
     */
    public void open(ActionEvent event) {
        fileChooser.setTitle("Open a .dot File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }

        originalPicture = new Picture(new ArrayList<>());
        try {
            originalPicture.load(file.toPath());
        } catch (FileNotFoundException e) {
            throwAlert("File Error", "The file could not be found");
            return;
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            throwAlert("File Error", "The file is not formatted correctly");
            return;
        }
        picture = new Picture(originalPicture, new ArrayList<>());
        dotCount.setText(String.valueOf(picture.getDots().size()));

        linesButton.setDisable(false);
        dotsButton.setDisable(false);
        removeButton.setDisable(false);
        saveButton.setDisable(false);

        clearCanvas();
        picture.drawDots(canvas);
        picture.drawLines(canvas);
    }

    /**
     * Saves the current Picture to a .dot file
     * @param event The event that caused the method to run
     */
    public void save(ActionEvent event) {
        fileChooser.setTitle("Save .dot File");
        File file = fileChooser.showSaveDialog(new Stage());

        try {
            picture.save(file.toPath());
        } catch (FileNotFoundException e) {
            throwAlert("File Error", "Desired file path cannot be found");
        }
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
        clearCanvas();
        picture.drawDots(canvas);
    }

    /**
     * Draws the lines between dots on the canvas
     * @param event The event that caused the method to run
     */
    public void drawLines(ActionEvent event) {
        clearCanvas();
        picture.drawLines(canvas);
    }

    /**
     * Asks the user how many dots they want left and then
     * removes the dots with the lowest critical values
     * @param event The event that caused the method to run
     */
    public void removeDots(ActionEvent event) {
        TextInputDialog prompt = new TextInputDialog("");
        prompt.setTitle("Remove Dots");
        prompt.setHeaderText("How Many?");
        prompt.setContentText("How many dots do you want to see:");

        Optional<String> answer = prompt.showAndWait();
        if (answer.isEmpty()) {
            return;
        }

        try {
            int numberDesired = Integer.parseInt(answer.get());
            if (numberDesired < originalPicture.getDots().size()) {
                picture = new Picture(originalPicture, new ArrayList<>());
                picture.removeDots(numberDesired);
            }
        } catch (NumberFormatException e) {
            throwAlert("Input Error", "Your response is not an integer");
            return;
        } catch (IllegalArgumentException e) {
            throwAlert("Input Error", "Number of dots desired can't be less than 3");
            return;
        }

        dotCount.setText(String.valueOf(picture.getDots().size()));
        clearCanvas();
        picture.drawDots(canvas);
        picture.drawLines(canvas);
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void throwAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
