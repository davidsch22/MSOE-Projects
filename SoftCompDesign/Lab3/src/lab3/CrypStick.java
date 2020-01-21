/**
 * Course: SE 2811
 * Winter 2019-2020
 * Lab 3 - Strategy-based Encryption
 * Name: David Schulz
 * Created: 12/19/19
 */

package lab3;

import java.nio.charset.StandardCharsets;

/**
 * Handles the media and encryption strategy
 */
public class CrypStick {
    private Media media;
    private Encrypter encrypter;

    public CrypStick() {

    }

    protected Media getMedia() {
        return media;
    }

    public void setEncryptionStrategy(Encrypter encrypter) {
        this.encrypter = encrypter;
    }

    public Encrypter getEncrypter() {
        return encrypter;
    }

    public void setMessage(String message) {
        media = new Media(message);
    }

    public String getMessage() {
        return new String(media.get(), StandardCharsets.UTF_8);
    }
}
