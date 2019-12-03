/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 6 - Exceptions
 * Name: David Schulz
 * Created: 1/15/19
 */
package schulzd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class for the website info program
 */
public class Lab6 extends Application {

    /**
     * JavaFX's runner method
     * @param primaryStage The window that the program is displayed on
     * @throws Exception An exception thrown if one occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("lab6.fxml"));
        primaryStage.setTitle("Website Tester");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
