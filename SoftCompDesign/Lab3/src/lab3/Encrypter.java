/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

/**
 * Makes sure every encryption strategy uses these two methods
 */
public interface Encrypter {
    byte[] decrypt(byte[] bytes);
    byte[] encrypt(byte[] bytes);
}
