package msoe.schulzd.lab7;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary tree printer
 *
 * Useful for testing with small trees
 *
 * I didn't make this.
 *
 * @author MightyPork from StackOverflow
 */
public class TreePrinter {

    /** Node that can be printed */
    public interface PrintableNode {

        /** Get left child */
        PrintableNode getLeft();

        /** Get right child */
        PrintableNode getRight();

        /** Get text to be printed */
        String getText();
    }

    /**
     * Print a tree
     *
     * @param root tree root node
     */
    public static void print(PrintableNode root) {
        System.out.println(TreePrinter.toString(root));
    }

    public static String toString(PrintableNode root) {
        List<List<String>> lines = new ArrayList<>();

        List<PrintableNode> level = new ArrayList<>();
        List<PrintableNode> next = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<>();

            nn = 0;

            for (PrintableNode n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.getText();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.getLeft());
                    next.add(n.getRight());

                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<PrintableNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    sb.append(c);
//                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            sb.append(' ');
//                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            sb.append(j % 2 == 0 ? ' ' : '─');
//                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        sb.append(j % 2 == 0 ? '┌' : '┐');
//                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            sb.append(j % 2 == 0 ? '─' : ' ');
//                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                sb.append('\n');
//                System.out.println();
            }

            // print line of numbers
            for (String f : line) {

                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    sb.append(' ');
//                    System.out.print(" ");
                }
                sb.append(f);
//                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    sb.append(' ');
//                    System.out.print(" ");
                }
            }
            sb.append('\n');
//            System.out.println();

            perpiece /= 2;
        }
        return sb.toString();
    }
}