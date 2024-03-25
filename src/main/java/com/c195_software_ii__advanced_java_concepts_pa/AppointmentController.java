package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.ContactDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Business;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Contact;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    Stage  stage;
    Parent scene;

    ObservableList<Customer>      customerList       = FXCollections.observableArrayList();
    ObservableList<Contact>       contactList        = FXCollections.observableArrayList();
    ObservableList<ZonedDateTime> availableTimeSlots = FXCollections.observableArrayList();
    ObservableList<String>        startTimes         = FXCollections.observableArrayList();
    ObservableList<String>        endTimes           = FXCollections.observableArrayList();

    @FXML private TextField          appointmentIDTextField;
    @FXML private TextField          titleTextField;
    @FXML private Button             cancelButton;
    @FXML private Button             createAppointmentButton;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private TextField          typeTextField;
    @FXML private ComboBox<Contact>  contactComboBox;
    @FXML private TextArea           descriptionTextArea;
    @FXML private DatePicker         endDateDatePicker;
    @FXML private ComboBox<String>   endTimeComboBox;
    @FXML private TextField          locationTextField;
    @FXML private DatePicker         startDateDatePicker;
    @FXML private ComboBox<String>   startTimeComboBox;
    @FXML private TextField          userIDTextField;

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
        try {
            int newAppointmentID          = Integer.parseInt(appointmentIDTextField.getText());
            String appointmentTitle       = titleTextField.getText();
            String appointmentType        = typeTextField.getText();
            int appointmentCustomerID     = customerComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            int appointmentContactID      = contactComboBox.getSelectionModel().getSelectedItem().getContactID();
            int appointmentUserID         = Integer.parseInt(userIDTextField.getText());
            String appointmentLocation    = locationTextField.getText();
            String appointmentDescription = descriptionTextArea.getText();

            DateTimeFormatter currentDateFormat   = DateTimeFormatter.ofPattern("M/dd/yyyy");
            DateTimeFormatter timeStampDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateDatePicker.getEditor().getText(), currentDateFormat);
            LocalDate endDate   = LocalDate.parse(endDateDatePicker.getEditor().getText(), currentDateFormat);
            startDate.format(timeStampDateFormat);
            endDate.format(timeStampDateFormat);

            DateTimeFormatter currentTimeFormat   = DateTimeFormatter.ofPattern("hh:mm a v");
            DateTimeFormatter timeStampTimeFormat = DateTimeFormatter.ofPattern("hh:mm:ss.S");
            LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), currentTimeFormat);
            LocalTime endTime   = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), currentTimeFormat);
            startTime.atDate(LocalDate.now(ZoneId.systemDefault()));
            endTime.atDate(LocalDate.now(ZoneId.systemDefault()));
            startTime.atOffset(ZoneOffset.of("Z"));
            endTime.atOffset(ZoneOffset.of("Z"));
            startTime.format(timeStampTimeFormat);
            endTime.format(timeStampTimeFormat);
            Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(startDate, startTime));
            Timestamp endDateTime   = Timestamp.valueOf(LocalDateTime.of(endDate, endTime));

            AppointmentDBImpl.createAppointment(newAppointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentType, startDateTime, endDateTime, appointmentCustomerID,
                    appointmentUserID, appointmentContactID);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (Exception e) {}

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerList.clear();
        contactList.clear();
        availableTimeSlots.clear();
        startTimes.clear();
        endTimes.clear();

        try {
            appointmentIDTextField.setText(String.valueOf(getAvailableAppointmentID()));
            customerList.setAll(CustomerDBImpl.getAllCustomers());
            contactList.setAll(ContactDBImpl.getContacts());
        } catch (SQLException e) { e.printStackTrace(); }

        customerComboBox.setItems(customerList);
        contactComboBox.setItems(contactList);

        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        startDateDatePicker.getEditor().setText(todayDate);
        endDateDatePicker.getEditor().setText(todayDate);

        Business business = new Business();

        availableTimeSlots.add(business.getBusinessOpenTime());

        while (ChronoUnit.MINUTES.between(availableTimeSlots.get(availableTimeSlots.size() - 1), business.getBusinessClosingTime()) > 0) {
             availableTimeSlots.add(availableTimeSlots.get(availableTimeSlots.size() - 1).plusMinutes(15));
        }

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
