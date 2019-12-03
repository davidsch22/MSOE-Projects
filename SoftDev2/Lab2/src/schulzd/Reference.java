/*
 * Course: CS1021
 * Winter 2018
 * Lab: Lab2
 * Name: David Schulz
 * Created: 12/6/18
 */

package schulzd;

import java.util.Scanner;

/**
 * Stores the general information of a reference
 * @author David Schulz
 */
public class Reference {
    private static int instanceCount = 0;
    private String author;
    private String myUniqueID;
    private int publicationYear;
    private String title;

    /**
     * The Reference constructor
     * Corrects user input if incorrect
     * @param author The reference author
     * @param title The reference title
     * @param publicationYear The reference publication/copyright year
     */
    public Reference(String author, String title, int publicationYear) {
        this.author = author;
        this.title = title;
        if (publicationYear < 0) {
            this.publicationYear = 0;
        } else {
            this.publicationYear = publicationYear;
        }
        myUniqueID = "REF" + instanceCount;
        instanceCount++;
    }

    /**
     * Prompt the user to input the updated info about the reference
     * @param in The scanner for user input
     */
    public void promptForUpdate(Scanner in) {
        System.out.println("Enter the updated author of the reference");
        author = in.nextLine();

        System.out.println("Enter the updated title of the reference");
        title = in.nextLine();

        boolean isValid = false;
        while(!isValid) {
            System.out.println("Enter the updated copyright year for the reference.");
            if (!in.hasNextInt()) {
                in.next();
            } else {
                publicationYear = in.nextInt();
                if (publicationYear >= 0) {
                    isValid = true;
                }
            }
        }
        in.nextLine();
    }

    public String getAuthor() {
        return author;
    }

    public String getMyUniqueID() {
        return myUniqueID;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
