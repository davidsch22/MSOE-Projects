/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

/**
 * Encrypts and decrypts the message by reversing the order of bytes
 */
public class ReverseEncrypter implements Encrypter {
    public ReverseEncrypter() {

    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversed[i] = bytes[bytes.length-1 - i];
        }
        return reversed;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversed[i] = bytes[bytes.length-1 - i];
        }
        return reversed;
    }
}
