/*
 * Name: Anthony Lohmiller, Kam Mitchell, David Schulz, Jesse Sierra
 * Date: 14 April 2020
 * Quarter: Spring 2019-2020
 * Class: SE Process
 * Lab: 4
 * Created: 30 March 2020
 */

package msoe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class handles the importing of the Unofficial Transcripts.
 */
public class Transcript {

    private File file; // User selected file.
    private String[] text; // String array used for parsing.
    private List<String> pdf; // The pdf read in.

    /**
     * Constructor for transcript
     *
     * @param file : transcript
     */
    public Transcript(File file) {

        this.file = file;
        text = null;
        pdf = null;
    }

    /**
     * parsing of pdf , deleting all identifiables
     *
     * @throws IOException
     */
    private void parsedPDF(){
        pdfToArray(); // converts pdf to String array seperated by white space
        makeUnknown(); // deletes all private info of the transcript's owner
        deleteClassName(); // deletes the ataul class name and just leaves the course number
        deleteCreditCount(); // deletes all unnecessary credit info
    }

    /**
     * The pdf completely parsed without any identifiable information.
     *
     * @return parsed transcript
     * @throws IOException Thrown if the PDF to be read in is invalid or contains bad information.
     */
    public List<String> getPdf() throws IOException {
        parsedPDF(); // returns the complete parsed pdf
        return this.pdf;
    }

    /**
     * Converts the pdf to a String Array
     *
     * @throws IOException thrown if there is an error converting the PDF to an array.
     */
    private void pdfToArray() {

        PDDocument document; //document to load file into.
        PDFTextStripper pdfStripper; // this will get the text from the pdf.

        try {
            //Load PDF document
            document = PDDocument.load(this.file);

            //Instantiate PDFTextStripper class
            pdfStripper = new PDFTextStripper();

            //store the parsed text into a string array, spliting it by spaces
            this.text = pdfStripper.getText(document).split("\r\n");

            document.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.pdf = new ArrayList<>(Arrays.asList(text)); // list of the standard string array
    }

    /**
     * Method to delete private identifiable (name, ID,  SSN, etc.) from pdf document
     */
    private void makeUnknown() {

        Iterator<String> listIterator = this.pdf.iterator(); // iterator to iterate through pdf since lines of pdf are unknown
        int i = 0; // varaible to determine which line the code is at
        int k = 0; // variable to delete first 4 lines.
        int size = pdf.size(); // size of the pdf

        //Loop to go through the different lines of the pdf
        while (listIterator.hasNext() && i < size) {

            String index = this.pdf.get(i); // the current line of the pdf

            //take out the first 4 lines(identity of person) and any other line that contains SSN.
            if (index.contains("SSN") || k < 4 || index.contains("ID") || index.contains("DATE")) {
                this.pdf.remove(i);
            } else {
                i++; // if it did not take anything out, increment i
            }
            k++;
            size = this.pdf.size(); // update size in case a line was removed

        }
    }

    /**
     * Goes through transcript and deletes the actual class name
     *
     */
    private void deleteClassName() {

        String change = "";

        for (int i = 0; i < this.pdf.size(); i++) {
            String line = this.pdf.get(i); // gets the current line of the pdf

            //Takes out all lines that do not match specif information needed for analyzing a transcript and what a student needs.
            if (line.matches(".*\\d.*")) {
                //If line contains any of these, keep them
                if (line.contains("Total Credits Earned") || line.contains("Major Totals") || line.contains("Quarter")) {
                    change += line + " "; // add a space between each word on the line

                    //if it does not, manipulate the line
                } else {
                    String[] split = line.split(" "); // split the line by spaces
                    for (int j = 0; j < split.length; j++) {

                        String s = split[j]; // gets the individual word

                        //we only want the beginning and end words
                        if (j == 0 || j == split.length - 1) {

                            change += s + " ";  // put a space between the two words
                        }
                    }
                }

            }
            // if the line contained no numbers and none of these key words, just keep it
            else if (line.contains("Milwaukee") || line.contains("BS") || line.contains("Undergraduate")) {

                change += line; // add the entire line
            }
            change += "\r\n"; // put the next line of words on a new line
        }
        //make the change string into a list and store it in the pdf
        this.pdf = Arrays.asList(change);
    }

    /**
     * Goes through and deletes the credit count per class
     *
     */
    private void deleteCreditCount() {

        String change = "";

        String[] split = this.pdf.get(0).split("\r\n"); // split be new line

        for (int i = 0; i < split.length - 1; i++) {
            String line = split[i]; // get the current line

            //if the line doesn't contain these, work will be done on them
            if (!line.contains("Total") && !line.contains("Quarter") && !line.contains("Cumulative") && !line.contains("Term") && !line.contains("Milwaukee") && !line.contains("BS") && !line.contains("Undergraduate")) {


                // split2 will split the line that is being evaluated by the spaces which will allow us to get what we need
                String[] split2 = line.split(" ");
                if (split2.length > 1) {
                    change += split2[0] + ": "; // add a ":" between the parts we need

                    //If statement that decides if the letter grade involves 1 or 2 characters then takes that grade after deciding
                    if (line.substring(line.length() - 3).matches((".*\\d.*"))) {
                        change += line.substring(line.length() - 2);
                    } else {
                        change += line.substring(line.length() - 3);
                    }

                    //add a new line
                    change += "\r\n";
                }


            }
            //if line did not contain a number, and if it also does not contain term (nobody cares for the term total since we have the grades already)
            //then just add the whole line
            else if (!line.contains("Term") && !line.contains("Page")) {
                change += line + "\r\n"; // add the line along with a new line
            }


        }
        // convert change into an array and store it in the pdf.
        this.pdf = Arrays.asList(change);
    }

}