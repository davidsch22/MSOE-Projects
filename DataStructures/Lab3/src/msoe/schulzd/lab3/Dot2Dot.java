/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 3 - Connect the Dots Generator Revisited
 * Name: David Schulz
 * Created: 3/21/19
 */

package msoe.schulzd.lab3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Runner class for the program
 */
public class Dot2Dot extends Application {
    /**
     * The JavaFX runner method
     * @param primaryStage The window the program runs on
     * @throws Exception Thrown if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Dot2Dot.fxml"));
        primaryStage.setTitle("Dot to Dot");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
