/*
 * Name: Anthony Lohmiller, Kam Mitchell, David Schulz, Jesse Sierra
 * Date: 14 April 2020
 * Quarter: Spring 2019-2020
 * Class: SE Process
 * Lab: 4
 * Created: 30 March 2020
 */

package msoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller class that handles the individual course pop-up window when a course is clicked
 * from the ListView.
 */
public class CourseDetailsController {
    @FXML
    private TextField name;
    @FXML
    private TextField code;
    @FXML
    private TextField credits;
    @FXML
    private ListView<String> prereqs;
    @FXML
    private CheckBox completed;

    private BasicCourseInfo course;

    /**
     * Allows the user to edit information about the course.
     * Intentionally left editable for the moment, will be changed later.
     * @param course The course to edit, as selected from the ListView.
     */
    public void setCourse(BasicCourseInfo course) {
        this.course = course;
        name.setText(course.getCourseName());
        code.setText(course.getCourseCode());
        credits.setText(String.valueOf(course.getCredits()));
        for (BasicCourseInfo prereq : course.getPrerequisites()) {
            prereqs.getItems().add(prereq.getCourseCode());
        }
        completed.setSelected(course.isCompleted());
    }

    @FXML
    public void showGraph(ActionEvent event) {
        try {
            Stage graphStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseGraph.fxml"));
            Parent root = loader.load();
            graphStage.setTitle("Prerequisites");
            graphStage.setScene(new Scene(root));
            graphStage.setResizable(false);
            graphStage.show();

            CourseGraphController graphController = loader.getController();
            graphController.showPrereqs(course);
        } catch (IOException e) {
            // Do nothing, it'll never happen
            // Required for reading CourseGraph.fxml
        }
    }
}
