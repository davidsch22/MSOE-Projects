/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 3 - Connect the Dots Generator Revisited
 * Name: David Schulz
 * Created: 3/21/19
 */

package msoe.schulzd.lab3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Stores the list of Dots that were read from the .dot file
 */
public class Picture {
    private List<Dot> dots;

    /**
     * Picture constructor
     * @param emptyList An empty List of Dots
     */
    public Picture(List<Dot> emptyList) {
        dots = emptyList;
    }

    /**
     * Constructor that copies the dots from original into
     * emptyList and uses it to store this picture's dots
     * @param original The original Picture
     * @param emptyList An empty List of Dots
     */
    public Picture(Picture original, List<Dot> emptyList) {
        emptyList.addAll(original.dots);
        dots = emptyList;
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

        while (input.hasNextLine()) {
            String line = input.nextLine();
            line.replaceAll(" ", "");
            double x = Double.parseDouble(line.substring(0, line.indexOf(",")));
            double y = Double.parseDouble(line.substring(line.indexOf(",") + 1));
            dots.add(new Dot(x, y));
        }

        input.close();
    }

    /**
     * Saves the current state of the dots List to a .dot file
     * @param path The path of the .dot file
     * @throws FileNotFoundException Thrown if desired path of file can't be found
     */
    public void save(Path path) throws FileNotFoundException {
        File file = path.toFile();
        PrintWriter writer = new PrintWriter(file);

        for (Dot dot : dots) {
            writer.println(dot.getX() + ", " + dot.getY());
        }

        writer.close();
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

    /**
     * Removes the Dot with the lowest critical value from the List until
     * the total number of Dots equals the number of Dots desired.
     * Iterates through the dots with index
     * @param numberDesired Number of dots left
     * @return Amount of time the method took to run in ms
     * @throws IllegalArgumentException Thrown if numberDesired is less than 3
     */
    public long removeDots(int numberDesired) throws IllegalArgumentException {
        long timeStarted = System.nanoTime();

        if (numberDesired < 3) {
            throw new IllegalArgumentException();
        } else if (numberDesired >= dots.size()) {
            return -1;
        }

        while (dots.size() > numberDesired) {
            int lowestValueIndex = -1;
            double lowestCritValue = Double.MAX_VALUE;

            for (int i = 0; i < dots.size(); i++) {
                Dot previousDot;
                Dot currentDot = dots.get(i);
                Dot nextDot;

                if (i == 0) {
                    previousDot = dots.get(dots.size() - 1);
                    nextDot = dots.get(i + 1);
                } else if (i == dots.size() - 1) {
                    previousDot = dots.get(i - 1);
                    nextDot = dots.get(0);
                } else {
                    previousDot = dots.get(i - 1);
                    nextDot = dots.get(i + 1);
                }

                double criticalValue = currentDot.calculateCriticalValue(previousDot, nextDot);
                if (criticalValue < lowestCritValue) {
                    lowestCritValue = criticalValue;
                    lowestValueIndex = i;
                }
            }

            dots.remove(lowestValueIndex);
        }

        long timeEnded = System.nanoTime();
        return timeEnded - timeStarted;
    }

    /**
     * Removes the Dot with the lowest critical value from the List until
     * the total number of Dots equals the number of Dots desired.
     * Iterates through the dots with Iterator
     * @param numberDesired Number of dots left
     * @return Amount of time the method took to run in ms
     * @throws IllegalArgumentException Thrown if numberDesired is less than 3
     */
    public long removeDots2(int numberDesired) {
        long timeStarted = System.nanoTime();

        while (dots.size() > numberDesired) {
            Iterator iterator = dots.iterator();

            Dot firstDot = (Dot)iterator.next();
            Dot secondDot = (Dot)iterator.next();
            Dot lastDot;

            Dot previousDot = firstDot;
            Dot currentDot = secondDot;
            Dot nextDot = (Dot)iterator.next();

            Dot lowestCritDot = currentDot;
            double criticalValue;
            double lowestCritValue = currentDot.calculateCriticalValue(previousDot, nextDot);

            // All dots in the middle
            while (iterator.hasNext()) {
                previousDot = currentDot;
                currentDot = nextDot;
                nextDot = (Dot)iterator.next();

                criticalValue = currentDot.calculateCriticalValue(previousDot, nextDot);
                if (criticalValue < lowestCritValue) {
                    lowestCritDot = currentDot;
                    lowestCritValue = criticalValue;
                }
            }

            lastDot = nextDot;

            // Last dot
            previousDot = currentDot;
            currentDot = lastDot;
            nextDot = firstDot;

            criticalValue = currentDot.calculateCriticalValue(previousDot, nextDot);
            if (criticalValue < lowestCritValue) {
                lowestCritDot = currentDot;
            }

            // First dot
            previousDot = lastDot;
            currentDot = firstDot;
            nextDot = secondDot;

            criticalValue = currentDot.calculateCriticalValue(previousDot, nextDot);
            if (criticalValue < lowestCritValue) {
                lowestCritDot = currentDot;
            }

            dots.remove(lowestCritDot);
        }

        long timeEnded = System.nanoTime();
        return timeEnded - timeStarted;
    }

    public List<Dot> getDots() {
        return dots;
    }
}
