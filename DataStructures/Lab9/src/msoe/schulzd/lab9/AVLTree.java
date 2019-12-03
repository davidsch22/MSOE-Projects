/*
 * Course: CS2852
 * Spring 2019
 * Name: David Schulz
 * Created 5/9/2019
 */

package msoe.schulzd.lab9;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Node of an AVL tree
 *
 * @author David Schulz
 * @version 5/9/2019
 */
class AVLNode<E extends Comparable<E>> implements TreePrinter.PrintableNode {
    AVLNode<E> right;
    AVLNode<E> left;
    E data;
    int height;

    /**
     * Constructor puts data into the node
     */
    public AVLNode(E data) {
        left = null;
        right = null;
        this.data = data;
        height = 0;
    }

    public AVLNode<E> getLeft() {
        return left;
    }

    public AVLNode<E> getRight() {
        return right;
    }

    public String getText() {
        return data.toString();
    }
}

/**
 * The AVL tree class
 * @param <E> Any data type that is comparable
 */
public class AVLTree<E extends Comparable<E>> {
    AVLNode<E> root;

    /**
     * Default constructor
     */
    public AVLTree(){
        super();
    }

    private int height(AVLNode node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    /**
     * Adds a new node with the given data and then balances the tree if necessary
     * @param data The data for the new node
     */
    public void add(E data) {
        root = add(data, root);
    }

    private AVLNode<E> add(E data, AVLNode<E> current) {
        if (current == null) {
            current = new AVLNode<E>(data);
        } else if (data.compareTo(current.data) < 0) {
            current.left = add(data, current.left);

            if (height(current.left) - height(current.right) > 1) {
                if (data.compareTo(current.left.data) < 0) {
                    current = rotateWithLeftChild(current);
                } else {
                    current = doubleWithLeftChild(current);
                }
            }
        } else if (data.compareTo(current.data) > 0) {
            current.right = add(data, current.right);

            if (height(current.right) - height(current.left) > 1) {
                if (data.compareTo(current.right.data) > 0) {
                    current = rotateWithRightChild(current);
                } else {
                    current = doubleWithRightChild(current);
                }
            }
        }

        current.height = Math.max(height(current.left), height(current.right)) + 1;

        return current;
    }

    private AVLNode<E> rotateWithLeftChild(AVLNode<E> node) {
        AVLNode<E> k1 = node.left;
        node.left = k1.right;
        k1.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        k1.height = Math.max(height(k1.left), node.height) + 1;
        return k1;
    }

    private AVLNode<E> rotateWithRightChild(AVLNode<E> node) {
        AVLNode<E> k2 = node.right;
        node.right = k2.left;
        k2.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        k2.height = Math.max(height(k2.right), node.height) + 1;
        return k2;
    }

    private AVLNode<E> doubleWithLeftChild(AVLNode<E> node) {
        node.left = rotateWithRightChild(node.left);
        return rotateWithLeftChild(node);
    }

    private AVLNode<E> doubleWithRightChild(AVLNode<E> node) {
        node.right = rotateWithLeftChild(node.right);
        return rotateWithRightChild(node);
    }

    public List<String> getAllWordsInOrder() {
        return getWordsInOrder(root);
    }

    private List<String> getWordsInOrder(AVLNode<E> node) {
        List<String> allWords = new ArrayList<>();

        if (node != null) {
            allWords.addAll(getWordsInOrder(node.left));
            allWords.add(node.data.toString());
            allWords.addAll(getWordsInOrder(node.right));
        }

        return allWords;
    }

    /**
     * creates a string representation of a tree
     * @return a string of the treee
     */
    @Override
    public String toString(){
        return root.toString();
    }
}
