package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.ContactDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.UserDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.EmptyFieldsException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.UserIDNotValidException;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.c195_software_ii__advanced_java_concepts_pa.Utilities.ShowAlert.showAlert;

public class AppointmentController implements Initializable {

    Stage  stage;
    Parent scene;

    ObservableList<Customer>      customerList       = FXCollections.observableArrayList();
    ObservableList<Contact>       contactList        = FXCollections.observableArrayList();
    ObservableList<ZonedDateTime> availableTimeSlots = FXCollections.observableArrayList();
    ObservableList<String>        startTimes         = FXCollections.observableArrayList();
    ObservableList<String>        endTimes           = FXCollections.observableArrayList();

    DateTimeFormatter datePickerFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
    DateTimeFormatter timeComboBoxFormat = DateTimeFormatter.ofPattern("hh:mm a v");

    Business business = new Business();

    Boolean updatingAppointment = false;

    @FXML private Label              appointmentPageLabel;
    @FXML private TextField          appointmentIDTextField;
    @FXML private TextField          titleTextField;
    @FXML private Button             cancelButton;
    @FXML private Button             saveAppointmentButton;
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
        appointmentIDTextField.setText(String.valueOf(appointment.getAppointmentID()));
        titleTextField.setText(appointment.getTitle());
        typeTextField.setText(appointment.getType());
        customerComboBox.getSelectionModel().select(selectCustomer(appointment));
        contactComboBox.getSelectionModel().select(selectContact(appointment));
        userIDTextField.setText(String.valueOf(appointment.getUserID()));
        locationTextField.setText(appointment.getLocation());
        descriptionTextArea.setText(appointment.getDescription());

        startDateDatePicker.getEditor().setText(appointment.getDateTimeStart().toLocalDateTime().toLocalDate().format(datePickerFormat));
        endDateDatePicker.getEditor().setText(appointment.getDateTimeEnd().toLocalDateTime().toLocalDate().format(datePickerFormat));

        startTimeComboBox.getSelectionModel().select(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));
        endTimeComboBox.getSelectionModel().select(appointment.getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));

        updatingAppointment = true;
        appointmentPageLabel.setText("Update Appointment");
        saveAppointmentButton.setText("Update Appointment");
    }

    public Customer selectCustomer(Appointment appointment) {
        for (Customer customer : customerList) {
            if (customer.getCustomerID() == appointment.getCustomerID()) {
                return customer;
            }
        }
        return null;
    }

    public Contact selectContact(Appointment appointment) {
        for (Contact contact : contactList) {
            if (contact.getContactID() == appointment.getContactID()) {
                return contact;
            }
        }
        return null;
    }

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        // If no entries have been made, exit without prompt
        if (titleTextField.getText().isBlank()
                && typeTextField.getText().isBlank()
                && customerComboBox.getSelectionModel().isEmpty()
                && contactComboBox.getSelectionModel().isEmpty()
                && userIDTextField.getText().isBlank()
                && locationTextField.getText().isBlank()
                && startTimeComboBox.getSelectionModel().isEmpty()
                && endTimeComboBox.getSelectionModel().isEmpty()
                && descriptionTextArea.getText().isBlank()) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will cancel any changes made. Do you want to continue?");

            // Wait for response from user
            Optional<ButtonType> result = alert.showAndWait();

            // If 'OK' pressed, continue to main page.
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    @FXML
    void onActionSaveAppointment(ActionEvent event) throws IOException {
        try {
            // If any fields empty, alert user
            if (titleTextField.getText().isBlank()
                    || typeTextField.getText().isBlank()
                    || customerComboBox.getSelectionModel().isEmpty()
                    || contactComboBox.getSelectionModel().isEmpty()
                    || userIDTextField.getText().isBlank()
                    || locationTextField.getText().isBlank()
                    || startDateDatePicker.getEditor().getText().isBlank()
                    || endDateDatePicker.getEditor().getText().isBlank()
                    || startTimeComboBox.getSelectionModel().isEmpty()
                    || endTimeComboBox.getSelectionModel().isEmpty()
                    || descriptionTextArea.getText().isBlank()) {
                throw new EmptyFieldsException(); }

            if (UserDBImpl.getUser(userIDTextField.getText()) == null) {
                throw new UserIDNotValidException(); }

            //Fill variables with values from fields
            int newAppointmentID          = Integer.parseInt(appointmentIDTextField.getText());
            String appointmentTitle       = titleTextField.getText();
            String appointmentType        = typeTextField.getText();
            int appointmentCustomerID     = customerComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            int appointmentContactID      = contactComboBox.getSelectionModel().getSelectedItem().getContactID();
            int appointmentUserID         = Integer.parseInt(userIDTextField.getText());
            String appointmentLocation    = locationTextField.getText();
            String appointmentDescription = descriptionTextArea.getText();

            LocalDate startDate = LocalDate.parse(startDateDatePicker.getEditor().getText(), datePickerFormat);
            LocalDate endDate   = LocalDate.parse(endDateDatePicker.getEditor().getText(), datePickerFormat);

            LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), timeComboBoxFormat);
            LocalTime endTime   = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), timeComboBoxFormat);

            ZonedDateTime zonedStartTime = ZonedDateTime.of(startDate, startTime, ZoneId.systemDefault());
            ZonedDateTime zonedEndTime = ZonedDateTime.of(endDate, endTime, ZoneId.systemDefault());

            if (updatingAppointment) {
                AppointmentDBImpl.updateAppointment(newAppointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, zonedStartTime, zonedEndTime, appointmentCustomerID,
                        appointmentUserID, appointmentContactID);
            }
            else {
                AppointmentDBImpl.createAppointment(newAppointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, zonedStartTime, zonedEndTime, appointmentCustomerID,
                        appointmentUserID, appointmentContactID);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e) {
            if (e instanceof EmptyFieldsException) {
                showAlert("Warning Dialog", "All fields must have a value before continuing."); }
            if (e instanceof UserIDNotValidException) {
                showAlert("Warning Dialog", "Not a valid User ID"); }
            if (e instanceof SQLException) {
                e.printStackTrace(); }
        }
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

        String todayDate = LocalDate.now().format(datePickerFormat);
        startDateDatePicker.getEditor().setText(todayDate);
        endDateDatePicker.getEditor().setText(todayDate);

        availableTimeSlots.add(business.getBusinessOpenTime());

        while (ChronoUnit.MINUTES.between(availableTimeSlots.get(availableTimeSlots.size() - 1), business.getBusinessClosingTime()) > 0) {
             availableTimeSlots.add(availableTimeSlots.get(availableTimeSlots.size() - 1).plusMinutes(15));
        }

        int index = 0;
        for (ZonedDateTime timeSlot : availableTimeSlots) {
            if (index < availableTimeSlots.size() - 1) {
                startTimes.add(timeSlot.withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));
            }
            if (index > 0) {
                endTimes.add(timeSlot.withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));
            }
            index++;
        }

        startTimeComboBox.setItems(startTimes);
        endTimeComboBox.setItems(endTimes);
    }

}
