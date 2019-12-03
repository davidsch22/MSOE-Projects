/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 9 - Image Manipulator (cont.)
 * Name: David Schulz
 * Created: 2/5/19
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
public class Lab9 extends Application {
    /**
     * JavaFX's main runner method
     * @param primaryStage The window the program is displayed on
     * @throws Exception Thrown if an error occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Lab9Controller.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Image Manipulator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        FXMLLoader kernelLoader = new FXMLLoader();
        kernelLoader.setLocation(getClass().getResource("KernelController.fxml"));
        Parent kernelRoot = kernelLoader.load();
        Stage kernelStage = new Stage();
        kernelStage.setTitle("Filter Kernel");
        kernelStage.setScene(new Scene(kernelRoot));

        Lab9Controller controller = loader.getController();
        controller.setKernelStage(kernelStage);
        KernelController kernelController = kernelLoader.getController();
        kernelController.setMainController(controller);
        kernelController.setLogger(controller.getLogger());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
