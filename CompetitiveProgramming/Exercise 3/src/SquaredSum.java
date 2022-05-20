/**
 * The squared sum problem
 *
 * Input:
 *    a single value k such that 1 <= k <= 268435456
 * Output:
 *    all values n and m such that n^2 = SUM(1, m) such that n^2 <= k
 *    each set n and m should be printed on their own line separated by a space
 *    The output should be sorted by increasing n
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public class SquaredSum {

    /**
     * Compute the n and m values for the squared sum using a running sum approach
     * @param maxVal - the max value of n^2
     */
    public static void runningSum(long maxVal) {
        long square = 1;
        long squareAdd = 1;
        long summed = 1;
        long nextSumAdd = 2;
        while(squareAdd != maxVal) {
            if(square == summed) {
                System.out.println(squareAdd + " " + (nextSumAdd-1));
                summed += nextSumAdd;
                nextSumAdd += 1;
                square += squareAdd;
                squareAdd += 1;
                square += squareAdd;
            } else if(square < summed) {
                square += squareAdd;
                squareAdd += 1;
                square += squareAdd;
            } else {
                summed += nextSumAdd;
                nextSumAdd += 1;
            }
        }
    }

    /**
     * Compute the n and m values for the squared sum using a binary search approach
     * @param maxVal - the max value of n^2
     */
    public static void binarySearch(long maxVal) {
        long x = 1;
        while (x != maxVal) {
            long sq = x * x;
            long lo = x;
            long hi = 2 * x;
            while (lo + 1 < hi) {
                long mid = (lo + hi) / 2;
                long summation = (mid * (mid + 1)) / 2;
                if (summation > sq) {
                    hi = mid;
                } else {
                    lo = mid;
                    if (summation == sq) break;
                }
            }
            long summation = (lo * (lo + 1)) / 2;

            if (sq == summation) {
                System.out.println(x + " " + lo);
            }
            x += 1;
        }
    }

    public static int generateTestCase() {
        Random rand = new Random();
        return rand.nextInt((int)1e9-1) + 2; // 2 to 10^9
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int k = Integer.parseInt(in.readLine());

        long start = System.nanoTime();
        runningSum(k);
        long runtime = System.nanoTime() - start;
        Runtime rt = Runtime.getRuntime();
        long usedMiB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        System.out.println("runningSum Runtime (ns): " + runtime);
        System.out.println("runningSum Memory Usage (MiB): " + usedMiB);

        start = System.nanoTime();
        binarySearch(k);
        runtime = System.nanoTime() - start;
        rt = Runtime.getRuntime();
        usedMiB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        System.out.println("binarySearch Runtime (ns): " + runtime);
        System.out.println("binarySearch Memory Usage (MiB): " + usedMiB);

        System.out.println("Generated test case: " + generateTestCase());
    }
}
