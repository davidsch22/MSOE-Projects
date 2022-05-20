import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Binary Search Tree - 'solution' gives the correct answer but causes a TLE
 */
public class BinarySearchTree {
    private BinarySearchTreeNode root;

    /**
     * Constructor for a binary search tree
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Insert a value into the tree
     * @param value - value to store
     * @return the hight of the stored node
     */
    public int insert(int value) {
        int depth = 0;

        // Root is null, first insertion
        if(root == null) {
            root = new BinarySearchTreeNode(value);
            return depth;
        }
        BinarySearchTreeNode node = root;

        // Find the correct place for the node and add it
        while(true) {
            depth++;
            if(value < node.value) {
                if(node.leftChild == null) {
                    node.leftChild = new BinarySearchTreeNode(value);
                    return depth;
                }
                node = node.leftChild;
            } else {
                if(node.rightChild == null) {
                    node.rightChild = new BinarySearchTreeNode(value);
                    return depth;
                }
                node = node.rightChild;
            }
        }
    }

    /**
     * Static internal class for a binary search tree node
     */
    static class BinarySearchTreeNode {
        public int value;
        public BinarySearchTreeNode leftChild;
        public BinarySearchTreeNode rightChild;

        public BinarySearchTreeNode(int v) {
            value = v;
            leftChild = null;
            rightChild = null;
        }
    }

    /**
     * Main routine to solve the problem 'binary search tree'
     *    https://open.kattis.com/problems/bst
     *    Causes a TLE
     * @param args - command line args - not used
     * @throws Exception - I/O related
     */
    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
        PrintWriter out = new PrintWriter(System.out, false);

        int count = Integer.parseInt(in.readLine());

        BinarySearchTree myTree = new BinarySearchTree();

        int runningHeight = 0;
        for(int i = 0; i < count; i++) {
            runningHeight += myTree.insert(Integer.parseInt(in.readLine()));
            out.println(runningHeight);
        }
        long runtime = System.nanoTime() - start;
        out.println("Runtime: " + runtime + " ns");
        out.close();
    }
}
