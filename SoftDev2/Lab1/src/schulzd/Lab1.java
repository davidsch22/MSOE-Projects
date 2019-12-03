/*
 * Course: CS-1021-091
 * Winter 2018
 * Lab 1
 * Name: David Schulz
 * Created: 11/29/18
 */

package schulzd;

import java.util.Scanner;
import java.util.ArrayList;
import edu.msoe.taylor.audio.WavFile;

/**
 * A program that asks the user how they want to manipulate sounds
 */
public class Lab1 {
    private static final int SAMPLE_RATE = 22050;
    private static final int VALID_BITS = 16;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            do {
                System.out.println("Enter 0 to exit, 1 to reverse a sound,");
                System.out.println("2 to make a tone with a frequency,");
                System.out.println("or 3 to make a tone in stereo with 2 frequencies: ");
                choice = input.nextInt();
            } while (choice != 0 && choice != 1 && choice != 2 && choice != 3);

            if (choice == 1) {
                choice1(input);
            } else if (choice == 2) {
                choice2(input);
            } else if (choice == 3) {
                choice3(input);
            }
        }
    }

    /**
     * Reverses the audio of a specified existing audio file
     *
     * @param input the scanner being used
     */
    public static void choice1(Scanner input) {
        System.out.print("Enter the audio file's name (without .wav): ");
        input.nextLine();
        String filename = input.nextLine();
        WavFile audioFile = new WavFile(filename + ".wav");
        ArrayList<Double> samples = audioFile.getSamples();

        ArrayList<Double> samplesReversed = new ArrayList<>();
        for (int i = samples.size() - 1; i >= 0; i--) {
            samplesReversed.add(samples.get(i));
        }

        WavFile audioReversedFile = new WavFile(filename + "Rev.wav",
                audioFile.getNumChannels(),
                audioFile.getNumFrames(),
                audioFile.getValidBits(),
                audioFile.getSampleRate());
        audioReversedFile.setSamples(samplesReversed);

        audioFile.close();
        audioReversedFile.close();
    }

    /**
     * Creates an audio file with a specified tone and frequency
     *
     * @param input the scanner being used
     */
    public static void choice2(Scanner input) {
        System.out.print("Enter the audio file's name (without .wav): ");
        input.nextLine();
        String filename = input.nextLine();

        System.out.print("Now specify a frequency (in Hz): ");
        double frequency = input.nextDouble();

        ArrayList<Double> samples = new ArrayList<>();
        for (int i = 0; i < SAMPLE_RATE; i++) {
            double newSample = Math.sin(2 * Math.PI * i * (frequency / SAMPLE_RATE));
            samples.add(newSample);
        }

        WavFile audioFrequencyFile = new WavFile(filename + ".wav",
                1,
                SAMPLE_RATE,
                VALID_BITS,
                SAMPLE_RATE);
        audioFrequencyFile.setSamples(samples);
        audioFrequencyFile.close();
    }

    /**
     * Creates an audio file that plays two specified frequencies in stereo
     *
     * @param input the scanner being used
     */
    public static void choice3(Scanner input) {
        System.out.print("Enter the audio file's name (without .wav): ");
        input.nextLine();
        String filename = input.nextLine();

        System.out.print("Now specify the first frequency (in Hz): ");
        double frequency1 = input.nextDouble();
        System.out.print("Now specify the second frequency (in Hz): ");
        double frequency2 = input.nextDouble();

        ArrayList<Double> samples = new ArrayList<>();
        for (int i = 0; i < SAMPLE_RATE; i++) {
            double newSample1 = Math.sin(2 * Math.PI * i * (frequency1 / SAMPLE_RATE));
            double newSample2 = Math.sin(2 * Math.PI * i * (frequency2 / SAMPLE_RATE));
            samples.add(newSample1);
            samples.add(newSample2);
        }

        WavFile audioStereoFile = new WavFile(filename + ".wav",
                2,
                SAMPLE_RATE,
                VALID_BITS,
                SAMPLE_RATE);
        audioStereoFile.setSamples(samples);
        audioStereoFile.close();
    }
}
