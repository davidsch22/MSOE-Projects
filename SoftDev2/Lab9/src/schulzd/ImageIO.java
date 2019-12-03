/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 9 - Image Manipulator (cont.)
 * Name: David Schulz
 * Created: 2/5/19
 */

package schulzd;

import edu.msoe.cs1021.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
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
        } else if (getExtension(path).equals("bmsoe")) {
            image = readBMSOE(path);
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
        } else if (getExtension(path).equals("bmsoe")) {
            writeBMSOE(image, path);
        } else {
            ImageUtil.writeImage(path, image);
        }
    }

    private static Image readMSOE(Path path) throws IOException {
        Scanner in = new Scanner(path.toFile());

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

    private static Image readBMSOE(Path path) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(path.toFile()));

        final int bmsoeLength = 5;
        String confirm = "";
        for (int i = 0; i < bmsoeLength; i++) {
            confirm += (char)in.read();
        }
        if (!confirm.equals("BMSOE")) {
            throw new IOException("ERROR: .bmsoe file does not contain a .bmsoe image");
        }

        int width = in.readInt();
        int height = in.readInt();

        WritableImage bmsoeImage = new WritableImage(width, height);
        PixelWriter writer = bmsoeImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelInt = in.readInt();
                Color pixelColor = intToColor(pixelInt);
                writer.setColor(x, y, pixelColor);
            }
        }

        in.close();

        image = bmsoeImage;
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

    private static void writeBMSOE(Image image, Path path) throws IOException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(path.toString()));
        PixelReader reader = image.getPixelReader();

        out.write('B');
        out.write('M');
        out.write('S');
        out.write('O');
        out.write('E');
        out.writeInt((int)image.getWidth());
        out.writeInt((int)image.getHeight());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = reader.getColor(x, y);
                int pixelInt = colorToInt(pixelColor);
                out.writeInt(pixelInt);
            }
        }

        out.close();
    }

    private static String getExtension(Path path) {
        String extension = "";

        int i = path.toString().lastIndexOf('.');
        if (i > 0) {
            extension = path.toString().substring(i+1);
        }

        return extension;
    }

    private static Color intToColor(int color) {
        double red = ((color >> 16) & 0x000000FF)/255.0;
        double green = ((color >> 8) & 0x000000FF)/255.0;
        double blue = (color & 0x000000FF)/255.0;
        double alpha = ((color >> 24) & 0x000000FF)/255.0;
        return new Color(red, green, blue, alpha);
    }

    private static int colorToInt(Color color) {
        int red = ((int)(color.getRed()*255)) & 0x000000FF;
        int green = ((int)(color.getGreen()*255)) & 0x000000FF;
        int blue = ((int)(color.getBlue()*255)) & 0x000000FF;
        int alpha = ((int)(color.getOpacity()*255)) & 0x000000FF;
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }
}
