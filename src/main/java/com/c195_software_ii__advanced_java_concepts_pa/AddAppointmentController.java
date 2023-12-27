package com.c195_software_ii__advanced_java_concepts_pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    Stage  stage;
    Parent scene;

    @FXML private TextField appointmentIDTextField;
    @FXML private Button cancelButton;
    @FXML private Button createAppointmentButton;
    @FXML private TextField customerIDTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField typeTextField;
    @FXML private ChoiceBox<?> contactChoiceBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker endDateDatePicker;
    @FXML private ChoiceBox<?> endTimeChoiceBox;
    @FXML private TextField locationTextField;
    @FXML private DatePicker startDateDatePicker;
    @FXML private ChoiceBox<?> startTimeChoiceBox;
    @FXML private TextField userIDTextField;

    @FXML void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void onActionCreateAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
