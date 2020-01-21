/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Lab 4: Decorators
 * Author: Dr. Yoder and _______
 * Date:
 */
package schulzd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.HashMap;
import java.util.List;
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

    /** TODO: PLEASE PLEASE find a new home for me! I don't belong as a responsibility of the
     * controller! */
    private static final double RADIUS = 40;

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

            //TODO: Delete this example code
            drawNode(canvas, 100,100);
            drawNode(canvas, 300,400);
            drawEdge(canvas, 100, 100, 300, 400);
            //ENDTODO

            network.draw(canvas);
        }
    }

    @FXML
    private void initialize() {
        networks.put("alexLike",createAlexNet());
        networks.put("inceptionLike",createInception());
    }

    /**
     * As client code, use the decorator classes to construct the inception-like network,
     * as described in the lab.
     * @return network The network created.
     */
    private Network createInception() {
        return null; // TODO return the network
    }

    /**
     * As client code, use the decorator classes to construct the AlexNet-like network,
     * as described in the lab.
     * @return network The network created.
     */
    private Network createAlexNet() {
        return null; // TODO return the network
    }


    /**
     * Draw a node centered on the given location.
     * @param canvas canvas on which to draw this.
     * @param x center of the node
     * @param y center of the node
     */
    /* TODO: PLEASE PLEASE find a new home for me.  I don't belong as a responsibility of the
     controller!
     You might even change what parameters I take...
    */
    public static void drawNode(Canvas canvas, int x, int y){
        canvas.getGraphicsContext2D().strokeOval(x-RADIUS,y-RADIUS,2*RADIUS,2*RADIUS);
    }

    /**
     * Draw edge between two nodes.
     *
     * @param canvas
     * @param x1 center of first node
     * @param y1
     * @param x2 center of second node
     * @param y2
     */
    /* TODO: PLEASE PLEASE find a new home for me.  I don't belong as a responsibility of the
     controller!
     You might even change what parameters I take...

     You do NOT need to draw arrows on the edges.
     */
    public static void drawEdge(Canvas canvas, double x1, double y1, double x2, double y2) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        Point2D p1 = new Point2D(x1, y1);
        Point2D p2 = new Point2D(x2, y2);
        Point2D direction = p2.subtract(p1).normalize();
        Point2D radius = direction.multiply(RADIUS);
        Point2D start = p1.add(radius);
        Point2D end = p2.subtract(radius);
        context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }
}
