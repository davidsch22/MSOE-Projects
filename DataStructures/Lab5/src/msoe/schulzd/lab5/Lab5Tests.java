/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 5 - Buffered IO
 * Name: David Schulz
 * Created: 4/3/19
 */

package msoe.schulzd.lab5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Simple program that demonstrates how to read/write from
 * Buffered Input/Output Streams.
 */
public class Lab5Tests {
    private static final byte[] BYTE_ARRAY_128 = byteArray(0, 127);
    private static final byte[] BYTE_ARRAY_126 = byteArray(0, 125);

    public static void main(String[] ignored) {
        try {
            readBytesFromByteArrayInputStream();
            writeBytesToByteArrayOutputStream();
        } catch(IOException e) {
            System.out.println("IOException");
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException");
        }
    }

    /**
     * Reads bytes from a BufferedInputStream.
     * @throws IOException if an IOException is encountered
     * @throws IllegalStateException if a partial byte is still being read
     */
    private static void readBytesFromByteArrayInputStream() throws
            IOException, IllegalStateException {
        ByteArrayInputStream bin128 = new ByteArrayInputStream(BYTE_ARRAY_128);
        ByteArrayInputStream bin126 = new ByteArrayInputStream(BYTE_ARRAY_126);
        ByteArrayInputStream bin0 = new ByteArrayInputStream(new byte[0]);

        BufferedInputStream in128 = new BufferedInputStream(bin128);
        BufferedInputStream in126 = new BufferedInputStream(bin126);
        BufferedInputStream in0 = new BufferedInputStream(bin0);

        // Size multiple of 8
        byte[] bytesRead128 = new byte[BYTE_ARRAY_128.length];

        // Uncommenting will cause IllegalStateException to be thrown
        //in128.readBit();

        in128.read(bytesRead128);

        // Size not multiple of 8
        byte[] bytesRead126 = new byte[BYTE_ARRAY_126.length];
        in126.read(bytesRead126);

        // Size 0
        byte[] bytesRead0 = new byte[0];
        in0.read(bytesRead0);

        // Confirm that bytes read match what was in the input stream
        if (Arrays.compare(bytesRead128, BYTE_ARRAY_128) != 0) {
            // This will happen
            System.out.println("Error: 128 bytes read don't match");
        } else {
            System.out.println("BufferedInputStream with 128 bytes test successful!");
        }

        if (Arrays.compare(bytesRead126, BYTE_ARRAY_126) != 0) {
            System.out.println("Error: 126 bytes read don't match");
        } else {
            System.out.println("BufferedInputStream with 126 bytes test successful!");
        }

        if (Arrays.compare(bytesRead0, new byte[0]) != 0) {
            System.out.println("Error: 0 bytes read don't match");
        } else {
            System.out.println("BufferedInputStream with 0 bytes test successful!");
        }
    }

    /**
     * Writes bytes to a BufferedOutputStream.
     * @throws IOException if an IOException is encountered
     * @throws IllegalStateException if a partial byte is still being written
     */
    private static void writeBytesToByteArrayOutputStream() throws
            IOException, IllegalStateException {
        ByteArrayOutputStream bout128 = new ByteArrayOutputStream(BYTE_ARRAY_128.length);
        BufferedOutputStream out128 = new BufferedOutputStream(bout128);

        // Write bytes to output stream
        out128.write(BYTE_ARRAY_128);
        out128.flush();

        // Get bytes out of the output stream byte array
        byte[] bytesWritten = bout128.toByteArray();

        // Confirm that bytes in the output stream match what was written
        if (Arrays.compare(bytesWritten, BYTE_ARRAY_128) != 0) {
            System.out.println("Error: bytes written don't match");
        } else {
            System.out.println("BufferedOutputStream test successful!");
        }
    }

    private static byte[] byteArray(int lower, int upper) {
        int size = Math.abs(upper - lower + 1);
        byte[] array = new byte[size];
        int j = lower;
        for (int i = 0; i < size; i++, j++) {
            array[i] = (byte)j;
        }
        return array;
    }
}

/*
 * Partner: Daniel Kaehn
 * - Create a BufferedInputStream with an array size multiple of 8
 * --- Array has 128 elements
 * - Create a BufferedInputStream with an array size not multiple of 8
 * --- Array has 126 elements
 * - Create a BufferedInputStream with an array size of 0
 * --- Array is empty
 * - Throw IllegalStateException
 * --- Call readBit() once and then call read()
 */
