/*
 * Course: SE 2800 051
 * Date: 31 March 2020
 * Quarter: Spring 2019-2020
 * Lab: 1
 * Created: 30 March 2020
 */

import msoe.Course;
import msoe.CourseTracksDataStructure;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;


public class Testing {

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
        ArrayList<String> results = new ArrayList<>(course.getAvailableQuarters());
        assertEquals(results.size(), 1); // size should be 1
        assertEquals(results.get(0), "fall"); // available quarter should be fall
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
        assertTrue(course.isCompleted());
    }

//    @Test
//    public void checkTrackInit(){
//        CourseLists track = new CourseLists();
//        track.initializeCourseTracks();
//        HashMap<String, BasicCourseInfo> sut = track.getCSTrack();
//        assertNotNull(sut);
//        assertFalse(sut.isEmpty());
//    }
//
//    @Test
//    public void checkCourses(){
//        CourseLists track = new CourseLists();
//        track.initializeCourseTracks();
//        HashMap<String, BasicCourseInfo> sut = track.getCSTrack();
//        BasicCourseInfo result = sut.get("calculus for engineers iii");
//        System.out.println(result);
//        assertEquals(result.getCourseName(), "Calculus for Engineers III");
//        assertEquals(result.getCourseCode(), "MA2314");
//    }
}
