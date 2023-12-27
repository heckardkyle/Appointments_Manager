package com.c195_software_ii__advanced_java_concepts_pa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

        @FXML private Button addCustomerButton;
        @FXML private TextField addressTextField;
        @FXML private Button cancelButton;
        @FXML private ComboBox<?> countryChoiceBox;
        @FXML private TextField customerIDTextField;
        @FXML private TextField customerNameTextField;
        @FXML private TextField phoneNumberTextField;
        @FXML private ComboBox<?> stateChoiceBox;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
