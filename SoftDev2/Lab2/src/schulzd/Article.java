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
 * Stores the specific information of an article reference
 * @author David Schulz
 */
public class Article extends Reference {
    private int endingPage;
    private int startingPage;
    private String journal;

    /**
     * The Article constructor
     * Corrects user input if incorrect
     * @param author The article author
     * @param title The article title
     * @param publicationYear The article publication year
     * @param journal The article journal
     * @param startingPage The article's first page
     * @param endingPage The article' last page
     */
    public Article(String author, String title, int publicationYear, String journal,
                   int startingPage, int endingPage) {
        super(author, title, publicationYear);
        this.journal = journal;
        if (startingPage < 1) {
            this.startingPage = 1;
        } else {
            this.startingPage = startingPage;
        }
        if (endingPage < this.startingPage) {
            this.endingPage = this.startingPage;
        } else {
            this.endingPage = endingPage;
        }
    }

    /**
     * Get the article info in proper BibTeX format
     * @return The string containing the info
     */
    public String toString() {
        return "@ARTICLE { " + getMyUniqueID() + ",\n" +
                "author = \"" + getAuthor() + "\",\n" +
                "title = \"" + getTitle() + "\",\n" +
                "journal = \"" + getJournal() + "\",\n" +
                "pages = \"" + getStartingPage() + "-" + getEndingPage() + "\",\n" +
                "year = \"" + getPublicationYear() + "\"\n" +
                "}\n";
    }

    /**
     * Get the article info in proper BibTeX format
     * @param in The scanner for user input
     */
    public void promptForUpdate(Scanner in) {
        super.promptForUpdate(in);

        System.out.println("Enter the updated title of the journal.");
        journal = in.nextLine();

        boolean isValid = false;
        while(!isValid) {
            System.out.println("Enter the updated first page of the article.");
            if (!in.hasNextInt()) {
                in.next();
            } else {
                startingPage = in.nextInt();
                if (startingPage >= 0) {
                    isValid = true;
                }
            }
        }

        isValid = false;
        while(!isValid) {
            System.out.println("Enter the updated last page of the article.");
            if (!in.hasNextInt()) {
                in.next();
            } else {
                endingPage = in.nextInt();
                if (endingPage >= startingPage) {
                    isValid = true;
                }
            }
        }
        in.nextLine();
    }

    public int getEndingPage() {
        return endingPage;
    }

    public int getStartingPage() {
        return startingPage;
    }

    public String getJournal() {
        return journal;
    }

    public void setEndingPage(int endingPage) {
        this.endingPage = endingPage;
    }

    public void setStartingPage(int startingPage) {
        this.startingPage = startingPage;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }
}
