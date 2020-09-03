/*
 * Name: Anthony Lohmiller, Kam Mitchell, David Schulz, Jesse Sierra
 * Date: 14 April 2020
 * Quarter: Spring 2019-2020
 * Class: SE Process
 * Lab: 4
 * Created: 30 March 2020
 */

package msoe;

import java.util.ArrayList;

/**
 * this is a course object to house the details of a specific course offered from a track
 */
public class BasicCourseInfo {
    private ArrayList<String> availableQuarters; //just created this as an array list for
    // now, unsure what best method would be for a large scale integration of this
    private String courseName;
    private ArrayList<BasicCourseInfo> prerequisites;
    private enum Status { notFinished, finished }
    private Status completion;
    private String courseCode; //Letter/Digit combo representing the course.
    private int credits;
    private int numOfStudents;

    /**
     * A course used by the track housing the necessary information tied to the specific course
     * @param name the name of the course
     */

    public BasicCourseInfo(String name, String courseCode, int credits){
        courseName = name;
        this.courseCode = courseCode;
        this.courseCode = courseCode.replaceAll("\\s", "");
        this.credits = credits;
        completion = Status.notFinished;
        prerequisites = new ArrayList<>();
        numOfStudents = 0;
    }

    /**
     * adds a prerequisite course to the list of necessary prerequisites
     * @param prereq the prerequisite course
     */
    public void addPrereq(BasicCourseInfo prereq){
        prerequisites.add(prereq);
    }

    /**
     * this sets the available quarters for a class
     * @param quarter the quarter the class is available in
     */
    public void addAvailableQuarter(String quarter){
        if (quarter.equalsIgnoreCase("fall") || quarter.equalsIgnoreCase("winter") ||
                quarter.equalsIgnoreCase("spring") || quarter.equalsIgnoreCase("summer")){
            availableQuarters.add(quarter.toLowerCase());
        }
    }

    // Accessor and mutator methods

    /**
     * this method marks a class as finished
     */
    public void setCompleted(boolean completionReplacement){
        if (completionReplacement) {
            completion = Status.finished;
        } else {
            completion = Status.notFinished;
        }
    }

    public boolean isCompleted(){
        return completion.equals(Status.finished);
    }

    public ArrayList<String> getAvailableQuarters(){
        return availableQuarters;
    }

    public ArrayList<BasicCourseInfo> getPrerequisites(){
        return prerequisites;
    }

    public void setCourseName(String name) {
        courseName = name;
    }

    public String getCourseName(){
        return courseName;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public String getCourseCode(){
        return this.courseCode;
    }

    public void setCredits(int credits){
        this.credits = credits;
    }

    public int getCredits(){
        return this.credits;
    }

    public int getNumOfStudents(){
        return this.numOfStudents;
    }

    public void addStudent(){
        numOfStudents++;
    }
    //End Accessor and mutator method block

}
