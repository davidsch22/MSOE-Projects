/*
 * Name: Anthony Lohmiller, Kam Mitchell, David Schulz, Jesse Sierra
 * Date: 14 April 2020
 * Quarter: Spring 2019-2020
 * Class: SE Process
 * Lab: 4
 * Created: 30 March 2020
 */

package msoe;

import com.opencsv.exceptions.CsvValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;
import java.util.Map.Entry;

public class AdvisingToolController {

    @FXML
    ToggleGroup yearToggle; //ToggleGroup for selecting based on year (Freshman, Sophomore, etc..)

    @FXML
    ToggleGroup termToggle; //ToggleGroup for selecting based on term (Fall, Winter, Spring)

    @FXML
    ToggleGroup majorToggle; //ToggleGroup for selecting based on major (Software Engineering, Computer Science)

    @FXML
    ToggleGroup modeToggle; //ToggleGroup for selecting between modes

    @FXML
    RadioMenuItem studentMenuItem;

    @FXML
    Label modeLabel;

    @FXML
    Button recommendButton;

    @FXML
    Button termSearchButton;

    @FXML
    Button completedGraphButton;

    @FXML
    TextField courseSearch;

    @FXML
    private ListView<String> courseList;

    private final HashMap<String, BasicCourseInfo> allCourses = CourseLists.courses;
    private final HashMap<String, TrackCourseInfo> SETrack = CourseLists.SETrack;
    private final HashMap<String, TrackCourseInfo> CSTrack = CourseLists.CSTrack;
    private List<String> pdf;
    private enum modes { student, faculty }
    private modes mode = modes.student;
    private boolean majorSelected = false;
    private boolean yearSelected = false;
    private boolean termSelected = false;

