/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 9 - Image Manipulator (cont.)
 * Name: David Schulz
 * Created: 2/5/19
 */

package schulzd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the kernel FXML file
 */
public class KernelController implements Initializable {
    @FXML
    TextField topLeft;
    @FXML
    TextField topCenter;
    @FXML
    TextField topRight;
    @FXML
    TextField centerLeft;
    @FXML
    TextField center;
    @FXML
    TextField centerRight;
    @FXML
    TextField bottomLeft;
    @FXML
    TextField bottomCenter;
    @FXML
    TextField bottomRight;

    private ArrayList<TextField> values;
    private Lab9Controller mainController;
    private Logger logger;
    private double[] kernel;

    /**
     * The FXML file's initialize method
     * @param location Location of the FXML file
     * @param resourceBundle The Resource Bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        values = new ArrayList<>();
        values.add(topLeft);
        values.add(topCenter);
        values.add(topRight);
        values.add(centerLeft);
        values.add(center);
        values.add(centerRight);
        values.add(bottomLeft);
        values.add(bottomCenter);
        values.add(bottomRight);
        kernel = new double[values.size()];
    }

    /**
     * Adjusts the kernel so the sum is 1
     * @param event The event that caused the method to start
     */
    @FXML
    public void apply(ActionEvent event) {
        for (int i = 0; i < values.size(); i++) {
            kernel[i] = Double.parseDouble(values.get(i).getText());
        }

        double sum = 0;
        for (double s : kernel) {
            sum += s;
        }

        if (sum <= 0) {
            throw new IllegalArgumentException("Invalid kernel input");
        } else {
            for (int i = 0; i < kernel.length; i++) {
                kernel[i] /= sum;
            }
        }

        logger.info("Adjusted kernel to a sum of 1");
        mainController.filterKernel(kernel);
    }

    /**
     * Sets the TextFields to the preset numbers for blurring the image
     * @param event The event that caused the method to start
     */
    @FXML
    public void blur(ActionEvent event) {
        topLeft.setText("0");
        topCenter.setText("1");
        topRight.setText("0");
        centerLeft.setText("1");
        center.setText("5");
        centerRight.setText("1");
        bottomLeft.setText("0");
        bottomCenter.setText("1");
        bottomRight.setText("0");
        logger.info("Set text fields to blur presets");
    }

    /**
     * Sets the TextFields to the preset numbers for sharpening the image
     * @param event The event that caused the method to start
     */
    @FXML
    public void sharpen(ActionEvent event) {
        topLeft.setText("0");
        topCenter.setText("-1");
        topRight.setText("0");
        centerLeft.setText("-1");
        center.setText("5");
        centerRight.setText("-1");
        bottomLeft.setText("0");
        bottomCenter.setText("-1");
        bottomRight.setText("0");
        logger.info("Set text fields to sharpen presets");
    }

    public void setMainController(Lab9Controller controller) {
        mainController = controller;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
