import java.util.Arrays;
import java.util.Random;

public class BigOh {

    // Interpreting Algorithms - Question 1
    public int findIt1(int[] array) {
        int min = Integer.MAX_VALUE;
        for (int value : array) {
            if (min > value) {
                min = value;
            }
        }
        return min;
    }

    // Interpreting Algorithms - Question 2
    public int sumIt1(int[] array) {
        int total = 0;
        for (int value : array) {
            total += value;
        }
        return total;
    }

    // Interpreting Algorithms - Question 3
    public int[] allSum1(int[] array) {
        int[] results = new int[array.length];
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j <= i; j++) {
                results[i] += array[j];
            }
        }
        return results;
    }

    // Interpreting Algorithms - Question 4
    public int[] allSum2(int[] array) {
        int[] results = new int[array.length];
        results[0] = array[0];
        for(int i = 1; i < array.length; i++) {
            results[i] = results[i-1] + array[i];
        }
        return results;
    }

    // Interpreting Algorithms - Question 5
    public void sortIt1(int[] array) {
        for(int i = 0; i < array.length; i++) {
            int smallest = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[smallest] > array[j]) {
                    smallest = j;
                }
            }
            int temp = array[i];
            array[i] = array[smallest];
            array[smallest] = temp;
        }
    }

    // Interpreting Algorithms - Question 6
    public void sortIt2(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int value : array) {
            if (max < value) {
                max = value;
            }
        }
        int[] counts = new int[max+1];
        for (int value : array) {
            counts[value]++;
        }
        int resultIndex = 0;
        for(int i = 0; i < counts.length; i++) {
            for(int j = 0; j < counts[i]; j++) {
                array[resultIndex] = i;
                resultIndex++;
            }
        }
    }

    /**
     * buildArray - builds an array of random integers of a specified size
     * @param size - size (number of elements) to create
     * @return array of random integer each in the range of 1-1000
     */
    public int[] buildArray(int size) {
        if(size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        int[] array = new int[size];
        Random r = new Random();
        for(int i = 0; i < array.length; i++) {
            array[i] = r.nextInt(1000 - 1) + 1;
        }
        return array;
    }

    /**
     * Java "Arrays" library function to find the smallest number in a list
     * @param array - the array to search
     * @return index of the smallest element
     */
    public int libFindIt(int[] array) {
        return  Arrays.stream(array).min().getAsInt();
    }

    /**
     * Java "Arrays" library function to sum a list
     * @param array - the array to sum
     * @return sum of all the elements in the array
     */
    public int libSumIt(int[] array) {
        return Arrays.stream(array).sum();
    }

    /**
     * Java library function to sort an array
     * NOTE: This performs an in-place sort
     * @param array - array to sort
     */
    public void libSortIt(int[] array) {
        Arrays.sort(array);
    }

    public static void main(String[] args) {
        BigOh main = new BigOh();

        for (int i = 10; i <= 100000; i *= 10) {
            int[] array = main.buildArray(i);
            long start = System.nanoTime();

            main.findIt1(array);
            // main.libFindIt(array);
            // main.sumIt1(array);
            // main.libSumIt(array);
            // main.allSum1(array);
            // main.allSum2(array);
            // main.sortIt1(array);
            // main.sortIt2(array);
            // main.libSortIt(array);

            long runtime = System.nanoTime() - start;
            Runtime rt = Runtime.getRuntime();
            long usedMiB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
            System.out.println(i + " Runtime (Nanoseconds): " + runtime);
            System.out.println(i + " Memory Usage (MiB): " + usedMiB);
        }
    }
}
