/*
 * Course: SE 2800 051
 * Date: 31 March 2020
 * Quarter: Spring 2019-2020
 * Lab: 1
 * Created: 30 March 2020
 */

import msoe.Course;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;


public class courseTesting {

    @DataProvider (name = "courseTermProvider")
    public Object[][] courseTermProvider(){
        return new Object[][]{
                new Object[]{"software Development", "CS1011", "fall", "freshman", 4, "", "fall"},
                new Object[]{"Web Application Development", "Se2840", "winter", "sOphoMore", 4, "", "winter"},
                new Object[]{"software VerIFICation", "cs2932", "spring", "senior", 4, "", "spring"},
        };
    }

    @DataProvider (name = "courseCodeProvider")
    public Object[][] courseCodeProvider(){
        return new Object[][]{
                new Object[]{"software Development", "CS1011", "fall", "freshman", 4, "", "CS1011"},
                new Object[]{"Web Application Development", "SE2840", "winter", "sOphoMore", 4, "", "SE2840"},
                new Object[]{"software VerIFICation", "CS2832", "spring", "senior", 4, "", "CS2832"},
        };
    }

    @DataProvider (name = "courseNameProvider")
    public Object[][] courseNameProvider(){
        return new Object[][]{
                new Object[]{"software Development", "CS1011", "fall", "freshman", 4, "", "software Development"},
                new Object[]{"Web Application Development", "SE2840", "winter", "sOphoMore", 4, "", "Web Application Development"},
                new Object[]{"software VerIFICation", "CS2832", "spring", "senior", 4, "", "software VerIFICation"},
        };
    }

    @DataProvider (name = "courseSetCompletedProvider")
    public Object[][] courseSetCompletedProvider(){
        return new Object[][]{
                new Object[]{"software Development", "CS1011", "fall", "freshman", 4, "", true, true},
                new Object[]{"Web Application Development", "SE2840", "winter", "sOphoMore", 4, "", false, false},
        };
    }

    //test marking classes as completed
    //test comparisons between available classes and completed classes
    @Test
    public void createCourse(){
        Course course = new Course("Software Development", "CS1011","Fall", "Freshman", 4, "");
        assertEquals(course.getCourseName(), "Software Development");
    }

    @Test
    public void setAvailableQuarters(){
        Course course = new Course("Chemistry", "CH200", "Fall", "Freshman", 4, "");
        course.addAvailableQuarter("fall");
        course.addAvailableQuarter("spring");
        ArrayList<String> results = course.getAvailableQuarters();
        assertEquals(results.size(), 2); // size should be 1
        assertEquals(results.get(0), "fall"); // available quarter should be fall
        assertEquals(results.get(1), "spring");
    }

    @Test
    public void setPrerequisites(){
        Course course = new Course("Calculus for Engineers III", "MA2314", "Winter", "Sophomore", 4, "");
        Course prereqCourse = new Course("Calculus for Engineers II", "MA137", "Fall", "Sophomore", 4, "");
        course.addPrereq(prereqCourse);
        assertEquals(course.getPrerequisites().toArray()[0], prereqCourse);
        assertEquals(course.getPrerequisites().get(0).getCourseName(), "Calculus for Engineers II");
    }

    @Test
    public void checkInitCompletion(){
        Course course = new Course("Calculus for Engineers ", "MA2314", "Winter", "Sophomore", 4, "");
        assertFalse(course.isCompleted()); // should be initially set to not completed
    }

    @Test
    public void checkCompletion(){
        Course course = new Course("Calculus for Engineers III", "MA2314", "Fall", "Winter", 4, "");
        course.setCompleted(false);
        assertFalse(course.isCompleted());
    }

    @Test (dataProvider = "courseTermProvider")
    public void testCourseAvailableTerms(String name, String code, String term, String year, int credits, String date, String expectedResult){
        Course sut = new Course(name, code, term, year, credits, date);
        assertEquals(sut.getTerm(), expectedResult);
    }

    @Test (dataProvider = "courseCodeProvider")
    public void testCourseAvailableCodes(String name, String code, String term, String year, int credits, String date, String expectedResult){
        Course sut = new Course(name, code, term, year, credits, date);
        assertEquals(sut.getCourseCode(), expectedResult);
    }

    @Test (dataProvider = "courseNameProvider")
    public void testCourseAvailableNames(String name, String code, String term, String year, int credits, String date, String expectedResult){
        Course sut = new Course(name, code, term, year, credits, date);
        assertEquals(sut.getCourseName(), expectedResult);
    }

    @Test (dataProvider = "courseSetCompletedProvider")
    public void testSetCompleted(String name, String code, String term, String year, int credits, String date, boolean valueToSet, boolean expectedValue){
        Course sut = new Course(name, code, term, year, credits, date);
        sut.setCompleted(valueToSet);
        assertEquals(valueToSet, expectedValue);
    }

    @Test
    public void testSetters(){
        Course sut = new Course("ChangeMe", "", "", "", 0,"");
        sut.setCourseName("Software Development");
        sut.setCourseCode("CS1011");
        sut.setTerm("fall");
        sut.setYear("senior");
        sut.setCredits(4);
        sut.setDate("September 2020");
        sut.addStudent();
        assertEquals(sut.getCourseName(), "Software Development");
        assertEquals(sut.getCourseCode(), "CS1011");
        assertEquals(sut.getTerm(), "fall");
        assertEquals(sut.getYear(), "senior");
        assertEquals(sut.getCredits(), 4);
        assertEquals(sut.getDate(), "September 2020");
        assertEquals(sut.getNumOfStudents(), 1);
    }
}
