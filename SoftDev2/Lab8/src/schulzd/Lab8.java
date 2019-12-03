/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 8 - Image Manipulator
 * Name: David Schulz
 * Created: 1/30/19
 */

package schulzd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The runner class for the image manipulator
 */
public class Lab8 extends Application {

    /**
     * JavaFX's main runner method
     * @param primaryStage The window the program is displayed on
     * @throws Exception Thrown if an error occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Lab8Controller.fxml"));
        primaryStage.setTitle("Image Manipulator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
