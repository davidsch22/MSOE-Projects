/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Lab 4: Decorators
 * Author:     Dr. Yoder and David Schulz
 * Date:       1/16/2020
 */
package schulzd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import java.util.HashMap;
import java.util.Map;

/**
 * The controller for the main window.
 *
 * Also manages the networks.
 */
public class Controller {
    @FXML
    private ToggleGroup network;
    @FXML
    private Canvas canvas;

    private Map<String, Network> networks = new HashMap<>();

    @FXML
    private void showNetwork(ActionEvent actionEvent) {
        ToggleButton source = (ToggleButton)actionEvent.getSource();
        String id = source.getId();
        System.out.println("id = " + id);
        // Clear Canvas: https://stackoverflow.com/q/27203671/1048186
        GraphicsContext context = canvas.getGraphicsContext2D();
        System.out.println("canvas.getWidth() = " + canvas.getWidth());
        System.out.println("canvas.getHeight() = " + canvas.getHeight());
        context.setLineWidth(3);
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(!networks.containsKey(id)) {
            System.out.println("Warning: Unknown network id:"+id);
        } else {
            System.out.println("DEBUG: Drawing network: "+id);
            Network network = networks.get(id);
            network.draw(canvas);
        }
    }

    @FXML
    private void initialize() {
        networks.put("alexLike", createAlexNet());
        networks.put("inceptionLike", createInception());
        networks.put("custom", createMyNet());
    }

    /**
     * As client code, use the decorator classes to construct the inception-like network,
     * as described in the lab.
     * @return network The network created.
     */
    private Network createInception() {
        NetworkLayer network = new Layer(3);
        network = new Layer(network);
        network = new Layer(network);
        network = new Layer(network);
        return network;
    }

    /**
     * As client code, use the decorator classes to construct the AlexNet-like network,
     * as described in the lab.
     * @return network The network created.
     */
    private Network createAlexNet() {
        NetworkLayer network = new Layer(4);
        network = new Layer(network);
        network = new Layer(network);
        network = new Layer(network, 4);
        network = new Layer(network, 3);
        return network;
    }

    private Network createMyNet() {
        NetworkLayer network = new Layer(7);
        network = new Layer(network);
        network = new Layer(network, 6);
        network = new Layer(network, 3);
        network = new Layer(network);
        return network;
    }
}
