
/**
 * @author: Kammein Mitchell
 * @Date: Last updated 5/19/2020
 */
package msoe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class will create an Alternate Graduation Plan
 */
public class AlternateGraduationPlan {

    private List<String> notPassedCourses;

    /**
     * Constructor for the class
     */
    public AlternateGraduationPlan() {
        notPassedCourses = new ArrayList<>();
    }

    /**
     * Prints the list to detail which classes may not be passed
     * @implNote : this class will need to be updated to return the list to be used to create new track
     * @implNote : in the futrue, this class will take a file that is inherited from the constructor
     * @throws IOException
     */
    public void printList() throws IOException {

        File file = new File("./UnofficialTranscript.pdf");
        Transcript transcript = new Transcript(file);
        CourseCompleter courseCompleter = new CourseCompleter(transcript.getPdf(), new CourseTracksDataStructure());
        courseCompleter.run();
        List<String> make = courseCompleter.getNotPassed();
        for (int i = 0; i < make.size(); i++) {
            System.out.println(make.get(i));
        }
    }

    //Todo: determine if these classes are preRequisites to other classes in the future

    //Todo: create a new track based of the course being pushed into a term depending on when the course is available to be taken

}

