/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 7 - Shapes Revisited
 * Name: David Schulz
 * Created: 1/21/19
 */

package schulzd;

import edu.msoe.winplotterfx.WinPlotterFX;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class creates shapes and "tells" them to draw themselves in a
 * provided WinPlotterFX window.
 * @author David Schulz
 * @version 1.0
 */
public class ShapeLoaderApp extends Application {
    WinPlotterFX plotter;
    Scanner in;
    ArrayList<Shape> shapes;

    /**
     * Launches the JavaFX application
     * @param args ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Draw the desired shapes from the given text file
     * @param stage Default stage given to a JavaFX program. Unused.
     */
    @Override
    public void start(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        plotter = new WinPlotterFX();
        shapes = new ArrayList<>();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file");
        //File file = fileChooser.showOpenDialog(stage);
        File file = new File("Test.txt");

        try {
            in = new Scanner(file);

            // Title Line
            String title = in.nextLine();
            plotter.setTitle(title);

            // Size Line
            String size = in.nextLine();
            double width = Double.parseDouble(size.substring(0, size.indexOf(" ")));
            double height = Double.parseDouble(size.substring(size.indexOf(" ")));
            plotter.setWindowSize(width, height);

            // Background Color Line
            String colorHex = in.nextLine();
            Color color = stringToColor(colorHex);
            double red = color.getRed();
            double green = color.getGreen();
            double blue = color.getBlue();
            plotter.setBackgroundColor(red, green, blue);

        } catch (FileNotFoundException e) {
            alert.setHeaderText("File Error");
            alert.setContentText("Unable to find desired file");
            alert.showAndWait();
            return;
        } catch (NumberFormatException e) {
            alert.setHeaderText("Number Format Error");
            alert.setContentText("Window width or height aren't valid numbers");
            alert.showAndWait();
            return;
        } catch (InputMismatchException e) {
            alert.setHeaderText("Color Error");
            alert.setContentText("Background color isn't valid hexadecimal color format");
            alert.showAndWait();
            return;
        }

        readShapes();
        drawShapes();

        plotter.showPlotter();
    }

    /**
     * Go through each line of the file, adding a specific Shape
     * object to the shapes ArrayList per line
     */
    public void readShapes() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Shape Parse Error");
        int line = 3;

        while (in.hasNextLine()) {
            try {
                line++;
                String currentLine = in.nextLine();
                shapes.add(parseShape(currentLine));
            } catch (InputMismatchException | IllegalArgumentException e) {
                if (e.getMessage() != null) {
                    alert.setContentText("Line " + line + ": " + e.getMessage());
                } else {
                    alert.setContentText("Line " + line + ": There was a problem reading the line");
                }
                alert.showAndWait();
            }
        }
    }

    /**
     * Reads a line from the file and creates a new Shape object with
     * the corresponding details said in the line
     * @param line One line from the file describing a shape
     * @return The Shape object described from the line
     * @throws InputMismatchException If hexadecimal color format isn't correct
     * or line wasn't correctly read
     * @throws IllegalArgumentException If shape type isn't recognized
     * or shape dimensions aren't valid
     */
    public static Shape parseShape(String line) throws InputMismatchException,
            IllegalArgumentException {
        Scanner reader = new Scanner(line);

        String type = reader.next();
        int xCoord = reader.nextInt();
        int yCoord = reader.nextInt();
        String colorStr = reader.next();
        Color color = stringToColor(colorStr);
        double value1;
        double value2;
        String label;

        switch (type) {
            case "P:":
                return new Point(xCoord, yCoord, color);
            case "C:":
                value1 = reader.nextDouble();
                return new Circle(xCoord, yCoord, value1, color);
            case "T:":
                value1 = reader.nextDouble();
                value2 = reader.nextDouble();
                return new Triangle(xCoord, yCoord, value1, value2, color);
            case "R:":
                value1 = reader.nextDouble();
                value2 = reader.nextDouble();
                return new Rectangle(xCoord, yCoord, value1, value2, color);
            case "LT:":
                value1 = reader.nextDouble();
                value2 = reader.nextDouble();
                label = reader.nextLine();
                return new LabeledTriangle(xCoord, yCoord, value1, value2, color, label);
            case "LR:":
                value1 = reader.nextDouble();
                value2 = reader.nextDouble();
                label = reader.nextLine();
                return new LabeledRectangle(xCoord, yCoord, value1, value2, color, label);
            default:
                throw new IllegalArgumentException("Shape type not recognized");
        }
    }

    /**
     * Render each shape on the window
     */
    public void drawShapes() {
        for (Shape shape : shapes) {
            shape.draw(plotter);
        }
    }

    /**
     * Converts a color in hexadecimal format to a Color object
     * @param hexColor The hexadecimal format
     * @return The Color object it equals
     * @throws InputMismatchException If string isn't correct hex color format
     */
    public static Color stringToColor(String hexColor) throws InputMismatchException {
        final int hexColorLength = 7;
        final int hexBase = 16;
        if (hexColor.charAt(0) != '#' || hexColor.length() != hexColorLength) {
            throw new InputMismatchException("Shape doesn't have valid hexadecimal color format");
        }
        for (int i = 1; i < hexColorLength; i++) {
            if (Character.digit(hexColor.charAt(i), hexBase) == -1) {
                throw new InputMismatchException(
                        "Shape doesn't have valid hexadecimal color format");
            }
        }

        return Color.web(hexColor);
    }
}