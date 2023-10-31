package com.c195_software_ii__advanced_java_concepts_pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class UserLoginFormController implements Initializable {

    Stage stage;
    Parent scene;

    ResourceBundle rb;

    @FXML private Label     titleLabel;
    @FXML private Label     userloginLabel;
    @FXML private Label     timezoneLabel;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button    signinButton;

    @FXML void onActionSignIn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rb = ResourceBundle.getBundle("UserLogin", Locale.getDefault());
        titleLabel.setText(rb.getString("title"));
        userloginLabel.setText(rb.getString("userLogin"));
        timezoneLabel.setText(rb.getString("timezone") + ": [" + ZoneId.systemDefault() + "]");
        usernameTextField.setPromptText(rb.getString("username"));
        passwordTextField.setPromptText(rb.getString("password"));
        signinButton.setText(rb.getString("signin"));
    }
}
