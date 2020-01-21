/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

/**
 * Encrypts and decrypts the message by shifting each byte value by a specified amount
 */
public class ShiftEncrypter implements Encrypter {
    private int amount;

    public ShiftEncrypter(int amount) {
        this.amount = amount;
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        byte[] decrypted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int decForm = bytes[i];
            int shifted = decForm - amount;
            if (shifted < 0) {
                shifted = shifted + 256;
            }
            byte shiftedForm = (byte)shifted;
            decrypted[i] = shiftedForm;
        }
        return decrypted;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        byte[] encrypted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int decForm = bytes[i];
            int shifted = decForm + amount;
            if (shifted > 255) {
                shifted = shifted - 256;
            }
            byte shiftedForm = (byte)shifted;
            encrypted[i] = shiftedForm;
        }
        return encrypted;
    }
}
