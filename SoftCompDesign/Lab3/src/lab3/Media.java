/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

/**
 * Holds the message in a byte array
 */
public class Media {
    private byte[] bytes;

    public Media(String message) {
        set(message);
    }

    public byte[] get() {
        return bytes;
    }

    public void set(byte[] bytes) {
        this.bytes = bytes;
    }

    public void set(String str) {
        String[] hex = str.split(" ");
        byte[] messageBytes = new byte[hex.length];
        for (int i = 0; i < hex.length; i++) {
            int dec = Integer.parseInt(hex[i], 16);
            messageBytes[i] = (byte)dec;
        }
        bytes = messageBytes;
    }

    public String toString() {
        String hex = "";
        for (byte b : bytes) {
            hex += String.format("%02x", b) + " ";
        }
        hex = hex.substring(0, hex.length() - 1);
        return hex;
    }
}
