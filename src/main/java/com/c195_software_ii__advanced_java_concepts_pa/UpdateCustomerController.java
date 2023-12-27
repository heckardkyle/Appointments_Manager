package com.c195_software_ii__advanced_java_concepts_pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField addressTextField;
    @FXML private Button cancelButton;
    @FXML private ComboBox<?> countryChoiceBox;
    @FXML private TextField customerIDTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private ComboBox<?> stateChoiceBox;
    @FXML private Button updateCustomerButton;

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
