import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class SequenceSum {

    /**
     * Compute all n and m values for the sequence sum with a given k value as the sum
     * @param k - the required sum of all values from n to m
     */
    public static void sequenceSum(int k) {
        for (int n = 0; n <= k; n++) {
            int sum = 0;
            for (int m = n; m <= k; m++) {
                sum += m;
                if (sum == k) {
                    System.out.println(n + " " + m);
                }
                if (sum >= k) {
                    break;
                }
            }
        }
    }

    public static int generateTestCase() {
        Random rand = new Random();
        return rand.nextInt((int)1e9) + 1; // 1 to 10^9
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int k = Integer.parseInt(in.readLine());

        long start = System.nanoTime();
        sequenceSum(k);
        long runtime = System.nanoTime() - start;
        Runtime rt = Runtime.getRuntime();
        long usedMiB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        System.out.println("Runtime (ns): " + runtime);
        System.out.println("Memory Usage (MiB): " + usedMiB);

        System.out.println("Generated test case: " + generateTestCase());
    }
}
