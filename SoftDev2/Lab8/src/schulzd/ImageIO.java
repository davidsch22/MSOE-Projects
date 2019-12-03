/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 8 - Image Manipulator
 * Name: David Schulz
 * Created: 1/30/19
 */

package schulzd;

import edu.msoe.cs1021.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * The class used to read and write image files, including .msoe
 */
public class ImageIO {
    private static Image image;

    /**
     * Reads the image file at the specified path and translates it to an Image object
     * @param path The path of the file to be read
     * @return The Image object created from the file
     * @throws IOException Thrown if file can't be found
     * or there are issues creating the Image object
     */
    public static Image read(Path path) throws IOException {
        if (getExtension(path).equals("msoe")) {
            image = readMSOE(path);
        } else {
            image = ImageUtil.readImage(path);
        }

        return image;
    }

    /**
     * Translates the specified Image object to the desired image file format and path
     * @param image The image object to write
     * @param path The path of the file to write to
     * @throws IOException Thrown if file can't be found
     */
    public static void write(Image image, Path path) throws IOException {
        if (getExtension(path).equals("msoe")) {
            writeMSOE(image, path);
        } else {
            ImageUtil.writeImage(path, image);
        }
    }

    private static Image readMSOE(Path path) throws IOException {
        Scanner in = new Scanner(new File(path.toString()));

        String confirm = in.nextLine();
        if (!confirm.equals("MSOE")) {
            throw new IOException("ERROR: .msoe file does not contain a .msoe image");
        }

        int width = in.nextInt();
        int height = in.nextInt();

        WritableImage msoeImage = new WritableImage(width, height);
        PixelWriter writer = msoeImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String pixelHex = in.next();
                Color pixelColor = Color.web(pixelHex);
                writer.setColor(x, y, pixelColor);
            }
        }

        in.close();

        image = msoeImage;
        return image;
    }

    private static void writeMSOE(Image image, Path path) throws FileNotFoundException {
        final int hexLength = 6;
        PrintWriter writer = new PrintWriter(new File(path.toString()));
        PixelReader reader = image.getPixelReader();

        writer.println("MSOE");
        writer.println((int)image.getWidth() + " " + (int)image.getHeight());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = reader.getColor(x, y);
                String pixelHex = "#" + Integer.toHexString(
                        pixelColor.hashCode()).substring(0, hexLength).toUpperCase();
                writer.print(pixelHex + "  ");
            }
            writer.println();
        }

        writer.close();
    }

    private static String getExtension(Path path) {
        String extension = "";

        int i = path.toString().lastIndexOf('.');
        if (i > 0) {
            extension = path.toString().substring(i+1);
        }

        return extension;
    }
}
