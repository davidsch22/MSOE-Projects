/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 8 - Image Manipulator
 * Name: David Schulz
 * Created: 1/30/19
 */

package schulzd;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * The FXML file's controller
 */
public class Lab8Controller implements Initializable {
    @FXML
    ImageView imageView;
    @FXML
    Label pixelColor;

    private FileChooser fileChooser;
    private File file;
    private Image image;
    private Alert alert;
    private Logger logger;
    private FileHandler handler;

    private final int alphaPos = 24;
    private final int redPos = 16;
    private final int greenPos = 8;
    private final int rgbScale = 0xff;

    /**
     * The FXML controller's startup method
     * @param location The location of the FXML file
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.gif", "*.tiff", "*.msoe"));

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
    }

    /**
     * Opens an image file from a desired path
     */
    public void open() {
        fileChooser.setTitle("Choose an image");
        file = fileChooser.showOpenDialog(new Stage());
        reload();
        logger.info("Successfully opened image");
    }

    /**
     * Saves the current changes to the image to a desired path
     */
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
     */
    public void grayscale() {
        final double redGray = 0.2126;
        final double greenGray = 0.7152;
        final double blueGray = 0.0722;

        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage grayImage = new WritableImage(width, height);
        PixelWriter pixelWriter = grayImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> alphaPos) & rgbScale);
                int red = ((pixel >> redPos) & rgbScale);
                int green = ((pixel >> greenPos) & rgbScale);
                int blue = (pixel & rgbScale);

                int grayLevel = (int) (redGray * red + greenGray * green + blueGray * blue);
                int gray = (alpha << alphaPos) + (grayLevel << redPos)
                        + (grayLevel << greenPos) + grayLevel;

                pixelWriter.setArgb(x, y, gray);
            }
        }

        image = grayImage;
        imageView.setImage(image);
        logger.info("Successfully made image grayscale");
    }

    /**
     * Makes the R, G, and B values of every pixel the opposite (255 - value)
     */
    public void negative() {
        final int maxRGB = 255;
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage negativeImage = new WritableImage(width, height);
        PixelWriter pixelWriter = negativeImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> alphaPos) & rgbScale);
                int red = ((pixel >> redPos) & rgbScale);
                int green = ((pixel >> greenPos) & rgbScale);
                int blue = (pixel & rgbScale);

                red = maxRGB - red;
                green = maxRGB - green;
                blue = maxRGB - blue;

                int negative = (alpha << alphaPos) | (red << redPos) | (green << greenPos) | blue;

                pixelWriter.setArgb(x, y, negative);
            }
        }

        image = negativeImage;
        imageView.setImage(image);
        logger.info("Successfully made image negative");
    }
}
