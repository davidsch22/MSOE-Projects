/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 1 - Dot 2 Dot Generator
 * Name: David Schulz
 * Created: 3/6/19
 */

package msoe.schulzd.lab1;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Stores the list of Dots that were read from the .dot file
 */
public class Picture {
    private List<Dot> dots;

    /**
     * Picture constructor
     */
    public Picture() {
        dots = new ArrayList<>();
    }

    /**
     * Reads the .dot file
     * @param path The path of the .dot file
     * @throws FileNotFoundException Thrown if .dot file can't be found
     * @throws NoSuchElementException Thrown if file is not formatted correctly
     * @throws IndexOutOfBoundsException Thrown if the file is not formatted correctly
     */
    public void load(Path path) throws FileNotFoundException, NoSuchElementException,
            IndexOutOfBoundsException {
        File file = path.toFile();
        Scanner input = new Scanner(file);

        while (input.hasNext()) {
            String firstCoord = input.next();
            String xString = firstCoord.substring(0, firstCoord.indexOf(","));
            double x = Double.parseDouble(xString);

            double y = input.nextDouble();
            dots.add(new Dot(x, y));
        }

        input.close();
    }

    /**
     * Draws the dots on the canvas
     * @param canvas The FXML controller's canvas
     */
    public void drawDots(Canvas canvas) {
        final int dotSize = 5;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        for (Dot dot : dots) {
            double xScaled = dot.getX() * canvas.getWidth();
            double yScaled = canvas.getHeight() - (dot.getY() * canvas.getHeight());
            double xCentered = xScaled - (dotSize / 2);
            double yCentered = yScaled - (dotSize / 2);

            gc.fillOval(xCentered, yCentered, dotSize, dotSize);
        }
    }

    /**
     * Draws the lines between dots on the canvas
     * @param canvas The FXML controller's canvas
     */
    public void drawLines(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.beginPath();

        for (int i = 0; i < dots.size(); i++) {
            Dot dot = dots.get(i);
            double xScale = dot.getX() * canvas.getWidth();
            double yScale = canvas.getHeight() - (dot.getY() * canvas.getHeight());

            if (i == 0) {
                gc.moveTo(xScale, yScale);
            } else {
                gc.lineTo(xScale, yScale);
            }
        }

        gc.closePath();
        gc.stroke();
    }
}
