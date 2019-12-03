/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 5 - Buffered IO
 * Name: David Schulz
 * Created: 4/3/19
 */

package msoe.schulzd.lab5;

import java.io.IOException;
import java.io.OutputStream;

/**
 * BufferedOutputStream class
 */
public class BufferedOutputStream {
    private OutputStream out;
    private byte[] buffer;
    private int inPos;
    private int outPos;
    private int byteToWrite;
    private int bitsWritten;
    private static final int DEFAULT_SIZE = 32;
    private static final int BITS_IN_BYTE = 8;

    /**
     * Constructor for the class with a default buffer size
     * @param out the OutputStream the buffer sends data to
     * @throws IOException if OutputStream can't be found
     */
    public BufferedOutputStream(OutputStream out) throws IOException {
        this(out, DEFAULT_SIZE);
    }

    /**
     * Constructor for the class
     * @param out the OutputStream the buffer sends data to
     * @param size the size of the buffer array
     * @throws IOException if OutputStream can't be found
     */
    public BufferedOutputStream(OutputStream out, int size) throws IOException {
        this.out = out;
        buffer = new byte[size];

        inPos = 0;
        outPos = 0;
        bitsWritten = 0;
    }

    /**
     * Writes the given byte next in the buffer's order
     * Sends the buffer through the OutputStream if full
     * @param b the byte to put into the buffer
     * @throws IOException if OutputStream can't be found
     * @throws IllegalStateException if partial byte is still being written
     */
    public void write(int b) throws IOException, IllegalStateException {
        if (bitsWritten % BITS_IN_BYTE != 0) {
            throw new IllegalStateException();
        }

        if (isFull()) {
            out.write(buffer);
            inPos = 0;
        }
        buffer[inPos] = (byte)b;
        inPos++;
    }

    /**
     * Sends bytes from a given array through the OutputStream
     * @param bytes The array to write from
     * @throws IOException if OutputStream can't be found
     * @throws IllegalStateException if partial byte is still being written
     */
    public void write(byte[] bytes) throws IOException, IllegalStateException {
        for (outPos = 0; outPos < bytes.length; outPos++) {
            if (bytes[outPos] != -1) {
                write(bytes[outPos]);
            }
        }
    }

    /**
     * Sends all bytes in the buffer through the OutputStream
     * @throws IOException if OutputStream can't be found
     * @throws IllegalStateException if partial byte is still being written
     */
    public void flush() throws IOException, IllegalStateException {
        if (bitsWritten % BITS_IN_BYTE != 0) {
            throw new IllegalStateException();
        }

        out.write(buffer);

        inPos = 0;
        outPos = 0;
    }

    /**
     * Writes a bit as part of the next byte in the buffer
     * @param b the next bit in the byte to write
     * @throws IOException if OutputStream can't be found
     */
    public void writeBit(int b) throws IOException {
        if (bitsWritten == 0) {
            bitsWritten++;
            byteToWrite = b << BITS_IN_BYTE - bitsWritten;
        } else if (bitsWritten < BITS_IN_BYTE - 1) {
            bitsWritten++;
            byteToWrite = (b << BITS_IN_BYTE - bitsWritten) | byteToWrite;
        } else {
            byteToWrite = b | byteToWrite;
            write(byteToWrite);
            bitsWritten = 0;
        }
    }

    private boolean isFull() {
        return inPos == buffer.length;
    }
}
