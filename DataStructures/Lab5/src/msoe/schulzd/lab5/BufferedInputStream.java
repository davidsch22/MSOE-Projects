/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 5 - Buffered IO
 * Name: David Schulz
 * Created: 4/3/19
 */

package msoe.schulzd.lab5;

import java.io.IOException;
import java.io.InputStream;

/**
 * BufferedInputStream class
 */
public class BufferedInputStream {
    private InputStream in;
    private byte[] buffer;
    private int inPos;
    private int outPos;
    private int byteRead;
    private int bitsRead;
    private static final int DEFAULT_SIZE = 32;
    private static final int BITS_IN_BYTE = 8;

    /**
     * Constructor for the class with a default buffer size
     * @param in the InputStream the buffer gets data from
     * @throws IOException if InputStream can't be found
     */
    public BufferedInputStream(InputStream in) throws IOException {
        this(in, DEFAULT_SIZE);
    }

    /**
     * Constructor for the class
     * @param in the InputStream the buffer gets data from
     * @param size the size of the buffer array
     * @throws IOException if InputStream can't be found
     */
    public BufferedInputStream(InputStream in, int size) throws IOException {
        this.in = in;
        buffer = new byte[size];

        inPos = 0;
        outPos = 0;

        inPos = this.in.read(buffer);

        bitsRead = 0;
    }

    /**
     * Reads the next byte in the buffer's order
     * Refills the buffer if empty
     * @return the next byte in the buffer
     * @throws IOException if InputStream can't be found
     * @throws IllegalStateException if partial byte is still being read
     */
    public int read() throws IOException, IllegalStateException {
        if (bitsRead % BITS_IN_BYTE != 0) {
            throw new IllegalStateException();
        }

        if (isEmpty()) {
            inPos = in.read(buffer);
            outPos = 0;
        }
        if (inPos == -1) {
            return -1;
        }

        int result = buffer[outPos];
        outPos++;
        return result;
    }

    /**
     * Fills a given array from the buffer
     * @param bytes The array to fill
     * @return The number of bytes read from the buffer
     * @throws IOException if InputStream can't be found
     * @throws IllegalStateException if partial byte is still being read
     */
    public int read(byte[] bytes) throws IOException, IllegalStateException {
        int numRead = 0;
        int index = 0;

        byteRead = read();
        if (byteRead == -1) {
            return -1;
        }
        numRead++;

        bytes[index] = (byte)byteRead;
        index++;

        while (byteRead != -1 && index < bytes.length) {
            byteRead = read();
            numRead++;
            bytes[index] = (byte)byteRead;
            index++;
        }

        return numRead;
    }

    /**
     * Reads a bit from the next byte in the buffer
     * @return the next unread bit in the byte
     * @throws IOException if InputStream can't be found
     */
    public int readBit() throws IOException {
        int bit;
        if (bitsRead % BITS_IN_BYTE == 0) {
            bitsRead = 0;
            byteRead = (byte)read();
            bit = byteRead & 0b00000001;
        } else {
            bit = (byteRead >> bitsRead) & 0b00000001;
        }
        bitsRead++;
        return bit;
    }

    private boolean isEmpty() {
        return outPos == buffer.length;
    }
}
