/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 4 - Auto Complete
 * Name: David Schulz
 * Created: 3/27/19
 */

package msoe.schulzd.lab4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Date;
import java.util.TimeZone;

/**
 * FXML file's controller
 */
public class AutoCompleteController {
    @FXML
    RadioMenuItem radioArrayList;
    @FXML
    RadioMenuItem radioLinkedList;
    @FXML
    RadioMenuItem radioIndexed;
    @FXML
    RadioMenuItem radioIterated;
    @FXML
    TextField keyword;
    @FXML
    TextArea resultsDisplay;
    @FXML
    Label timeDisplay;
    @FXML
    Label matches;

    private File file;
    private IndexedAutoCompleter indexed;
    private IteratedAutoCompleter iterated;

    /**
     * Prompts user to select a file and then reads it
     * @param event Event that caused method to run
     */
    @FXML
    public void open(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Dictionary File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv")
        );
        file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            if (radioArrayList.isSelected()) {
                indexed = new IndexedAutoCompleter(new ArrayList<>());
                iterated = new IteratedAutoCompleter(new ArrayList<>());
            } else if (radioLinkedList.isSelected()) {
                indexed = new IndexedAutoCompleter(new LinkedList<>());
                iterated = new IteratedAutoCompleter(new LinkedList<>());
            }

            try {
                indexed.initialize(file.getPath());
                iterated.initialize(file.getPath());
            } catch (FileNotFoundException e) {
                throwAlert("File Error", "File could not be found");
                return;
            } catch (IndexOutOfBoundsException e) {
                throwAlert("File Error", "CSV file is not formatted correctly");
                return;
            }

            displayOperationTime();
            keyword.setDisable(false);
        }
    }

    /**
     * Re-reads the file using whatever the strategy was changed to
     * @param event Event that caused method to run
     */
    @FXML
    public void changeStrat(ActionEvent event) {
        try {
            indexed.initialize(file.getPath());
            iterated.initialize(file.getPath());
        } catch (FileNotFoundException e) {
            throwAlert("File Error", "File could not be found");
            return;
        } catch (IndexOutOfBoundsException e) {
            throwAlert("File Error", "CSV file is not formatted correctly");
            return;
        }

        displayOperationTime();
        keyword.setText("");
        search();
    }

    /**
     * Searches the dictionary with what is entered and displays the results
     */
    @FXML
    public void search() {
        List<String> results;
        if (radioIndexed.isSelected()) {
            results = indexed.allThatBeginWith(keyword.getText());
        } else {
            results = iterated.allThatBeginWith(keyword.getText());
        }

        String display = "";
        for (String word : results) {
            display += word + "\n";
        }

        displayOperationTime();
        resultsDisplay.setText(display);
        matches.setText(Integer.toString(results.size()));
    }

    private void displayOperationTime() {
        long time;
        if (radioIndexed.isSelected()) {
            time = indexed.getLastOperationTime();
        } else {
            time = iterated.getLastOperationTime();
        }

        final int nanosInMicros = 1000;
        final int nanosInMillis = 1000000;
        final int nanosInSeconds = 1000000000;

        if (time < nanosInMicros) {
            timeDisplay.setText(time + " nanoseconds");
        } else if (time < nanosInMillis) {
            timeDisplay.setText((time / nanosInMicros) + " microseconds");
        } else if (time < nanosInSeconds) {
            timeDisplay.setText((time / nanosInMillis) + " milliseconds");
        } else {
            Date date = new Date(time / nanosInMillis);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            timeDisplay.setText(formatter.format(date));
        }
    }

    private void throwAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
