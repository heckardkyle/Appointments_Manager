package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.ContactDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.UserDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.EndBeforeStartException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.EmptyFieldsException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.NotValidDateException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.UserIDNotValidException;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.AppointmentOverlapException;
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

/**
 * Appointment page for updating and creating new appointments.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class AppointmentController implements Initializable {

    /* --Stage Declarations-- */
    Stage  stage;
    Parent scene;

    /* --Observable Lists-- */
    ObservableList<Customer>      customerList       = FXCollections.observableArrayList();
    ObservableList<Contact>       contactList        = FXCollections.observableArrayList();
    ObservableList<ZonedDateTime> availableTimeSlots = FXCollections.observableArrayList();
    ObservableList<String>        startTimes         = FXCollections.observableArrayList();
    ObservableList<String>        endTimes           = FXCollections.observableArrayList();

    /* --DateTimeFormatters-- */
    DateTimeFormatter datePickerFormat   = DateTimeFormatter.ofPattern("M/d/yyyy");
    DateTimeFormatter timeComboBoxFormat = DateTimeFormatter.ofPattern("hh:mm a v");

    /* --Global Objects-- */
    Business business = new Business();

    /* --Booleans-- */
    Boolean updatingAppointment = false;

    /* --FXMLs-- */
    @FXML private Label              appointmentPageLabel;
    @FXML private TextField          appointmentIDTextField;
    @FXML private TextField          titleTextField;
    @FXML private TextField          typeTextField;
    @FXML private TextField          userIDTextField;
    @FXML private TextField          locationTextField;
    @FXML private TextArea           descriptionTextArea;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<Contact>  contactComboBox;
    @FXML private ComboBox<String>   startTimeComboBox;
    @FXML private ComboBox<String>   endTimeComboBox;
    @FXML private DatePicker         startDateDatePicker;
    @FXML private DatePicker         endDateDatePicker;
    @FXML private Button             saveAppointmentButton;
    @FXML private Button             cancelButton;

    /**
     * Finds the next available appointmentID.
     * @return <code>newAppointmentID</code>
     * @throws SQLException
     */
    public int getAvailableAppointmentID() throws SQLException {
        // Declare variable to store largest appointmentID used
        int newAppointmentID = 0;

        // Look for largest appointmentID in database
        for (Appointment appointment : AppointmentDBImpl.getAllAppointments()) {
            if (appointment.getAppointmentID() > newAppointmentID)
                newAppointmentID = appointment.getAppointmentID();
        }
        // return next available ID
        return newAppointmentID + 1;
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

    /**
     * Sends appointment to Appointment page.
     * When update button is pressed in AppointmentCustomerPage, the selected appointment's information is sent to the
     * Appointment page to fill each of the fields, and the page is set to "update mode".
     * @param appointment the appointment to update
     */
    public void sendAppointment (Appointment appointment) {
        // Fill fields with sent appointment's info
        appointmentIDTextField.setText(String.valueOf(appointment.getAppointmentID()));
        titleTextField.setText(appointment.getTitle());
        typeTextField.setText(appointment.getType());
        customerComboBox.getSelectionModel().select(selectCustomer(appointment));
        contactComboBox.getSelectionModel().select(selectContact(appointment));
        userIDTextField.setText(String.valueOf(appointment.getUserID()));
        locationTextField.setText(appointment.getLocation());
        descriptionTextArea.setText(appointment.getDescription());

        // Set dates of datepickers to appointment dates
        startDateDatePicker.getEditor().setText(appointment.getDateTimeStart().toLocalDateTime().toLocalDate().format(datePickerFormat));
        endDateDatePicker.getEditor().setText(appointment.getDateTimeEnd().toLocalDateTime().toLocalDate().format(datePickerFormat));

        // Set times in start time and end time ComboBoxes to sent appointments times
        startTimeComboBox.getSelectionModel().select(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));
        endTimeComboBox.getSelectionModel().select(appointment.getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));

        // Set page to update mode
        updatingAppointment = true;
        appointmentPageLabel.setText("Update Appointment");
        saveAppointmentButton.setText("Update Appointment");
    }

    /**
     * Sets End date to same day as Start Date.
     * When date is selected in StartDate DatePickers, set date in EndDate DatePicker to same day.
     * @param event Date picked in StartDate DatePicker
     * @throws IOException
     */
    @FXML
    void onActionSetEndDate(ActionEvent event) throws IOException {
        // Set endDateDatePicker date to same as startDateDatePicker
        endDateDatePicker.getEditor().setText(startDateDatePicker.getEditor().getText());
    }

    /**
     * Sends user back to AppointmentCustomerPage.
     * If any fields are filled, user is prompted to continue.
     * @param event Cancel button is pressed
     * @throws IOException
     */
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
        // Else, ask if user would like to continue
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

    /**
     * Create/Update Appointment after button is pressed.
     * Afterwords, user is sent to AppointmentCustomerPage. If any values are empty, user is alerted.
     * @param event Create/Update Appointment Button is pressed
     * @throws IOException
     */
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

            //Fill variables with values from fields
            int newAppointmentID          = Integer.parseInt(appointmentIDTextField.getText());
            String appointmentTitle       = titleTextField.getText();
            String appointmentType        = typeTextField.getText();
            int appointmentCustomerID     = customerComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            int appointmentContactID      = contactComboBox.getSelectionModel().getSelectedItem().getContactID();
            int appointmentUserID         = Integer.parseInt(userIDTextField.getText());
            String appointmentLocation    = locationTextField.getText();
            String appointmentDescription = descriptionTextArea.getText();

            // Grab dates from datePickers
            LocalDate startDate = LocalDate.parse(startDateDatePicker.getEditor().getText(), datePickerFormat);
            LocalDate endDate   = LocalDate.parse(endDateDatePicker.getEditor().getText(), datePickerFormat);

            // Grab selected Times from ComboBoxes
            LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), timeComboBoxFormat);
            LocalTime endTime   = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), timeComboBoxFormat);

            // Convert Dates and Times to ZonedDateTime with systemDefault TimeZone
            ZonedDateTime zonedStartTime = ZonedDateTime.of(startDate, startTime, ZoneId.systemDefault());
            ZonedDateTime zonedEndTime = ZonedDateTime.of(endDate, endTime, ZoneId.systemDefault());

            // Checks if User ID is valid
            if (UserDBImpl.getUser(userIDTextField.getText()) == null) {
                throw new UserIDNotValidException(); }

            // Checks if end time is before start Time
            if (zonedEndTime.isBefore(zonedStartTime) || zonedEndTime.isEqual(zonedStartTime)) {
                throw new EndBeforeStartException(); }

            // Checks if start and end time are during business hours
            if (!business.getBusinessDaysOfWeekOpen().contains(zonedStartTime.getDayOfWeek()) ||
                    !business.getBusinessDaysOfWeekOpen().contains(zonedEndTime.getDayOfWeek())) {
                throw new NotValidDateException(); }

            //Checks if appointment would overlap another appointment of the selected customer
            for (Appointment appointment : AppointmentDBImpl.getAllAppointments()) {

                // For each appointment in database, if it has same customer ID, but not same appointmentID
                if (appointment.getCustomerID() == appointmentCustomerID
                        && !(appointment.getAppointmentID() == Integer.parseInt(userIDTextField.getText()))) {

                    // Get time from appointments to compare
                    ZonedDateTime startTime1 = appointment.getDateTimeStart();
                    ZonedDateTime endTime1 = appointment.getDateTimeEnd();
                    ZonedDateTime startTime2 = zonedStartTime;
                    ZonedDateTime endTime2 = zonedEndTime;

                    // Compare Times
                    boolean predicate1 = startTime2.isBefore(startTime1);
                    boolean predicate2 = endTime2.isBefore(startTime1) || endTime2.isEqual(startTime1);
                    boolean predicate3 = startTime2.isEqual(endTime1) || startTime2.isAfter(endTime1);
                    boolean predicate4 = endTime2.isAfter(endTime1);

                    // If overlap, throw exception
                    if (!((predicate1 && predicate2) || (predicate3 && predicate4))) {
                        throw new AppointmentOverlapException();
                    }
                }
            }

            // If in update mode, update appointment
            if (updatingAppointment) {
                AppointmentDBImpl.updateAppointment(newAppointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, zonedStartTime, zonedEndTime, appointmentCustomerID,
                        appointmentUserID, appointmentContactID);
            }
            // Otherwise, create new appointment
            else {
                AppointmentDBImpl.createAppointment(newAppointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, zonedStartTime, zonedEndTime, appointmentCustomerID,
                        appointmentUserID, appointmentContactID);
            }

            // Send user to AppointmentCustomerPage
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // If any exceptions are thrown, catch and do not update or create
        catch (Exception e) {
            if (e instanceof EmptyFieldsException) {
                showAlert("Warning Dialog", "All fields must have a value before continuing."); }
            if (e instanceof UserIDNotValidException) {
                showAlert("Warning Dialog", "Not a valid User ID"); }
            if (e instanceof EndBeforeStartException) {
                showAlert("Warning Dialog", "Appointment must end after appointment start period"); }
            if (e instanceof NotValidDateException) {
                showAlert("Warning Dialog", "Appointment must take place on a business date"); }
            if (e instanceof SQLException) {
                e.printStackTrace(); }
            if (e instanceof AppointmentOverlapException) {
                showAlert("Warning Dialog", "Appointment overlaps existing appointment");
            }
        }
    }

    /**
     * Initializes page.
     * Sets the appointmentIDTextField, ComboBoxes, and DatePickers
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Clear lists
        customerList.clear();
        contactList.clear();
        availableTimeSlots.clear();
        startTimes.clear();
        endTimes.clear();

        try {
            // Fill appointmentID field and fill lists for customer and contact ComboBoxes
            appointmentIDTextField.setText(String.valueOf(getAvailableAppointmentID()));
            customerList.setAll(CustomerDBImpl.getAllCustomers());
            contactList.setAll(ContactDBImpl.getContacts());
        } catch (SQLException e) { e.printStackTrace(); }

        // Assign lists to ComboBoxes
        customerComboBox.setItems(customerList);
        contactComboBox.setItems(contactList);

        // Setup DatePickers to Today's Date
        String todayDate = LocalDate.now().format(datePickerFormat);
        startDateDatePicker.getEditor().setText(todayDate);
        endDateDatePicker.getEditor().setText(todayDate);

        // Add business open time to available time slots
        availableTimeSlots.add(business.getBusinessOpenTime());

        // Iterate and add every 15 minutes to availableTimeSlots until close time
        while (ChronoUnit.MINUTES.between(availableTimeSlots.get(availableTimeSlots.size() - 1), business.getBusinessClosingTime()) > 0) {
             availableTimeSlots.add(availableTimeSlots.get(availableTimeSlots.size() - 1).plusMinutes(15));
        }

        // For each in availableTimeSlots, add to startTime and end times
        int index = 0;
        for (ZonedDateTime timeSlot : availableTimeSlots) {
            // start times does not include last index
            if (index < availableTimeSlots.size() - 1) {
                startTimes.add(timeSlot.withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));
            }
            // end times does not include first index
            if (index > 0) {
                endTimes.add(timeSlot.withZoneSameInstant(ZoneId.systemDefault()).format(timeComboBoxFormat));
            }
            index++;
        }

        // assign lists to start and end time ComboBoxes
        startTimeComboBox.setItems(startTimes);
        endTimeComboBox.setItems(endTimes);
    }

}
