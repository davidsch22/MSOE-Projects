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
 * Stores the specific information of a book reference
 * @author David Schulz
 */
public class Book extends Reference {
    private String publisher;

    /**
     * The Book constructor
     * @param author The book author
     * @param title The book title
     * @param publicationYear The book publication year
     * @param publisher The book publisher
     */
    public Book(String author, String title, int publicationYear, String publisher) {
        super(author, title, publicationYear);
        this.publisher = publisher;
    }

    /**
     * Get the book info in proper BibTeX format
     * @return The string containing the info
     */
    public String toString() {
        return "@BOOK { " + getMyUniqueID() + ",\n" +
                "author = \"" + getAuthor() + "\",\n" +
                "title = \"" + getTitle() + "\",\n" +
                "publisher = \"" + getPublisher() + "\",\n" +
                "year = \"" + getPublicationYear() + "\"\n" +
                "}\n";
    }

    /**
     * Prompt the user to input the updated info about the book
     * @param in The scanner for user input
     */
    public void promptForUpdate(Scanner in) {
        super.promptForUpdate(in);

        System.out.println("Enter the updated publisher for the book.");
        publisher = in.nextLine();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
