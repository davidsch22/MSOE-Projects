package msoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseCompleter {
    private CourseTracksDataStructure track;
    private HashMap<String, Course> currentTrack;
    private List<String> pdf;
    private ArrayList<String> list;
    private List<String> notPassed;

    /**
     * this class parses passed data from the transcript and marks courses completed if finished
     */
    public CourseCompleter(List<String> pdf, CourseTracksDataStructure track) {
        this.pdf = pdf;
        stringifyPDF();
        this.track = track;
        currentTrack = track.getSETrackCSV(); // TODO: Add CS track support
        notPassed = new ArrayList<>();
    }

    public void determineCompletion(ArrayList<String> courses) {
        boolean courseCompleted;
        for (int i = 0; i < courses.size(); i++) {
            String line = courses.get(i);
            String[] splitCourses = line.split(":");
            if (splitCourses[1].contains("IP")) {
                notPassed.add(splitCourses[0]);
            }
            for (Map.Entry<String, Course> stringCourseEntry : currentTrack.entrySet()) {
                Course courseData = stringCourseEntry.getValue();
                if (splitCourses[0].equals(courseData.getCourseCode())) { // checks if the course matches a course from the selected track
                    Course course = currentTrack.get(courseData.getCourseCode());
                    courseCompleted = parsePassing(splitCourses[1]); // compares grade to
                    course.setCompleted(courseCompleted);
                }
            }
        }
    }


    private boolean parsePassing(String courseGrade) {
        boolean passed = false;
        String parsedGrade;
        parsedGrade = courseGrade.replaceAll("\r", "");
        parsedGrade = parsedGrade.replaceAll(" ", "");
        switch (parsedGrade) {
            case "A":
            case "AB":
            case "B":
            case "BC":
            case "C":
            case "CD":
            case "D":
            case "S":
            case "TR":
                passed = true;
                break;
        }

        return passed;
    }

    public CourseTracksDataStructure run() {
        pruneReturns();
        determineCompletion(pruneReturns());
        return track;
    }

    private void stringifyPDF() {
        list = new ArrayList<>();
        String oneLiner = pdf.toString();
        String[] array = oneLiner.split("\n");
        for (String s :
                array) {
            if (s.contains("MA231:")) {
                s = s.replaceAll("MA231", "MA2314");
            }
            list.add(s);
        }
    }

    private ArrayList<String> pruneReturns() {
        ArrayList<String> prunedList = new ArrayList<>();
        for (String s :
                list) {
            if (!s.contains("Milwaukee") && !s.contains("Undergraduate") && !s.contains("BS in ") &&
                    !s.contains("Quarter") && !s.contains("Credits") && !s.contains("Page") && !s.contains("Major")
                    && !s.contains("[") && !s.contains("]")) {
                prunedList.add(s);
            }
        }
        return list = prunedList;
    }

    /**
     * Get the list of all classes that can be potentailly failed
     * @return list of classes that can be failed
     */
    public List<String> getNotPassed(){return notPassed;}
}
