package com.c195_software_ii__advanced_java_concepts_pa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {
    @FXML private TextField appointmentIDTextField;
    @FXML private Button cancelButton;
    @FXML private ChoiceBox<?> contactChoiceBox;
    @FXML private TextField customerIDTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker endDateDatePicker;
    @FXML private ChoiceBox<?> endTimeChoiceBox;
    @FXML private TextField locationTextField;
    @FXML private DatePicker startDateDatePicker;
    @FXML private ChoiceBox<?> startTimeChoiceBox;
    @FXML private TextField typeTextField;
    @FXML private Button updateAppointmentButton;
    @FXML private TextField userIDTextField;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
