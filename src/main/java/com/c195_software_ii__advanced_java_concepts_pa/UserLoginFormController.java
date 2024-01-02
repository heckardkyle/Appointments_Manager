package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.JDBC;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.UserDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Utilities.Log_Activity;
import com.c195_software_ii__advanced_java_concepts_pa.Utilities.LoginAlert;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserLoginFormController implements Initializable {

    /* Stage Declarations */
    Stage  stage;
    Parent scene;

    /* Language Resource Bundle Declarations */
    ResourceBundle rb;

    /* User Login Page FXML Declarations */
    @FXML private Label         titleLabel;
    @FXML private Label         userloginLabel;
    @FXML private Label         timezoneLabel;
    @FXML private TextField     usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button        signinButton;

    /* Exception Declarations */
    /**
     * Declares 'UsernameEmptyException'.
     * Thrown if usernameTextField is empty.
     */
    public class UsernameEmptyException extends Exception {
        public UsernameEmptyException() {} }

    /**
     * Declares 'PasswordEmptyException'.
     * Thrown if passwordField is empty.
     */
    public class PasswordEmptyException extends Exception {
        public PasswordEmptyException() {} }

    /**
     * Declares 'IncorrectCredentialsException'.
     * Thrown if username and password do not have a match.
     */
    public class IncorrectCredentialsException extends Exception {
        public IncorrectCredentialsException() {} }

    /**
     * Displays alert to user if any custom exceptions are thrown.
     * @param message message to display to user in alert prompt
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(rb.getString("loginError"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * @param event signin Button pressed
     * @throws Exception Exceptions thrown for failed userName or password entries
     */
    @FXML
    void onActionSignIn(ActionEvent event) throws Exception {
        try {
            // throw exception if username or password is empty
            if (usernameTextField.getText().isEmpty()) { throw new UsernameEmptyException(); }
            if (passwordField.getText().isEmpty()) { throw new PasswordEmptyException(); }

            // Checks if username and password are valid
            if (UserDBImpl.GetUser(usernameTextField.getText(), passwordField.getText()) != null) {

                // Log successful login
                Log_Activity.LogActivity(usernameTextField.getText(), "Successfully Logged In");

                // Alert user if any appointments are within 15 minutes
                LoginAlert.LoginAlert();

                // Take user to main page
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                // After sign on, prompts user to confirm exit when X button is pressed.
                stage.setOnCloseRequest(event1 -> {
                    // Prompt user with confirmation box
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("exitPressed"));

                    // Wait for response
                    Optional<ButtonType> result = alert.showAndWait();

                    // If ok, exit application
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        JDBC.closeConnection();
                        System.exit(0);
                    }
                    // Prevent Exiting Application
                    else {
                        throw new RuntimeException("Cancel Application Closure");
                    }
                });
            }
            // Throw exception if credentials invalid
            else { throw new IncorrectCredentialsException(); }
        }
        // catch block for this function's Exceptions
        catch (UsernameEmptyException | PasswordEmptyException | IncorrectCredentialsException | RuntimeException e) {
            // alert user of empty username
            if (e instanceof UsernameEmptyException) { showAlert(rb.getString("usernameEmpty")); }

            // alert user of empty password
            if (e instanceof PasswordEmptyException) { showAlert(rb.getString("passwordEmpty")); }

            // alert user of invalid credentials and log activity
            if (e instanceof IncorrectCredentialsException) {
                Log_Activity.LogActivity(usernameTextField.getText(), "Invalid Credentials");
                showAlert(rb.getString("incorrectCredentials")); }

            // Corrects error that causes unexpected Application Closure
            if (e instanceof RuntimeException) { /* Cancel Exiting Application */ }
        }
    }


    /**
     * Initializes Stage and Scene.
     * Detects System language and translates text boxes and labels.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rb = ResourceBundle.getBundle("UserLogin", Locale.getDefault());
        titleLabel.setText(rb.getString("title"));
        userloginLabel.setText(rb.getString("userLogin"));
        timezoneLabel.setText(rb.getString("timezone") + ": [" + ZoneId.systemDefault() + "]");
        usernameTextField.setPromptText(rb.getString("username"));
        passwordField.setPromptText(rb.getString("password"));
        signinButton.setText(rb.getString("signin"));
    }
}
