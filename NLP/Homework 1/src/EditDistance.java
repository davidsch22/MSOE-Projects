import java.util.Scanner;

public class EditDistance {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Source word: ");
        String source = in.nextLine();
        System.out.print("Target word: ");
        String target = in.nextLine();

        System.out.println("Edit Distance: " + minEditDistance(source, target));
    }

    private static int minEditDistance(String source, String target) {
        final int INS_COST = 1;
        final int DEL_COST = 1;
        int subCost = 2;

        int n = source.length();
        int m = target.length();
        int[][] D = new int[n+1][m+1];

        // Initialization: the zeroth row and column is the distance from the empty string
        D[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            D[i][0] = D[i-1][0] + DEL_COST;
        }
        for (int j = 1; j <= m; j++) {
            D[0][j] = D[0][j-1] + INS_COST;
        }

        // Recurrence relation:
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (source.charAt(i-1) == target.charAt(j-1)) {
                    subCost = 0;
                } else {
                    subCost = 2;
                }
                D[i][j] = Math.min(Math.min(D[i-1][j] + DEL_COST, D[i-1][j-1] + subCost), D[i][j-1] + INS_COST);
            }
        }

        // Termination
        return D[n][m];
    }
}
