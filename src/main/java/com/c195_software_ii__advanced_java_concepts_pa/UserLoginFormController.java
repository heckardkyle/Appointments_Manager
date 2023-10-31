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

    Stage  stage;
    Parent scene;

    ResourceBundle rb;

    @FXML private Label         titleLabel;
    @FXML private Label         userloginLabel;
    @FXML private Label         timezoneLabel;
    @FXML private TextField     usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button        signinButton;

    public class UsernameEmptyException extends Exception {
        public UsernameEmptyException() {} }
    public class PasswordEmptyException extends Exception {
        public PasswordEmptyException() {} }
    public class IncorrectCredentialsException extends Exception {
        public IncorrectCredentialsException() {} }


    @FXML void onActionSignIn(ActionEvent event) throws Exception {
        try {
            if (usernameTextField.getText().isEmpty()) { throw new UsernameEmptyException(); }
            if (passwordField.getText().isEmpty()) { throw new PasswordEmptyException(); }

            if (UserDBImpl.GetUser(usernameTextField.getText(), passwordField.getText()) != null) {

                Log_Activity.LogActivity(usernameTextField.getText(), "Successfully Logged In");

                LoginAlert.LoginAlert();

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out and close the program?");

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            JDBC.closeConnection();
                            System.exit(0);
                        }
                    }
                });
            }
            else { throw new IncorrectCredentialsException(); }
        }
        catch (UsernameEmptyException e1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("loginError"));
            alert.setContentText(rb.getString("usernameEmpty"));
            alert.showAndWait();
        }
        catch (PasswordEmptyException e2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("loginError"));
            alert.setContentText(rb.getString("passwordEmpty"));
            alert.showAndWait();
        }
        catch (IncorrectCredentialsException e3) {
            Log_Activity.LogActivity(usernameTextField.getText(), "Invalid Credentials");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("loginError"));
            alert.setContentText(rb.getString("incorrectCredentials"));
            alert.showAndWait();
        }
    }



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