    /**
     * Initialize method used for the course details popup when ListView item is clicked.
     */
    public void initialize() {
        File curriculum = new File("curriculum.csv");
        File prereqs = new File("prerequisites.csv");
        File offerings = new File("offerings.csv");
        try {
            CourseInformation.completedParse(curriculum, prereqs , offerings);
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        courseList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        BasicCourseInfo course = allCourses.getOrDefault(newValue, null);
                        if (course == null) return;

                        Stage detailsStage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseDetails.fxml"));
                        Parent root = loader.load();
                        detailsStage.setTitle(newValue);
                        detailsStage.setScene(new Scene(root));
                        detailsStage.show();

                        CourseDetailsController detailsController = loader.getController();
                        detailsController.setCourse(course);
                    } catch (IOException e) {
                        // Do nothing, it'll never happen
                        // Required for reading CourseDetails.fxml
                    }
                }
        );
        recommendButton.setDisable(true);
        termSearchButton.setDisable(true);
        completedGraphButton.setDisable(true);
    }

    /**
     * Allows for searching by term when the user hits the Search By Term Button.
     *
     * @param event The event causing this method to fire.
     */
    @FXML
    protected void searchByTerm(ActionEvent event) {

        courseList.getItems().clear(); //clears the list so it doesn't keep adding items.
        if (SETrack != null && CSTrack != null) {
            String majorSearch = ((RadioMenuItem) majorToggle.getSelectedToggle()).getText();
            String termSearch = ((RadioMenuItem) termToggle.getSelectedToggle()).getText();
            String yearSearch = ((RadioMenuItem) yearToggle.getSelectedToggle()).getText();

            //Ensures only SE courses display when it is selected in the menu, same with CS.
            if (majorSearch.equalsIgnoreCase("Software Engineering")) {
                for (Entry<String, TrackCourseInfo> stringCourseEntry : SETrack.entrySet()) {
                    TrackCourseInfo courseData = stringCourseEntry.getValue();
                    if (courseData.getYear().equalsIgnoreCase(yearSearch) && courseData.getTerm().equalsIgnoreCase(termSearch)) {
                        courseList.getItems().add(stringCourseEntry.getKey());
                    }
                }
            } else if (majorSearch.equalsIgnoreCase("Computer Science")) {
                for (Entry<String, TrackCourseInfo> stringCourseEntry : CSTrack.entrySet()) {
                    TrackCourseInfo courseData = stringCourseEntry.getValue();
                    if (courseData.getYear().equalsIgnoreCase(yearSearch) && courseData.getTerm().equalsIgnoreCase(termSearch)) {
                        courseList.getItems().add(stringCourseEntry.getKey());
                    }
                }
            }
        } else {
            //If the user hasn't imported the tracks yet, displays helpful error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("BasicCourseInfo Tracks Not Imported");
            alert.setContentText("All course tracks must be loaded before they are displayed.");
            alert.showAndWait();
        }
    }

    /**
     * Allows for searching by course name when the user hits the Search By BasicCourseInfo button.
     *
     * @param event The event causing this method to fire.
     */
    @FXML
    protected void searchByCourse(ActionEvent event) {
        courseList.getItems().clear(); //clears the list so it doesn't keep adding items.

        String courseStr = courseSearch.getText();
        for (String courseKey : allCourses.keySet()) {
            if (courseKey.toLowerCase().contains(courseStr.toLowerCase())) {
                courseList.getItems().add(courseKey);
            }
        }
    }

    /**
     * Loads the imported PDF and displays to the ListView the recommended courses to take.
     */
    @FXML
    public void onImport() {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileChooser fileChooser = new FileChooser(); // FileChooser for selecting unofficial transcript PDF.
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PDF", "*.pdf"));
        fileChooser.setInitialDirectory(new File(currentPath));
        try {
            File file = fileChooser.showOpenDialog(null);
            Transcript transcript = new Transcript(file);
            pdf = transcript.getPdf();
            CourseCompleter courseCompleter = new CourseCompleter(pdf);
            courseCompleter.run();
            recommendButton.setDisable(false);
            completedGraphButton.setDisable(false);
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Exited Before Choosing PDF");
            alert.setContentText("Please select a PDF to Import");
            alert.showAndWait();
        } catch (IOException e) { // shouldnt get since limited to pdfs but thrown by class
            Alert alert = passAlert(e);
            alert.setHeaderText("Error With Selected File");
        }
    }

    public void onRecommendationsFaculty(){
        final HashMap<String, BasicCourseInfo> notCompleted = new HashMap<>();
        ArrayList<BasicCourseInfo> sorted;
        courseList.getItems().clear();

        for (Map.Entry<String, BasicCourseInfo> stringCourseEntry : allCourses.entrySet()) {
            BasicCourseInfo courseData = stringCourseEntry.getValue();
            if (!courseData.isCompleted()) {
                if (!notCompleted.containsKey(courseData.getCourseCode())) {
                    courseData.addStudent();
                    notCompleted.put(courseData.getCourseCode(), courseData);
                } else {
                    notCompleted.get(courseData.getCourseCode()).addStudent();
                }
            }
        }
        ArrayList<BasicCourseInfo> unsortedMasterList = new ArrayList<>();
        for (Map.Entry<String, BasicCourseInfo> stringCourseEntry : notCompleted.entrySet()) {
            unsortedMasterList.add(stringCourseEntry.getValue());
        }
        sorted = sortList(unsortedMasterList);
        String term = "";
        String year = "";
        String filler = "---------------";
        for (BasicCourseInfo c : sorted) {
            TrackCourseInfo trackC = SETrack.getOrDefault(c.getCourseCode(), null);
            if (trackC == null) trackC = CSTrack.getOrDefault(c.getCourseCode(), null);
            if (trackC == null) continue;

            if (courseList.getItems().isEmpty()) { //TODO: THIS WILL NOT WORK PROPERLY UNTIL ELECTIVES ARE HANDLED
                courseList.getItems().add(filler + "Year: " + trackC.getYear() + " Term: " + trackC.getTerm() + filler);
                term = trackC.getTerm();
                year = trackC.getYear();
            } else if (!trackC.getTerm().equals(term) || !trackC.getYear().equals(year)) {
                courseList.getItems().add(filler + "Year: " + trackC.getYear() + " Term: " + trackC.getTerm() + filler);
                term = trackC.getTerm();
                year = trackC.getYear();
            }
            courseList.getItems().add(c.getCourseCode() + " x" + c.getNumOfStudents());
        }
    }

    public void onRecommendationsStudent(){
        ArrayList<BasicCourseInfo> notCompleted = new ArrayList<>();
        ArrayList<BasicCourseInfo> sorted;
        courseList.getItems().clear(); //clears the list so it doesn't keep adding items.
        for (Map.Entry<String, BasicCourseInfo> stringCourseEntry : allCourses.entrySet()) { //TODO: HARDCODED TO SE TRACK
            BasicCourseInfo courseData = stringCourseEntry.getValue();
            if (!courseData.isCompleted()) {
                notCompleted.add(courseData);
            }
        }
        sorted = sortList(notCompleted);
        String term = "";
        String year = "";
        String filler = "---------------";
        for (BasicCourseInfo c : sorted) {
            TrackCourseInfo trackC = SETrack.getOrDefault(c.getCourseCode(), null);
            if (trackC == null) trackC = CSTrack.getOrDefault(c.getCourseCode(), null);
            if (trackC == null) continue;

            if (courseList.getItems().isEmpty() || (!trackC.getTerm().equals(term) || !trackC.getYear().equals(year))) { //TODO: THIS WILL NOT WORK PROPERLY UNTIL ELECTIVES ARE HANDLED
                courseList.getItems().add(filler + "Year: " + trackC.getYear() + " Term: " + trackC.getTerm() + filler);
                term = trackC.getTerm();
                year = trackC.getYear();
            }
            courseList.getItems().add(c.getCourseCode());
        }
    }

    //TODO: Sort by year and by term.
    @FXML
    public void onRecommendations() {
        if (mode == modes.faculty){
            onRecommendationsFaculty();
        } else {
            onRecommendationsStudent();
        }
    }

    @FXML
    public void onGenerateGraduationPlanSelect() {
        if (mode == modes.faculty){
            onGenerateGraduationPlan("faculty");
        } else {
            onGenerateGraduationPlan("student");
        }
    }

    @FXML
    public void onModeSelect(){
        String selected = ((RadioMenuItem)modeToggle.getSelectedToggle()).getText();
        if (selected.equalsIgnoreCase("student")){
            mode = modes.student;
        } else if (selected.equalsIgnoreCase("faculty")){
            mode = modes.faculty;
        }
        modeLabel.setText(selected);
    }

    /**
     * Allows for exiting the program when the exit menu item is selected.
     */
    @FXML
    public void onExit() {
        System.exit(0);
    }

    /**
     * Helper method for displaying alerts
     *
     * @param e The exception thrown
     * @return an alert object to display to the user
     */
    private Alert passAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getMessage());
        return alert;
    }

    private ArrayList<BasicCourseInfo> sortList(ArrayList<BasicCourseInfo> unsortedList){
        ArrayList<BasicCourseInfo> sortedList = new ArrayList<>();
        ArrayList<String> terms = new ArrayList<>();
        terms.add("fall");
        terms.add("winter");
        terms.add("spring");
        ArrayList<String> years = new ArrayList<>();
        years.add("freshman");
        years.add("sophomore");
        years.add("junior");
        years.add("senior");
        for (String year :
                years) {
            for (String term :
                    terms) {
                for (BasicCourseInfo c : unsortedList) {
                    TrackCourseInfo trackC = SETrack.getOrDefault(c.getCourseCode(), null);
                    if (trackC == null) trackC = CSTrack.getOrDefault(c.getCourseCode(), null);
                    if (trackC == null) continue;

                    if (trackC.getTerm().equalsIgnoreCase(term) && trackC.getYear().equalsIgnoreCase(year)) {
                        sortedList.add(c);
                    }
                }
            }
        }
        return sortedList;
    }

    /**
     * Displays searchByTerm button only when all toggles have been selected
     * @param e: The action event to use (in case we need it)
     */
    @FXML
    private void displayTermSearchButton(ActionEvent e){
        if(majorToggle.getSelectedToggle() != null){
            if(majorToggle.getSelectedToggle().isSelected())
            majorSelected = true;
        }
        if(yearToggle.getSelectedToggle() != null){
            if(yearToggle.getSelectedToggle().isSelected())
                yearSelected = true;
        }
        if(termToggle.getSelectedToggle() != null){
            if(termToggle.getSelectedToggle().isSelected())
                termSelected = true;
        }
        if(majorSelected && termSelected && yearSelected){
            termSearchButton.setDisable(false);
        }
    }

    @FXML
    private void clearDisplay(ActionEvent e){
        courseList.getItems().clear();
    }

    @FXML
    private void showCompletedGraph(ActionEvent event) {
        try {
            Stage graphStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseGraph.fxml"));
            Parent root = loader.load();
            graphStage.setTitle("Completed Courses");
            graphStage.setScene(new Scene(root));
            graphStage.setResizable(false);
            graphStage.show();

            CourseGraphController graphController = loader.getController();
            graphController.showCompleted();
        } catch (IOException e) {
            // Do nothing, it'll never happen
            // Required for reading CourseGraph.fxml
        }
    }

    @FXML
    protected void onGenerateGraduationPlan(String type) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "TXT Files (*.txt)", "*.txt"));
        File file = fc.showSaveDialog(null);
        generateGraduationPlan(file, type);
    }

    private static void generateGraduationPlan(File file, String type) {
        // TODO: Make sure this is only accessible after importing a transcript
        String currentTerm = null;
        String currentYear = null;
        String nextTerm = null;
        String nextYear = null;
        ArrayList<BasicCourseInfo> completedCourses = new ArrayList<>();
        ArrayList<BasicCourseInfo> incompleteCourses = new ArrayList<>();
        ArrayList<BasicCourseInfo> electives = new ArrayList<>();

        for (Map.Entry<String, BasicCourseInfo> stringCourseEntry : CourseLists.courses.entrySet()) {
            BasicCourseInfo courseData = stringCourseEntry.getValue();
            if (!courseData.isCompleted() && !(courseData.getCourseName().equalsIgnoreCase(""))) {
                incompleteCourses.add(courseData);
            } else if(courseData.isCompleted() && !(courseData.getCourseName().equalsIgnoreCase(""))) {
                completedCourses.add(courseData);
            } else {
                electives.add(courseData);
            }
        }

        int fall = 0;
        int winter = 0;
        int spring = 0;


        for(BasicCourseInfo course : incompleteCourses){
            TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) continue;

            String term = trackC.getTerm();
            if(term.equalsIgnoreCase("Fall")){
                fall++;
            } else if(term.equalsIgnoreCase("Winter")){
                winter++;
            } else if(term.equalsIgnoreCase("Spring")){
                spring++;
            }

            if(fall == 3 || winter == 3 || spring ==3){
                currentTerm = term;
                currentYear = trackC.getYear();
                break;
            }

        }


        int nF = 0;
        int nW = 0;
        int nS = 0;
        for(BasicCourseInfo course: incompleteCourses){
            TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) continue;

            if(!(trackC.getTerm().equalsIgnoreCase(currentTerm))){
                String nTerm = trackC.getTerm();
                if(nTerm.equalsIgnoreCase("Fall")){
                    nF++;
                } else if(nTerm.equalsIgnoreCase("Winter")){
                    nW++;
                } else if(nTerm.equalsIgnoreCase("Spring")){
                    nS++;
                }

                if(nF == 3 || nW == 3 || nS ==3){
                    nextTerm = nTerm;
                    nextYear = trackC.getYear();
                    break;
                }

            }
        }

        int estimatedGradYear = 0;
        if (currentYear != null) {
            if(currentYear.equalsIgnoreCase("Freshman")){
                estimatedGradYear = Year.now().getValue() + 3;
            } else if(currentYear.equalsIgnoreCase("Sophomore")){
                estimatedGradYear = Year.now().getValue() + 2;
            } else if(currentYear.equalsIgnoreCase("Junior")){
                estimatedGradYear = Year.now().getValue() + 1;
            } else if(currentYear.equalsIgnoreCase("Senior")){
                estimatedGradYear = Year.now().getValue();
            }
        }

        if(type.equalsIgnoreCase("student")){
            try {
                PrintWriter pw = new PrintWriter(file);
                pw.write("Personal Graduation Plan\n");
                pw.write("Current Year Standing: " + currentYear + "\n");
                pw.write("Current Term: " + currentTerm + "\n");
                pw.write("Next Year Standing: " + nextYear + "\n");
                pw.write("Next Term: " + nextTerm + "\n");
                pw.write("Estimated Graduation Date: May " + estimatedGradYear);
                pw.write("\n\n");
                pw.write("Currently Enrolled Courses:\n");
                pw.write("\n");
                for(BasicCourseInfo course: incompleteCourses){
                    TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
                    if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
                    if (trackC == null) continue;

                    if(trackC.getTerm().equalsIgnoreCase(currentTerm) && trackC.getYear().equalsIgnoreCase(currentYear)){
                        pw.write(trackC.getTerm() + " - " +course.getCourseCode() + " - " + course.getCourseName() + "\n");
                    }
                }
                pw.write("\n\n");
                pw.write("Completed Courses:\n");
                printClasses(completedCourses, pw);

                pw.write("\n\n");
                pw.write("Incomplete Courses Needed for Graduation:\n");
                printClasses(incompleteCourses, pw);
                pw.write("\n\n");
                pw.write("NOTE: This does not include electives, please consult your faculty advisor.");
                pw.close();
            } catch (FileNotFoundException e) {
                //User Exited without saving a file
                //Do nothing
            }

        } else {
            try {
                PrintWriter pw = new PrintWriter(file);
                pw.write("Advising Graduation Plan\n");
                pw.write("Student's Current Year and Term: " + currentYear + " - " + currentTerm + " Semester\n");
                pw.write("Student's Next Year and Term: " + nextYear + " - " + nextTerm + " Semester\n");
                pw.write("Estimated Graduation Date: May " + estimatedGradYear);
                pw.write("\n\n");
                pw.write("Student's Current Courses:\n");
                pw.write("\n");
                for(BasicCourseInfo course: incompleteCourses){
                    TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
                    if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
                    if (trackC == null) continue;

                    if(trackC.getTerm().equalsIgnoreCase(currentTerm) && trackC.getYear().equalsIgnoreCase(currentYear)){
                        pw.write(trackC.getTerm() + " - " +course.getCourseCode() + " - " + course.getCourseName() + "\n");
                    }
                }
                pw.write("\n\n");
                pw.write("Completed Courses:\n");
                printClasses(completedCourses, pw);

                pw.write("\n\n");
                pw.write("Incomplete Courses Needed for Graduation:\n");
                printClasses(incompleteCourses, pw);
                pw.write("\n\n");
                pw.write("NOTE: Electives are not currently generated for student, refer to master list of electives for additional information.");
                pw.close();
            } catch (FileNotFoundException e) {
                //User Exited without saving a file
                //Do nothing
            }
        }

    }

    public static void printClasses(ArrayList<BasicCourseInfo> courses, PrintWriter pw){
        pw.write("\n");
        pw.write("Freshman\n");
        pw.write("------------------------\n");
        for(BasicCourseInfo course: courses){
            TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) continue;

            if(trackC.getYear().equalsIgnoreCase("Freshman")){
                pw.write(trackC.getTerm() + " - " +course.getCourseCode() + " - " + course.getCourseName() + "\n");
            }
        }
        pw.write("\n");
        pw.write("Sophomore\n");
        pw.write("------------------------\n");
        for(BasicCourseInfo course: courses){
            TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) continue;

            if(trackC.getYear().equalsIgnoreCase("Sophomore")){
                pw.write(trackC.getTerm() + " - " +course.getCourseCode() + " - " + course.getCourseName() + "\n");
            }
        }
        pw.write("\n");
        pw.write("Junior\n");
        pw.write("------------------------\n");
        for(BasicCourseInfo course: courses){
            TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) continue;

            if(trackC.getYear().equalsIgnoreCase("Junior")){
                pw.write(trackC.getTerm() + " - " +course.getCourseCode() + " - " + course.getCourseName() + "\n");
            }
        }
        pw.write("\n");
        pw.write("Senior\n");
        pw.write("------------------------\n");
        for(BasicCourseInfo course: courses){
            TrackCourseInfo trackC = CourseLists.SETrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) trackC = CourseLists.CSTrack.getOrDefault(course.getCourseCode(), null);
            if (trackC == null) continue;

            if(trackC.getYear().equalsIgnoreCase("Senior")){
                pw.write(trackC.getTerm() + " - " +course.getCourseCode() + " - " + course.getCourseName() + "\n");
            }
        }
    }

}
