package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.ContactDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Business;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    Stage  stage;
    Parent scene;

    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    @FXML private TextField         appointmentIDTextField;
    @FXML private Button            cancelButton;
    @FXML private Button            createAppointmentButton;
    @FXML private TextField         customerIDTextField;
    @FXML private TextField         customerNameTextField;
    @FXML private TextField         typeTextField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private TextArea          descriptionTextArea;
    @FXML private DatePicker        endDateDatePicker;
    @FXML private ComboBox<String> endTimeComboBox;
    @FXML private TextField         locationTextField;
    @FXML private DatePicker        startDateDatePicker;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private TextField         userIDTextField;

    public int getAvailableAppointmentID() throws SQLException {
        int newAppointmentID = -1;

        for (Appointment appointment : AppointmentDBImpl.getAllAppointments()) {
            if (appointment.getAppointmentID() > newAppointmentID)
                newAppointmentID = appointment.getAppointmentID();
        }
        return newAppointmentID + 1;
    }

    public void sendAppointment (Appointment appointment) {
        // WIP
    }

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionCreateAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try { appointmentIDTextField.setText(String.valueOf(getAvailableAppointmentID())); } catch (SQLException e) { e.printStackTrace(); }

        contactList.clear();

        try {
            contactList.setAll(ContactDBImpl.getContacts());
        } catch (SQLException e) { e.printStackTrace(); }

        contactComboBox.setItems(contactList);

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/dd/YYYY");
        String formattedDate = localDate.format(dateFormatter);
        startDateDatePicker.getEditor().setText(formattedDate);
        endDateDatePicker.getEditor().setText(formattedDate);

        Business business = new Business();

        ObservableList<ZonedDateTime> availableTimeSlots = FXCollections.observableArrayList();
        availableTimeSlots.add(business.getBusinessOpenTime());

        while (ChronoUnit.MINUTES.between(availableTimeSlots.get(availableTimeSlots.size() - 1), business.getBusinessClosingTime()) > 0) {
             availableTimeSlots.add(availableTimeSlots.get(availableTimeSlots.size() - 1).plusMinutes(15));
        }

        ObservableList<String> startTimes = FXCollections.observableArrayList();
        ObservableList<String> endTimes = FXCollections.observableArrayList();

        int index = 0;
        for (ZonedDateTime timeSlot : availableTimeSlots) {
            if (index < availableTimeSlots.size() - 1) {
                startTimes.add(timeSlot.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("hh:mm a v")));
            }
            if (index > 0) {
                endTimes.add(timeSlot.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("hh:mm a v")));
            }
            index++;
        }

        startTimeComboBox.setItems(startTimes);
        endTimeComboBox.setItems(endTimes);

    }

}
