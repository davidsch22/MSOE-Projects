/*
 * Course: CS2852
 * Spring 2018-2019
 * Lab 9 - Autocomplete Revisited
 * Name: David Schulz
 * Created: 5/9/19
 */

package msoe.schulzd.lab9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
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
    Menu stratMenu;
    @FXML
    ToggleGroup strategyGroup;
    @FXML
    TextField keyword;
    @FXML
    TextArea resultsDisplay;
    @FXML
    Label timeDisplay;
    @FXML
    Label matches;

    private AutoCompleter strategy;

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
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            strategy = new IndexedAutoCompleter(new ArrayList<>());

            try {
                strategy.initialize(file.getPath());
            } catch (FileNotFoundException e) {
                throwAlert("File Error", "File could not be found");
                return;
            } catch (IndexOutOfBoundsException e) {
                throwAlert("File Error", "CSV file is not formatted correctly");
                return;
            }

            displayOperationTime();
            stratMenu.setDisable(false);
            keyword.setDisable(false);
        }
    }

    /**
     * Re-reads the file using whatever the strategy was changed to
     * @param event Event that caused method to run
     */
    @FXML
    public void changeStrat(ActionEvent event) {
        List<String> dictionary = strategy.getDictionary();

        RadioMenuItem strategyRadio = (RadioMenuItem)strategyGroup.getSelectedToggle();
        switch (strategyRadio.getText()) {
            case "ArrayListIndexed" :
                strategy = new IndexedAutoCompleter(new ArrayList<>(dictionary));
                break;
            case "ArrayListIterated" :
                strategy = new IteratedAutoCompleter(new ArrayList<>(dictionary));
                break;
            case "LinkedListIndexed" :
                strategy = new IndexedAutoCompleter(new LinkedList<>(dictionary));
                break;
            case "LinkedListIterated" :
                strategy = new IteratedAutoCompleter(new LinkedList<>(dictionary));
                break;
            case "AVLTree" :
                strategy = new AVLTreeCompleter(dictionary);
                break;
            case "CascadingArrayLists" :
                strategy = new CascadingALCompleter(dictionary);
        }

        clearGUI();
        displayOperationTime();
    }

    /**
     * Searches the dictionary with what is entered and displays the results
     */
    @FXML
    public void search() {
        List<String> results = strategy.allThatBeginWith(keyword.getText());

        String display = "";
        for (String word : results) {
            display += word + "\n";
        }

        displayOperationTime();
        resultsDisplay.setText(display);
        matches.setText(Integer.toString(results.size()));
    }

    private void displayOperationTime() {
        long time = strategy.getLastOperationTime();

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

    private void clearGUI() {
        keyword.setText("");
        resultsDisplay.clear();
        timeDisplay.setText("0 milliseconds");
        matches.setText("0");
    }

    private void throwAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
