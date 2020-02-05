/*
 * Course:     SE 2811
 * Term:       Winter 2019-20
 * Assignment: Lab 4: Decorators
 * Author:     Dr. Yoder and David Schulz
 * Date:       1/16/2020
 */
package schulzd;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public abstract class NetworkLayer implements Network {
    private static double RADIUS = 20;
    private static int COL_SPREAD = 150;
    private static int ROW_SPREAD = 80;

    private NetworkLayer inputLayer;
    private NetworkLayer previousLayer;
    private boolean isConvo;
    private int numNodes;
    private int numLayers;
    private ArrayList<Node> nodes;

    public NetworkLayer(NetworkLayer previousLayer, int numNodes) {
        this.previousLayer = previousLayer;
        this.numNodes = numNodes;
        if (this.numNodes == -1) {
            isConvo = true;
            this.numNodes = previousLayer.numNodes;
        }
        if (previousLayer == null) {
            inputLayer = this;
            numLayers = 1;
        } else {
            inputLayer = previousLayer.inputLayer;
            numLayers = previousLayer.numLayers + 1;
        }
        createNodes();
    }

    public void createNodes() {
        nodes = new ArrayList<>();
        for (int i = 1; i <= numNodes; i++) {
            int x = COL_SPREAD * numLayers;
            int y = ROW_SPREAD * i;
            Node node = new Node(x, y);
            nodes.add(node);
        }
    }

    public void draw(Canvas canvas) {
        NetworkLayer current = this;
        while (current != null) {
            for (int i = 0; i < current.numNodes; i++) {
                Node node = current.nodes.get(i);
                drawNode(canvas, node);

                if (current.isConvo) {
                    Node receptive = current.previousLayer.nodes.get(i);
                    drawEdge(canvas, receptive, node);
                } else if (current.previousLayer != null) {
                    ArrayList<Node> previousNodes = current.previousLayer.nodes;
                    for (Node prevNode : previousNodes) {
                        drawEdge(canvas, prevNode, node);
                    }
                }
            }

            current = current.previousLayer;
        }
    }

    /**
     * Draw a node centered on the given location.
     *
     * @param canvas canvas on which to draw this
     * @param node the node
     */
    public void drawNode(Canvas canvas, Node node) {
        canvas.getGraphicsContext2D().strokeOval(node.getX()-RADIUS,node.getY()-RADIUS,2*RADIUS,2*RADIUS);
    }

    /**
     * Draw edge between two nodes.
     *
     * @param canvas canvas on which to draw this
     * @param node1 first node
     * @param node2 second node
     */
    public void drawEdge(Canvas canvas, Node node1, Node node2) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        Point2D p1 = new Point2D(node1.getX(), node1.getY());
        Point2D p2 = new Point2D(node2.getX(), node2.getY());
        Point2D direction = p2.subtract(p1).normalize();
        Point2D radius = direction.multiply(RADIUS);
        Point2D start = p1.add(radius);
        Point2D end = p2.subtract(radius);
        context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public int inputSize() {
        return inputLayer.nodes.size();
    }

    public int outputSize() {
        return nodes.size();
    }

    public int numLayers() {
        return numLayers;
    }
}
