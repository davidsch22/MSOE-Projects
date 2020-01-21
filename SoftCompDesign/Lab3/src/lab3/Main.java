/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Runs the whole program
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("E)ncrypt or D)ecrypt: ");
        String crypt = in.nextLine().toLowerCase();
        while (!crypt.equals("e") && !crypt.equals("d")) {
            System.out.print("Not valid input. E)ncrypt or D)ecrypt: ");
            crypt = in.nextLine().toLowerCase();
        }

        System.out.print("Method (rev, shift, xor): ");
        String method = in.nextLine().toLowerCase();
        while (!method.equals("rev") && !method.equals("shift") && !method.equals("xor")) {
            System.out.print("Not valid input. Method (rev, shift, xor): ");
            method = in.nextLine().toLowerCase();
        }

        int amount = -1;
        if (method.equals("shift")) {
            System.out.print("Shift amount: ");
            amount = in.nextInt();
            in.nextLine();
        }

        String key = "";
        if (method.equals("xor")) {
            System.out.print("Key: ");
            key = in.nextLine();
        }

        System.out.println("Message: ");
        String message = "";
        while (in.hasNextLine()) {
            message += in.nextLine();
            if (crypt.equals("e")) {
                message += "\n";
            }
        }
        in.close();

        CrypStick crypStick = new CrypStick();

        switch (method) {
            case "rev":
                crypStick.setEncryptionStrategy(new ReverseEncrypter());
                break;
            case "shift":
                crypStick.setEncryptionStrategy(new ShiftEncrypter(amount));
                break;
            case "xor":
                crypStick.setEncryptionStrategy(new XOREncrypter(key.getBytes(StandardCharsets.UTF_8)));
        }

        if (crypt.equals("e")) {
            crypStick.setMessage("0");
            crypStick.getMedia().set(message.getBytes(StandardCharsets.UTF_8));
            byte[] encrypted = crypStick.getEncrypter().encrypt(crypStick.getMedia().get());
            crypStick.getMedia().set(encrypted);
            System.out.println(crypStick.getMedia().toString());
        } else {
            crypStick.setMessage(message);
            byte[] decrypted = crypStick.getEncrypter().decrypt(crypStick.getMedia().get());
            crypStick.getMedia().set(decrypted);
            System.out.println(crypStick.getMessage());
        }
    }
}
