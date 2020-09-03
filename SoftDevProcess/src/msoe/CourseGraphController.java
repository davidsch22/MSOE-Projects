package msoe;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class CourseGraphController {
    @FXML
    private Canvas canvas;

    public void showCompleted() {
        HashMap<String, BasicCourseInfo> allCourses = CourseLists.courses;
        HashMap<String, BasicCourseInfo> completedCourses = new HashMap<>();

        for (BasicCourseInfo course : allCourses.values()) {
            if (course.isCompleted()) {
                completedCourses.put(course.getCourseCode(), course);

                for (int i = 0; i < course.getPrerequisites().size(); i++) {
                    BasicCourseInfo prereq = course.getPrerequisites().get(i);
                    BasicCourseInfo existPrereq = completedCourses.getOrDefault(prereq.getCourseCode(), null);
                    if (existPrereq != null) {
                        course.getPrerequisites().set(i, existPrereq);
                        completedCourses.remove(prereq.getCourseCode());
                    } else {
                        course.getPrerequisites().set(i, allCourses.getOrDefault(prereq.getCourseCode(), prereq));
                    }
                }
            }
        }

        renderCompleted(completedCourses);
    }

    public void renderCompleted(HashMap<String, BasicCourseInfo> completedCourses) {
        int colNum = 1;
        ArrayList<CourseCoords> colPrereqs = new ArrayList<>();

        for (BasicCourseInfo course : completedCourses.values()) {
            colPrereqs.add(new CourseCoords(course));
        }

        while (!colPrereqs.isEmpty()) {
            showColumn(colPrereqs, colNum);

            ArrayList<CourseCoords> nextPrereqs = new ArrayList<>();
            for (CourseCoords prereq : colPrereqs) {
                for (BasicCourseInfo next : prereq.course.getPrerequisites()) {
                    Optional<CourseCoords> already = nextPrereqs.stream().filter(thisCourse ->
                            thisCourse.course.getCourseCode().equals(next.getCourseCode())).findFirst();
                    if (already.isPresent()) {
                        already.get().addCoords(prereq.theseCoords);
                    } else {
                        CourseCoords newPrereq = new CourseCoords(next, prereq.theseCoords);
                        nextPrereqs.add(newPrereq);
                    }
                }
            }
            colPrereqs = nextPrereqs;
            colNum++;
        }
    }

    public void showPrereqs(BasicCourseInfo course) {
        int colNum = 1;
        ArrayList<CourseCoords> colPrereqs = new ArrayList<>();
        colPrereqs.add(new CourseCoords(course));
        while (!colPrereqs.isEmpty()) {
            showColumn(colPrereqs, colNum);

            ArrayList<CourseCoords> nextPrereqs = new ArrayList<>();
            for (CourseCoords prereq : colPrereqs) {
                for (BasicCourseInfo next : prereq.course.getPrerequisites()) {
                    Optional<CourseCoords> already = nextPrereqs.stream().filter(thisCourse ->
                            thisCourse.course.getCourseCode().equals(next.getCourseCode())).findFirst();
                    if (already.isPresent()) {
                        already.get().addCoords(prereq.theseCoords);
                    } else {
                        BasicCourseInfo realNext = CourseLists.courses.getOrDefault(next.getCourseCode(), null);
                        if (realNext == null) realNext = CourseLists.courses.getOrDefault(next.getCourseCode(), null);
                        if (realNext == null) realNext = next;
                        CourseCoords newPrereq = new CourseCoords(realNext, prereq.theseCoords);
                        nextPrereqs.add(newPrereq);
                    }
                }
            }
            colPrereqs = nextPrereqs;
            colNum++;
        }
    }

    private void showColumn(ArrayList<CourseCoords> prereqs, int colNum) {
        final int X_SPREAD = 110;
        int x = (int)canvas.getWidth() - (X_SPREAD * colNum);
        double ySpread = canvas.getHeight() / (prereqs.size() + 1);
        for (int i = 0; i < prereqs.size(); i++) {
            CourseCoords courseCoords = prereqs.get(i);
            int y = (int)(ySpread + (ySpread * i));
            canvas.getGraphicsContext2D().fillText(courseCoords.course.getCourseCode(), x, y);
            canvas.getGraphicsContext2D().strokeRoundRect(x-10, y-20, 60, 30, 5, 5);
            for (int j = 0; j < courseCoords.nextCoords.length; j++) {
                int nextX = courseCoords.nextCoords[j][0];
                int nextY = courseCoords.nextCoords[j][1];
                if (nextX != 0 && nextY != 0) {
                    canvas.getGraphicsContext2D().strokeLine(x + 50, y - 5, nextX - 10, nextY - 5);
                }
            }
            prereqs.get(i).theseCoords[0] = x;
            prereqs.get(i).theseCoords[1] = y;
        }
    }

    private static class CourseCoords {
        BasicCourseInfo course;
        int[] theseCoords;
        int[][] nextCoords;

        public CourseCoords(BasicCourseInfo course) {
            this(course, new int[]{0, 0});
        }

        public CourseCoords(BasicCourseInfo course, int[] nextCoords) {
            this.course = course;
            theseCoords = new int[2];
            this.nextCoords = new int[1][2];
            this.nextCoords[0][0] = nextCoords[0];
            this.nextCoords[0][1] = nextCoords[1];
        }

        public void addCoords(int[] coords) {
            int numCoords = nextCoords.length;
            int[][] newCoords = new int[numCoords+1][2];
            for (int i = 0; i < nextCoords.length; i++) {
                newCoords[i][0] = nextCoords[i][0];
                newCoords[i][1] = nextCoords[i][1];
            }
            newCoords[numCoords][0] = coords[0];
            newCoords[numCoords][1] = coords[1];
            nextCoords = newCoords;
        }
    }
}
