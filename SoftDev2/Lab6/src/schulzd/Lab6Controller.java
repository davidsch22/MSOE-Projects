/*
 * Course: CS1021
 * Winter 2018-2019
 * Lab 6 - Exceptions
 * Name: David Schulz
 * Created: 1/15/19
 */
package schulzd;

import edu.msoe.se1021.Lab6.WebsiteTester;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.net.ConnectException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The FXML file's controller class
 */
public class Lab6Controller implements Initializable {
    @FXML
    TextField url;
    @FXML
    Button analyze;
    @FXML
    TextField size;
    @FXML
    TextField time;
    @FXML
    TextField port;
    @FXML
    TextField host;
    @FXML
    TextField timeout;
    @FXML
    Button set;
    @FXML
    TextArea webpage;

    private WebsiteTester tester = new WebsiteTester();
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    /**
     * The FXML file's required initialize method
     * @param location The location of the file
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

    }

    /**
     * Gets basic info about the website at the given URL
     */
    public void analyze() {
        String urlEntered = url.getText();

        try {
            tester.openURL(urlEntered);
            tester.openConnection();
            tester.downloadText();

            int fileSize = tester.getSize();
            size.setText(Integer.toString(fileSize));

            double downloadTime = tester.getDownloadTime();
            time.setText(Double.toString(downloadTime));

            String hostname = tester.getHostname();
            host.setText(hostname);

            int sitePort = tester.getPort();
            port.setText(Integer.toString(sitePort));

            String content = tester.getContent();
            webpage.setText(content);

        } catch (MalformedURLException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("URL Error");
            alert.setContentText("The URL entered in the text box is invalid");
            alert.showAndWait();
            url.setText("");
            return;
        } catch (UnknownHostException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Host Error");
            alert.setContentText("Error: Unable to reach the host " + urlEntered);
            alert.showAndWait();
            return;
        } catch (SocketTimeoutException e) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Wait longer?");
            alert.setContentText("There has been a timeout reaching the site. " +
                    "Click OK to extend the timeout period.");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                extendTimeout();
            }
            return;
        } catch (ConnectException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Connection Error");
            alert.setContentText("Error: Unable to connect to " + urlEntered);
            alert.showAndWait();
            return;
        } catch (FileNotFoundException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("File Error");
            alert.setContentText("Error: File not found on the server, " + urlEntered);
            alert.showAndWait();
            return;
        } catch (IOException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Error: There was a problem reading from the file");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    /**
     * Extends the timeout time when user chooses to,
     * then reanalyzes the website
     */
    public void extendTimeout() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set Timeout");
        dialog.setHeaderText("Set extended timeout");
        dialog.setContentText("Desired timeout:");
        Optional<String> newTimeout = dialog.showAndWait();
        if (newTimeout.isPresent()) {
            timeout.setText(newTimeout.get());
            setTimeout();
            analyze();
        }
    }

    /**
     * Sets the amount of time (in ms) to wait before throwing
     * SocketTimeoutException if connection to website can't be made
     */
    public void setTimeout() {
        try {
            String timeoutEntered = timeout.getText();
            tester.setTimeout(timeoutEntered);
        } catch (NumberFormatException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Timeout Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
