/*
 * Course: CS2852
 * Spring 2019
 * Lab 7 - Morse Code Decoder
 * Name: David Schulz
 * Created: 4/25/19
 */

package msoe.schulzd.lab7;

/**
 * The MorseTree class
 * @param <E> The data type the nodes will store
 */
public class MorseTree<E> {
    private class Node implements TreePrinter.PrintableNode {
        private E data;
        private Node left;
        private Node right;

        private Node(E symbol) {
            data = symbol;
            left = null;
            right = null;
        }

        public String getText() {
            if (data != null) {
                return data.toString();
            }
            return "null";
        }

        private void setData(E symbol) {
            data = symbol;
        }

        private E getData() {
            return data;
        }

        private void setLeftSubtree(Node left) {
            this.left = left;
        }

        private void setRightSubtree(Node right) {
            this.right = right;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }
    }

    private Node root;

    /**
     * Default constructor
     */
    public MorseTree() {
        root = new Node(null);
    }

    /**
     * Adds a new symbol to the tree and places it depending on its morse code equivalent
     * @param symbol The character being classified
     * @param code The morse code equivalent of the symbol
     * @throws IllegalArgumentException if code passed is not a morse code char
     */
    public void add(E symbol, String code) throws IllegalArgumentException {
        add(root, symbol, code);
    }

    private void add(Node current, E symbol, String code) throws IllegalArgumentException {
        if (code.length() == 0) {
            current.setData(symbol);
            return;
        }

        String firstCode = code.substring(0, 1);
        String remainingCode;

        if (code.length() == 1) {
            remainingCode = "";
        } else {
            remainingCode = code.substring(1);
        }

        if (firstCode.equals(".")) {
            if (current.getLeft() == null) {
                current.setLeftSubtree(new Node(null));
            }
            add(current.getLeft(), symbol, remainingCode);
        } else if (firstCode.equals("-")) {
            if (current.getRight() == null) {
                current.setRightSubtree(new Node(null));
            }
            add(current.getRight(), symbol, remainingCode);
        } else {
            throw new IllegalArgumentException(firstCode);
        }
    }

    /**
     * Translates given morse code into a symbol/character
     * @param code The given morse code
     * @return The equivalent symbol
     */
    public E decode(String code) {
        if (code.length() == 0) {
            return null;
        } else {
            try {
                return decode(root, code);
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Skipping " + e.getMessage());
                return null;
            }
        }
    }

    private E decode(Node current, String code) throws IllegalArgumentException {
        if (code.length() == 0) {
            return current.getData();
        }

        String firstCode = code.substring(0, 1);
        String remainingCode;

        if (code.length() == 1) {
            remainingCode = "";
        } else {
            remainingCode = code.substring(1);
        }

        if (firstCode.equals(".")) {
            return decode(current.getLeft(), remainingCode);
        } else if (firstCode.equals("-")) {
            return decode(current.getRight(), remainingCode);
        } else {
            throw new IllegalArgumentException(firstCode);
        }
    }

    public Node getRoot() {
        return root;
    }
}