/*
 * Course: CS1011
 * Winter 2019
 * Lab 5 - Game of Life
 * Name: David Schulz
 * Created: 1/9/2019
 */

package schulzd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The runner class for the Game of Life program
 */
public class Lab5 extends Application {

    /**
     * The start method for JavaFX to run
     * @param primaryStage The window the program is displayed on
     * @throws Exception The exception thrown if unsuccessful
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("lab5.fxml"));
        primaryStage.setTitle("The Game of Life");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getRoot().requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
