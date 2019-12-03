/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 9 - Image Manipulator (cont.)
 * Name: David Schulz
 * Created: 2/5/19
 */

package schulzd;

import edu.msoe.cs1021.ImageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * The main FXML file's controller
 */
public class Lab9Controller implements Initializable {
    @FXML
    ImageView imageView;
    @FXML
    Button save;
    @FXML
    Button reload;
    @FXML
    Button grayscale;
    @FXML
    Button negative;
    @FXML
    Button red;
    @FXML
    Button redGray;
    @FXML
    Button filter;

    private FileChooser fileChooser;
    private File file;
    private Image image;
    private Alert alert;
    private Logger logger;
    private FileHandler handler;
    private Stage kernelStage;
    private boolean filterShowing;

    private static final int RGB_SCALE = 255;

    /**
     * The FXML controller's startup method
     * @param location The location of the FXML file
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.gif", "*.tiff", "*.msoe", "*.bmsoe"));

        alert = new Alert(Alert.AlertType.ERROR);

        logger = Logger.getLogger("Logger");
        logger.setUseParentHandlers(false);
        try {
            handler = new FileHandler(System.getProperty("user.dir")
                    + File.separator + "Log.log", true);
            logger.addHandler(handler);
        } catch (IOException e) {
            logger.severe("Could not create log file");
            alert.setHeaderText("Log Error");
            alert.setContentText("Could not create log file");
            alert.showAndWait();
        }

        filterShowing = false;
    }

    /**
     * Opens an image file from a desired path
     */
    @FXML
    public void open() {
        fileChooser.setTitle("Choose an image");
        file = fileChooser.showOpenDialog(new Stage());
        reload();
        logger.info("Successfully opened image");

        save.setDisable(false);
        reload.setDisable(false);
        grayscale.setDisable(false);
        negative.setDisable(false);
        red.setDisable(false);
        redGray.setDisable(false);
        filter.setDisable(false);
    }

    /**
     * Saves the current changes to the image to a desired path
     */
    @FXML
    public void save() {
        fileChooser.setTitle("Save image");
        file = fileChooser.showSaveDialog(new Stage());
        try {
            ImageIO.write(image, file.toPath());
        } catch (FileNotFoundException e) {
            logger.severe("File could not be located");
            alert.setHeaderText("File Error");
            alert.setContentText("File could not be located");
            alert.showAndWait();
            return;
        } catch (IOException e) {
            logger.severe(e.getMessage());
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        logger.info("Successfully saved image");
    }

    /**
     * Reloads the image from the original file it was opened from
     */
    @FXML
    public void reload() {
        try {
            image = ImageIO.read(file.toPath());
        } catch (FileNotFoundException e) {
            logger.severe("File could not be located");
            alert.setHeaderText("File Error");
            alert.setContentText("File could not be located");
            alert.showAndWait();
            return;
        } catch (IOException e) {
            logger.severe(e.getMessage());
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        imageView.setImage(image);
        logger.info("Successfully reloaded image");
    }

    /**
     * Puts every pixel on a gray-only scale
     * @param event The event that caused the method to start
     */
    @FXML
    public void grayscale(ActionEvent event) {
        image = transformImage(image, (y, c) -> {
            final double redGray = 0.2126;
            final double greenGray = 0.7152;
            final double blueGray = 0.0722;

            double red = c.getRed();
            double green = c.getGreen();
            double blue = c.getBlue();

            red *= RGB_SCALE * redGray;
            green *= RGB_SCALE * greenGray;
            blue *= RGB_SCALE * blueGray;

            double grayLevel = (red + green + blue) / RGB_SCALE;

            return new Color(grayLevel, grayLevel, grayLevel, c.getOpacity());
        });

        imageView.setImage(image);
        logger.info("Successfully made image grayscale");
    }

    /**
     * Makes the R, G, and B values of every pixel the opposite (255 - value)
     * @param event The event that caused the method to start
     */
    @FXML
    public void negative(ActionEvent event) {
        image = transformImage(image, (y, c) -> {
            double red = c.getRed();
            double green = c.getGreen();
            double blue = c.getBlue();

            red = 1 - red;
            green = 1 - green;
            blue = 1 - blue;

            return new Color(red, green, blue, c.getOpacity());
        });

        imageView.setImage(image);
        logger.info("Successfully made image negative");
    }

    /**
     * Removes the green and blue values of every pixel in the image
     * @param event The event that caused the method to start
     */
    @FXML
    public void red(ActionEvent event) {
        image = transformImage(image, (y, c) -> new Color(c.getRed(), 0, 0, c.getOpacity()));

        imageView.setImage(image);
        logger.info("Successfully made image red only");
    }

    /**
     * Alternates between red and grayscale every pixel row of the image
     * @param event The event that caused the method to start
     */
    @FXML
    public void redGray(ActionEvent event) {
        image = transformImage(image, (y, c) -> {
            double red = c.getRed();
            double green = c.getGreen();
            double blue = c.getBlue();

            if (y % 2 == 0) {
                final double redGray = 0.2126;
                final double greenGray = 0.7152;
                final double blueGray = 0.0722;

                double grayRed = red * RGB_SCALE * redGray;
                double grayGreen = green * RGB_SCALE * greenGray;
                double grayBlue = blue * RGB_SCALE * blueGray;

                red = (grayRed + grayGreen + grayBlue) / RGB_SCALE;
                green = (grayRed + grayGreen + grayBlue) / RGB_SCALE;
                blue = (grayRed + grayGreen + grayBlue) / RGB_SCALE;
            } else {
                green = 0;
                blue = 0;
            }

            return new Color(red, green, blue, c.getOpacity());
        });

        imageView.setImage(image);
        logger.info("Successfully made image red-gray");
    }

    /**
     * Toggles the filter button between show and hide
     * @param event The event that caused the method to start
     */
    @FXML
    public void toggleFilter(ActionEvent event) {
        if (filterShowing) {
            kernelStage.hide();
            filter.setText("Show Filter");
        } else {
            kernelStage.show();
            filter.setText("Hide Filter");
        }

        filterShowing = !filterShowing;
    }

    /**
     * Applies the given kernel to the currently displayed image
     * @param kernel The given kernel from the KernelController
     */
    public void filterKernel(double[] kernel) {
        filterShowing = false;
        filter.setText("Show Filter");

        image = ImageUtil.convolve(image, kernel);
        imageView.setImage(image);
        logger.info("Successfully filtered image with kernel");
    }

    private static Image transformImage(Image image, Transformable transform) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage changedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = changedImage.getPixelWriter();
        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setColor(x, y, transform.apply(y, pixelReader.getColor(x, y)));
            }
        }
        return changedImage;
    }

    public void setKernelStage(Stage stage) {
        kernelStage = stage;
    }

    public Logger getLogger() {
        return logger;
    }
}
