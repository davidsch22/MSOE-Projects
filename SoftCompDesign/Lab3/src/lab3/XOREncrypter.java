/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

/**
 * Encrypts and decrypts the message by using the exclusive-or operator with a specified key
 */
public class XOREncrypter implements Encrypter {
    private byte[] key;

    public XOREncrypter(byte[] key) {
        this.key = key;
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        byte[] decrypted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int keyIndex = i % key.length;
            decrypted[i] = (byte)(bytes[i]^key[keyIndex]);
        }
        return decrypted;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        byte[] encrypted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int keyIndex = i % key.length;
            encrypted[i] = (byte)(bytes[i]^key[keyIndex]);
        }
        return encrypted;
    }
}
