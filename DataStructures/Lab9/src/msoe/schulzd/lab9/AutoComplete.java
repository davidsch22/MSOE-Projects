/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 9 - Autocomplete Revisited
 * Name: David Schulz
 * Created: 5/9/19
 */

package msoe.schulzd.lab9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main runner class
 */
public class AutoComplete extends Application {
    /**
     * JavaFX's start method
     * @param primaryStage The window the GUI is run on
     * @throws Exception Thrown if any exception gets this far
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("autocomplete.fxml"));
        primaryStage.setTitle("Auto Complete");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
