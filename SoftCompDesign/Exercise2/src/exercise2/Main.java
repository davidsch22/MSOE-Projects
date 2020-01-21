package exercise2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int num = in.nextInt();
        System.out.println(num + " ** 4 = " + (num * num * num * num));
    }
}
