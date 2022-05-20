import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.TreeMap;

public class BST {
    /**
     * Main routine to solve the problem 'binary search tree'
     *    https://open.kattis.com/problems/bst
     *    Causes a TLE
     * @param args - command line args - not used
     * @throws Exception - I/O related
     */
    public static void main(String[] args) throws Exception {
        // long start = System.nanoTime();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
        PrintWriter out = new PrintWriter(System.out, false);

        int n = Integer.parseInt(in.readLine());

        TreeMap<Integer, Integer> nodes = new TreeMap<>();

        Integer floor, ceil, depth;
        long totalDepth = 0; // Running total

        for (int i = 0; i < n; i++) {
            Integer num = Integer.parseInt(in.readLine());

            floor = nodes.floorKey(num); // Parent node if inserted on the right
            ceil = nodes.ceilingKey(num); // Parent node if inserted on the left

            if (floor == null) {
                if (ceil == null) {
                    depth = 0; // Empty tree
                } else {
                    depth = nodes.get(ceil) + 1; // Only ceil has a value
                }
            } else if (ceil == null) {
                depth = nodes.get(floor) + 1; // Only floor has a value
            } else {
                depth = Math.max(nodes.get(floor), nodes.get(ceil)) + 1; // The one with the larger depth is where it will be inserted
            }

            nodes.put(num, depth); // Store new node's depth in the map
            totalDepth += depth;
            out.println(totalDepth);
        }
        // long runtime = System.nanoTime() - start;
        // out.println("Runtime: " + runtime + " ns");
        out.close();
    }
}
