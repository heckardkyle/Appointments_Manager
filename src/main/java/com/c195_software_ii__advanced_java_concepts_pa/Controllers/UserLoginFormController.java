package com.c195_software_ii__advanced_java_concepts_pa.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserLoginFormController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signinButton;

    @FXML
    private Label timezoneLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label userloginLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    void onActionSignIn(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
