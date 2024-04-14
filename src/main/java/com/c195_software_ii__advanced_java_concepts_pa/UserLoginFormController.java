package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.JDBC;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.UserDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.IncorrectCredentialsException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.PasswordEmptyException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.UsernameEmptyException;
import com.c195_software_ii__advanced_java_concepts_pa.Utilities.AlertInterface;
import com.c195_software_ii__advanced_java_concepts_pa.Utilities.Log_Activity;
import com.c195_software_ii__advanced_java_concepts_pa.Utilities.LoginAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * UserLoginForm Page Controller.
 * Starting window of application for User to Log in to application.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class UserLoginFormController implements Initializable {

    /* --Stage Declarations-- */
    Stage  stage;
    Parent scene;

    /* --Language Resource Bundle-- */
    ResourceBundle rb;

    /* --UserLoginForm FXMLs-- */
    @FXML private Label         titleLabel;
    @FXML private Label         userloginLabel;
    @FXML private Label         timezoneLabel;
    @FXML private TextField     usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button        signinButton;

    /* --Lambdas-- */
    AlertInterface warningAlert = (title, message) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    };

    /**
     * Verifies User login info and takes user to main page of application.
     * Each login attempt is login attempt is logged unless one of the text fields are empty. If the User successfully
     * logs into the application, an alert is shown of any appointments upcoming within 15 minutes of login.
     * This method also handles application closure if the Red X button is pressed.
     *
     * @param event signin Button pressed
     * @throws Exception Exceptions thrown for failed userName or password entries
     */
    @FXML
    void onActionSignIn(ActionEvent event) throws Exception {
        try {
            // throw exception if username or password is empty
            if (usernameTextField.getText().isEmpty()) { throw new UsernameEmptyException(); }
            if (passwordField    .getText().isEmpty()) { throw new PasswordEmptyException(); }

            // If Username and Password is valid
            if (UserDBImpl.getUser(usernameTextField.getText(), passwordField.getText()) != null) {

                // Log successful login
                Log_Activity.LogActivity(usernameTextField.getText(), "Successfully Logged In");

                // Alert user if any appointments are within 15 minutes
                LoginAlert.LoginAlert();

                // Take user to main page
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                // ------------------------------------- //
                // Because this page is the Parent scene //
                // ------------------------------------- //

                // After sign on, prompts user to confirm exit when X button is pressed.
                stage.setOnCloseRequest(event1 -> {

                    // Prompt user with confirmation box
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("exitPressed"));

                    // Wait for response
                    Optional<ButtonType> result = alert.showAndWait();

                    // If user selects ok, close connection and exit application
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        JDBC.closeConnection();
                        System.exit(0);
                    }
                    // If user selects cancel
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

            // alert user of empty username text field
            if (e instanceof UsernameEmptyException) { warningAlert.showAlert(rb.getString("loginError"), rb.getString("usernameEmpty")); }

            // alert user of empty password field
            if (e instanceof PasswordEmptyException) { warningAlert.showAlert(rb.getString("loginError"), rb.getString("passwordEmpty")); }

            // alert user of invalid credentials and log activity
            if (e instanceof IncorrectCredentialsException) {
                Log_Activity.LogActivity(usernameTextField.getText(), "Invalid Credentials");
                warningAlert.showAlert(rb.getString("loginError"), rb.getString("incorrectCredentials")); }

            // Corrects error that causes unexpected Application Closure
            if (e instanceof RuntimeException) { /* Cancel Exiting Application */ }
        }
    }


    /**
     * Initializes Scene.
     * Detects System language and translates text boxes and labels.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Translates all visible text for the User based on Locale.
        rb = ResourceBundle.getBundle("UserLogin", Locale.getDefault());
        titleLabel       .setText      (rb.getString("title"));
        userloginLabel   .setText      (rb.getString("userLogin"));
        timezoneLabel    .setText      (rb.getString("timezone") + ": [" + ZoneId.systemDefault() + "]");
        usernameTextField.setPromptText(rb.getString("username"));
        passwordField    .setPromptText(rb.getString("password"));
        signinButton     .setText      (rb.getString("signin"));
    }
}
