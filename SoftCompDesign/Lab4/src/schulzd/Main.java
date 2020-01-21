/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Lab 4: Decorators
 * Author: Dr. Yoder and _______
 * Date:
 */
package schulzd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("neuralNetwork.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 850, 650));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
