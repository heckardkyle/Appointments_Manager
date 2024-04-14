package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.*;
import com.c195_software_ii__advanced_java_concepts_pa.Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.c195_software_ii__advanced_java_concepts_pa.Utilities.ShowAlert.showAlert;

/**
 * The Main Form of the application.
 * This page includes three tabs: Appointments, Customers, Reports.
 * Appointments Tab displays all appointments scheduled either by month or by week.
 * Customer tab displays all customers in Database.
 * Reports Tab Contains different options for reports displayed in accordion containers, give the user options to
 * choose from in ComboBoxes and generates the relevant information in the TableView.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class AppointmentCustomerPageController implements Initializable {

    /* --Stage Declarations-- */
    Stage  stage;
    Parent scene;

    /* --Appointment Table Lists-- */
    ObservableList<Contact>     contactList              = FXCollections.observableArrayList();
    ObservableList<Appointment> appointmentList          = FXCollections.observableArrayList();
    ObservableList<Appointment> monthlyTableAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> weeklyTableAppointments  = FXCollections.observableArrayList();

    /* --Customer Table Lists-- */
    ObservableList<Customer>           customerList = FXCollections.observableArrayList();
    ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

    /* --Reports Table and Accordion Lists-- */
    ObservableList<String>      appointmentAccordionList = FXCollections.observableArrayList();
    ObservableList<String>      contactsAccordionList    = FXCollections.observableArrayList();
    ObservableList<String>      usersAccordionList       = FXCollections.observableArrayList();
    ObservableList<Appointment> reportsAppointmentList   = FXCollections.observableArrayList();
    ObservableList<Contact>     reportsContactList       = FXCollections.observableArrayList();
    ObservableList<User>        reportsUserList          = FXCollections.observableArrayList();
    ObservableList<String>      comboBoxList1            = FXCollections.observableArrayList();
    ObservableList<String>      comboBoxList2            = FXCollections.observableArrayList();

    /* --DateTimeFormatters-- */
    DateTimeFormatter tableFormat      = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a v");
    DateTimeFormatter datePickerFormat = DateTimeFormatter.ofPattern("M/d/yyyy");

    /* --Appointment FXMLs-- */
    @FXML private Tab          appointmentsTab;
    @FXML private ToggleGroup  MonthlyWeeklyTG;
    @FXML private ToggleButton monthlyViewButton;
    @FXML private ToggleButton weeklyViewButton;
    @FXML private Label        weekMonthLabel;
    @FXML private DatePicker   datePicker;
    @FXML private TableView  <Appointment>          appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> appointmentIDCol;
    @FXML private TableColumn<Appointment, String>  titleCol;
    @FXML private TableColumn<Appointment, String>  descriptionCol;
    @FXML private TableColumn<Appointment, String>  locationCol;
    @FXML private TableColumn<Appointment, String>  contactCol;
    @FXML private TableColumn<Appointment, String>  typeCol;
    @FXML private TableColumn<Appointment, String>  startDateTimeCol;
    @FXML private TableColumn<Appointment, String>  endDateTimeCol;
    @FXML private TableColumn<Appointment, Integer> customerIDCol;
    @FXML private TableColumn<Appointment, Integer> UserIDCol;
    @FXML private Button newAppointmentButton;
    @FXML private Button updateAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button appointmentlogoutButton;

    /* --Customer FXMLs-- */
    @FXML private Tab customersTab;
    @FXML private TableView  <Customer>          customerTableView;
    @FXML private TableColumn<Customer, Integer> customerCustomerIDCol;
    @FXML private TableColumn<Customer, String>  customerCustomerNameCol;
    @FXML private TableColumn<Customer, String>  customerCustomerAddressCol;
    @FXML private TableColumn<Customer, String>  customerPostalCodeCol;
    @FXML private TableColumn<Customer, String>  customerPhoneNumberCol;
    @FXML private TableColumn<Customer, String>  customerDivisionCol;
    @FXML private Button newCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button customerLogoutButton;

    /* --Reports FXMLs-- */
    @FXML private ListView<String> appointmentsAccordianListView;
    @FXML private ListView<String> contactsAccordianListView;
    @FXML private ListView<String> usersAccordianListView;
    @FXML private ComboBox<String> reportsComboBox1;
    @FXML private ComboBox<String> reportsComboBox2;
    @FXML private Label            reportsTotalLabel;
    @FXML private TableView<Appointment>          reportsTableView;
    @FXML private TableColumn<Appointment,String> reportsTableViewC1;
    @FXML private TableColumn<Appointment,String> reportsTableViewC2;
    @FXML private TableColumn<Appointment,String> reportsTableViewC3;
    @FXML private TableColumn<Appointment,String> reportsTableViewC4;
    @FXML private TableColumn<Appointment,String> reportsTableViewC5;
    @FXML private TableColumn<Appointment,String> reportsTableViewC6;
    @FXML private TableColumn<Appointment,String> reportsTableViewC7;
    @FXML private Button reportsLogoutButton;

    /* ------------------- */
    /* --Appointment Tab-- */
    /* ------------------- */

    /**
     * Display appointments by month.
     * @param event Monthly button is pressed
     */
    @FXML void onActionMonthlyViewButton(ActionEvent event) {
        monthlyViewButton.setSelected(true); // Prevents button from being un-clicked
        appointmentTableView.setItems(monthlyTableAppointments);
        appointmentTableView.refresh();
    }

    /**
     * Display appointments by week.
     * @param event Weekly button is pressed
     */
    @FXML void onActionWeeklyViewButton(ActionEvent event) {
        weeklyViewButton.setSelected(true); // Prevents button from being un-clicked
        appointmentTableView.setItems(weeklyTableAppointments);
        appointmentTableView.refresh();
    }

    /**
     * Changes appointments show in TableView based on date picked.
     * When date is chosen, the monthly and weekly appointments are refreshed.
     * @param event date changed in DatePicker
     */
    @FXML void onActionDatePicker(ActionEvent event) {

        // clear filtered lists.
        monthlyTableAppointments.clear();
        weeklyTableAppointments .clear();

        // Parse date from DatePicker
        LocalDate datePickerLocalDate = LocalDate.parse(datePicker.getEditor().getText(), datePickerFormat);

        // Filter monthly appointments list by selected month
        monthlyTableAppointments.setAll(appointmentList.filtered(appointment -> {

            // Get Months to Compare
            Month appointmentMonth = appointment.getDateTimeStart().getMonth();
            Month datePickerMonth  = datePickerLocalDate.getMonth();

            // If Months are same, add to filter
            return appointmentMonth == datePickerMonth;
        }));

        // Filter weekly appointments list by selected week
        weeklyTableAppointments.setAll(appointmentList.filtered(appointment -> {

            // Get Weeks to compare
            long appointmentWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(appointment.getDateTimeStart());
            long datePickerWeek  = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(datePickerLocalDate);

            // If weeks are same, add to filter
            return appointmentWeek == datePickerWeek;
        }));

        // if monthly button is selected, display appointments by month
        if (monthlyViewButton.isSelected()) {
            appointmentTableView.setItems(monthlyTableAppointments);
        }

        // if weekly button is selected, display appointments by week
        if (weeklyViewButton.isSelected() ) {
            appointmentTableView.setItems(weeklyTableAppointments);
        }

        // refresh TableView
        appointmentTableView.refresh();
    }

    /**
     * Enables and disables appointment update and delete buttons if there's an active selection or not.
     * @param event Mouse click event on TableView
     */
    @FXML public void onMouseTableClick(MouseEvent event) {

        // If selection made, enable update and delete buttons
        if (!(appointmentTableView.getSelectionModel().getSelectedCells().isEmpty())) { // If selection made, enable buttons
            updateAppointmentButton.setDisable(false);
            deleteAppointmentButton.setDisable(false);
        }
        // If no selection made, disable update and delete buttons
        else {
            updateAppointmentButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
        }
    }

    /**
     * Send user to Appointment Page to create a new appointment.
     * @param event Add appointment button pressed
     * @throws IOException
     */
    @FXML void onActionCreateNewAppointment(ActionEvent event) throws IOException {
        // set new scene and send user to appointment page
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Send user to appointment page to update selected appointment.
     * @param event Update appointment button pressed.
     * @throws IOException
     */
    @FXML void onActionUpdateAppointment(ActionEvent event) throws IOException {

        // Load appointment page controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointment.fxml"));
        loader.load();

        // Get method from controller and send selected appointment
        AppointmentController appointmentController = loader.getController();
        appointmentController.sendAppointment(appointmentTableView.getSelectionModel().getSelectedItem());

        // set new scene and send user to appointment page
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Delete selected appointment and remove from database.
     * @param event Delete Appointment button pressed
     * @throws SQLException
     */
    @FXML void onActionDeleteAppointment(ActionEvent event) throws SQLException {

        // Ask user for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");

        // Wait for User response
        Optional<ButtonType> result = alert.showAndWait();

        // If user selects ok, delete selected appointment.
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Get selected appointment
            Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();

            // Delete appointment and alert user of deletion
            AppointmentDBImpl.deleteAppointment(appointment.getAppointmentID());
            String message = "Cancelled appointment:\n"
                    + "Appointment ID : "  + appointment.getAppointmentID() + "\n"
                    + "Appointment Type: " + appointment.getType();
            showAlert("Information", message);

            // reset tableview
            updateAppointmentButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
            appointmentList.clear();
            appointmentList.setAll(AppointmentDBImpl.getAllAppointments());
            appointmentTableView.refresh();
        }
    }

    /**
     * Logs User out of application and sends user to UserLoginForm page.
     * @param event Logout button pressed
     * @throws IOException
     */
    @FXML void onActionAppointmentLogout(ActionEvent event) throws IOException {

        // Ask User for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");

        // Wait for response
        Optional<ButtonType> result = alert.showAndWait();

        // If user selects ok, send user back to UserLoginForm page
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("UserLoginForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            // Disables Confirmation dialog when X button pressed after logout.
            stage.setOnCloseRequest(event1 -> {
                JDBC.closeConnection();
                System.exit(0);
            });
        }
    }

    /* ---------------- */
    /* --Customer Tab-- */
    /* ---------------- */

    /**
     * Enables and disables customer update and delete buttons if there's an active selection or not.
     * @param event Mouse click event on TableView
     */
    @FXML public void onMouseListClick(MouseEvent event) {

        // if selection is made, enable update and delete buttons
        if (!(customerTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            updateCustomerButton.setDisable(false);
            deleteCustomerButton.setDisable(false);
        }
        // If no selection made, disable update and delete buttons
        else {
            updateCustomerButton.setDisable(true);
            deleteCustomerButton.setDisable(true);
        }
    }

    /**
     * Send user to customer page to create new customer.
     * @param event add customer button pressed
     * @throws IOException
     */
    @FXML void onActionAddCustomer(ActionEvent event) throws IOException {
        // Set new scene and send user to customer page
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Send user to customer page to update selected customer.
     * @param event update customer button pressed
     * @throws IOException
     */
    @FXML void onActionUpdateCustomer(ActionEvent event) throws IOException {

        // Load CustomerPage Controller
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Customer.fxml"));
        loader.load();

        // Send selected customer info to Customer Page
        CustomerController customerController = loader.getController();
        customerController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem());

        // Set new scene and send user to customer page
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Delete Customer's appointments, customer, and remove customer from database.
     * @param event Delete Customer button pressed
     * @throws SQLException
     */
    @FXML void onActionDeleteCustomer(ActionEvent event) throws SQLException {

        // Ask User for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer and all associated appointments?");

        // Wait for User response
        Optional<ButtonType> result = alert.showAndWait();

        // If Customer selects ok, delete customers appointments, then delete customer
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // For each appointment in appointment list
            for (Appointment appointment : appointmentList) {

                // if appointment belongs to customer, delete appointment
                if (appointment.getCustomerID() == customerTableView.getSelectionModel().getSelectedItem().getCustomerID()) {
                    AppointmentDBImpl.deleteAppointment(appointment.getAppointmentID());
                }
            }
            // delete customer
            CustomerDBImpl.deleteCustomer(customerTableView.getSelectionModel().getSelectedItem().getCustomerID());

            // Refresh Appointment and Customer TableViews
            updateCustomerButton.setDisable(true);
            deleteCustomerButton.setDisable(true);
            appointmentList.clear();
            customerList.clear();
            appointmentList.setAll(AppointmentDBImpl.getAllAppointments());
            customerList.setAll(CustomerDBImpl.getAllCustomers());
            appointmentTableView.refresh();
            customerTableView.refresh();
        }
    }

    /**
     * Log User out of application and send User back to UserLoginForm page.
     * @param event Logout Button is pressed
     * @throws IOException
     */
    @FXML void onActionCustomerLogout(ActionEvent event) throws IOException {

        // Ask User for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");

        // Wait for User response
        Optional<ButtonType> result = alert.showAndWait();

        // If User selects ok, Send User back to UserLoginForm page
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Set new scene and send User to UserLoginForm page
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("UserLoginForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            // Disables Confirmation dialog when X button pressed after logout.
            stage.setOnCloseRequest(event1 -> {
                JDBC.closeConnection();
                System.exit(0);
            });
        }
    }

    /* --------------- */
    /* --Reports Tab-- */
    /* --------------- */

    /**
     * Disables and clears Reports TableView Columns, ComboBoxes, and Label.
     */
    public void setDisableVisibleReportsTab() {

        // Disable ComboBoxes and Label
        reportsComboBox1 .setDisable(true);
        reportsComboBox2 .setDisable(true);
        reportsComboBox1 .setVisible(false);
        reportsComboBox2 .setVisible(false);
        reportsTotalLabel.setVisible(false);

        // Disable TableView Columns
        reportsTableViewC1.setVisible(false);
        reportsTableViewC2.setVisible(false);
        reportsTableViewC3.setVisible(false);
        reportsTableViewC4.setVisible(false);
        reportsTableViewC5.setVisible(false);
        reportsTableViewC6.setVisible(false);
        reportsTableViewC7.setVisible(false);

        // Clear ComboBoxes and Lists
        comboBoxList1         .clear();
        comboBoxList2         .clear();
        reportsAppointmentList.clear();
        reportsContactList    .clear();
        reportsUserList       .clear();
    }

    /**
     * Sets up ComboBox1 base on Appointment Report option chosen.
     * @param event Selection made on list within Appointment Accordion.
     * @throws IOException
     * @throws SQLException
     */
    @FXML void onMouseClickedAppointmentListView(MouseEvent event) throws Exception {

        // If By Type selected, set up ComboBox1 to have types to select from
        if (appointmentsAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("By Type")) {

            // Reset Table and ComboBoxes
            setDisableVisibleReportsTab();
            contactsAccordianListView.getSelectionModel().clearSelection();
            usersAccordianListView   .getSelectionModel().clearSelection();

            // Find all Types available to choose from
            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());
            for (Appointment appointment : reportsAppointmentList) {
                if (!comboBoxList1.contains(appointment.getType())) {
                    comboBoxList1.add(appointment.getType());
                }
            }
            // Set all type in ComboBox1
            reportsComboBox1.setItems(comboBoxList1);

            // Enable ComboBox1 to be usable
            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("Type");
        }
        // If by Month is selected, set up ComboBox1 to have Years to select from
        if (appointmentsAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("By Month")) {

            // Reset Table and ComboBoxes
            setDisableVisibleReportsTab();
            contactsAccordianListView.getSelectionModel().clearSelection();
            usersAccordianListView   .getSelectionModel().clearSelection();

            // Find all Appointment Years available to choose from
            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());
            for (Appointment appointment : reportsAppointmentList) {
                String appointmentYear = String.valueOf(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getYear());
                if (!comboBoxList1.contains(appointmentYear)) {
                    comboBoxList1.add(appointmentYear);
                }
            }
            // Set all years in ComboBox1
            reportsComboBox1.setItems(comboBoxList1);

            // Enable ComboBox1 to be usable
            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("Year");
        }
    }

    /**
     * Sets up ComboBox1 based on Contact Report option chosen.
     * @param event Selection made on list within Contact Accordion
     * @throws Exception
     */
    @FXML void onMouseClickedContactsListView(MouseEvent event) throws Exception {

        // If schedule option chosen, set up ComboBox1 to have Contacts to choose from
        if (contactsAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("Schedule")) {

            // Reset Table and ComboBoxes
            setDisableVisibleReportsTab();
            appointmentsAccordianListView.getSelectionModel().clearSelection();
            usersAccordianListView       .getSelectionModel().clearSelection();

            // Retrieve all appointments
            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());

            // Get all Contact names
            reportsContactList.addAll(ContactDBImpl.getContacts());
            for (Contact contact : reportsContactList) {
                comboBoxList1.add(contact.getContactName());
            }
            // Put contact Name in ComboBox1
            reportsComboBox1.setItems(comboBoxList1);

            // Enable ComboBox1 to be usable
            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("Contact");
        }
    }

    /**
     * Sets up ComboBox1 base on Users Report option chosen.
     * @param event Selection made on list within Users Accordion
     * @throws Exception
     */
    @FXML void onMouseClickedUsersListView(MouseEvent event) throws Exception {

        // If Appointments option chosen, set up ComboBox1 to have Users to choose from
        if (usersAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("Appointments")) {

            // Reset Table and ComboBoxes
            setDisableVisibleReportsTab();
            appointmentsAccordianListView.getSelectionModel().clearSelection();
            contactsAccordianListView    .getSelectionModel().clearSelection();

            // Retrieve all appointments
            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());

            // Get all User IDs
            reportsUserList.addAll(UserDBImpl.getAllUserIDs());
            for (User user : reportsUserList) {
                comboBoxList1.add(String.valueOf(user.getUserID()));
            }
            // Put user IDs in ComboBox1
            reportsComboBox1.setItems(comboBoxList1);

            // Enable ComboBox1 to be usable
            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("User ID");
        }
    }

    /**
     * Generates a report from ComboBox1 or sets up ComboBox2, depending on report chosen.
     * @param event Selection made in ComboBox1
     */
    @FXML void onActionReportsComboBox1(ActionEvent event) {

        // If selection was made in ComboBox1; not null
        if (reportsComboBox1.getSelectionModel().getSelectedItem() != null) {


            // If report is for Appointments by Type
            if (reportsComboBox1.getPromptText().equalsIgnoreCase("Type")) {

                // Filter appointments by Type chosen in ComboBox1 and assign to TableView
                reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                        appointment.getType().equalsIgnoreCase(reportsComboBox1.getSelectionModel().getSelectedItem())));

                // Enable Necessary Table Columns
                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                // Name Table Columns
                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                // Assign members to Table Columns
                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));
                // Convert appointmentID to String
                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                // Get appointment Start Time in System TimeZone
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                // Get appointment End Time in System TimeZone
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                // Convert customerID to String
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                // Create String for Label
                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                // Enable Label and assign String to Label
                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }


            // If report is for Appointments by Month
            if (reportsComboBox1.getPromptText().equalsIgnoreCase("Year")) {

                // Based on Year selected in ComboBox1, set up ComboBox2 with Month to select from
                for (Appointment appointment : reportsAppointmentList.filtered(appointment ->
                        appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getYear()
                        == Integer.parseInt(reportsComboBox1.getSelectionModel().getSelectedItem()))) {
                    String appointmentMonth = String.valueOf(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getMonth());
                    if (!comboBoxList2.contains(appointmentMonth)) {
                        comboBoxList2.add(appointmentMonth);
                    }
                }
                // Assign Months to ComboBox2
                reportsComboBox2.setItems(comboBoxList2);

                // Enable ComboBox2 to be usable
                reportsComboBox2.setDisable(false);
                reportsComboBox2.setVisible(true);
                reportsComboBox2.setPromptText("Month");
            }


            // If report is for Contact Appointments
            if (reportsComboBox1.getPromptText().equalsIgnoreCase("Contact")) {

                // Filter appointments in TableView by contact chosen in ComboBox1
                for (Contact contact : reportsContactList) {
                    if (contact.getContactName().equalsIgnoreCase(reportsComboBox1.getSelectionModel().getSelectedItem())) {
                        reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                                appointment.getCustomerID() == contact.getContactID()));
                    }
                }

                // Enable Necessary Table Columns
                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                // Name Table Columns
                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                // Assign members to TableColumns
                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));
                // Convert AppointmentID to String
                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                // Get appointment start time in system TimeZone
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                // Get appointment End Time in system TimeZone
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                // Convert customerID to String
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                // Create String for Label
                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                // Enable Label and assign String to Label
                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }


            // If report is for User appointments
            if (reportsComboBox1.getPromptText().equalsIgnoreCase("User ID")) {

                // Filter TableView Appointments by User chosen in ComboBox1
                for (User user : reportsUserList) {
                    if (String.valueOf(user.getUserID()).equalsIgnoreCase(reportsComboBox1.getSelectionModel().getSelectedItem())) {
                        reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                                appointment.getUserID() == user.getUserID()));
                    }
                }

                // Enable Necessary Table Columns
                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                // Name Table Columns
                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                // Assign members to Table Columns
                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));
                // Convert AppointmentID to String
                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                // Get appointment start time in system Time Zone
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                // Get appointment end time in system Time Zone
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                // Convert customerID to String
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                // Create String for label
                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                // Enable Label and assign String
                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }
        }
    }

    /**
     * Generates a report based on selection in ComboBox2 depending on report chosen.
     * @param event Selection made in ComboBox2
     */
    @FXML void onActionReportsComboBox2(ActionEvent event) {

        // If selection was made in ComboBox2; not null
        if (reportsComboBox2.getSelectionModel().getSelectedItem() != null) {


            // If report is for Appointments by Month
            if (reportsComboBox2.getPromptText().equalsIgnoreCase("Month")) {

                // filter appointments in tableView based on Year and Month chosen in both ComboBoxes
                reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                        appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getYear()
                                == Integer.parseInt(reportsComboBox1.getSelectionModel().getSelectedItem())
                        && String.valueOf(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getMonth())
                                .equalsIgnoreCase(reportsComboBox2.getSelectionModel().getSelectedItem())));

                // Enable necessary Table Columns
                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                // Name Table Columns
                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                // Assign members to Table Columns
                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));
                // Convert AppointmentID to String
                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                // Get Appointment StartTime in system Time Zone
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                // Get Appointment end Time in system Time Zone
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                // Convert customerID to String
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                // Create string for label
                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                // Enable label and assign String
                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }
        }
    }

    /**
     * Logs user out of application and sends user to UserLoginForm Page
     * @param event Logout Button pressed
     * @throws IOException
     */
    @FXML void onActionReportsLogout(ActionEvent event) throws IOException {

        // Ask user to Confirm
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");

        // Wait for user to respond
        Optional<ButtonType> result = alert.showAndWait();

        // If user selected ok, send user to UserLoginForm page
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("UserLoginForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            // Disables Confirmation dialog when X button pressed after logout.
            stage.setOnCloseRequest(event1 -> {
                JDBC.closeConnection();
                System.exit(0);
            });
        }
    }

    /**
     * Initialize the AppointmentCustomerPage / Main Form.
     * Sets up Appointment Tab in monthly TableView for appointments that occur during the current month.
     * Sets up Customer Tab with all customers.
     * Sets up Reports Tab with options in Accordions.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            /* ------------------- */
            /* --Appointment Tab-- */
            /* ------------------- */

            // Set date picker to today's date.
            datePicker.getEditor().setText(LocalDate.now(ZoneId.systemDefault()).format(datePickerFormat));

            // Clear appointments list then retrieve appointments
            appointmentList.clear();
            appointmentList.setAll(AppointmentDBImpl.getAllAppointments());

            // Clear contacts list then retrieve all contacts
            contactList.clear();
            contactList.setAll(ContactDBImpl.getContacts());

            // Filter appointments by selected month for monthly table
            monthlyTableAppointments.setAll(appointmentList.filtered(appointment -> {

                // Get Months to Compare
                Month appointmentMonth = appointment.getDateTimeStart().getMonth();
                Month datePickerMonth  = LocalDate.parse(datePicker.getEditor().getText(), datePickerFormat).getMonth();

                // If Months are same, add to filter
                return appointmentMonth == datePickerMonth;
            }));

            // Filter appointment by selected week for weekly table
            weeklyTableAppointments.setAll(appointmentList.filtered(appointment -> {

                // Get Weeks to compare
                LocalDate datePickerLocalDate = LocalDate.parse(datePicker.getEditor().getText(), datePickerFormat);
                long appointmentWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(appointment.getDateTimeStart());
                long datePickerWeek  = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(datePickerLocalDate);

                // If weeks are same, add to filter
                return appointmentWeek == datePickerWeek;
            }));

            // Default to monthlyTableAppointments
            appointmentTableView.setItems(monthlyTableAppointments);

            // Fill appointmentTableView Columns
            appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol        .setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol  .setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol     .setCellValueFactory(new PropertyValueFactory<>("location"));
            typeCol         .setCellValueFactory(new PropertyValueFactory<>("type"));
            customerIDCol   .setCellValueFactory(new PropertyValueFactory<>("customerID"));
            UserIDCol       .setCellValueFactory(new PropertyValueFactory<>("userID"));
            // Display Contact Name instead of Contact_ID
            contactCol.setCellValueFactory(cellData -> {
                for (Contact contact : contactList) {
                    if (cellData.getValue().getContactID() == contact.getContactID()) {
                        return new SimpleStringProperty(contact.getContactName());
                    }
                }
                return null;
            });
            // Customize format and convert timezone of start time
            startDateTimeCol.setCellValueFactory(cellData -> {
                String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                return new SimpleStringProperty(formattedStartTime);
            });
            // Customize format and convert timezone of end time.
            endDateTimeCol.setCellValueFactory(cellData -> {
                String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                return new SimpleStringProperty(formattedEndTime);
            });

            /* ---------------- */
            /* --Customer Tab-- */
            /* ---------------- */

            // clear customerList and divisionList then retrieve them upon initialize and assign customer to tableview
            customerList.clear();
            divisionList.clear();
            customerList.setAll(CustomerDBImpl.getAllCustomers());
            divisionList.setAll(FirstLevelDivisionDBImpl.getFirstLevelDivisions());
            customerTableView.setItems(customerList);

            // Fill table columns
            customerCustomerIDCol     .setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerCustomerNameCol   .setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerCustomerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerPostalCodeCol     .setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerPhoneNumberCol    .setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            // Display FirstLevelDivision divisionName instead of divisionID
            customerDivisionCol.setCellValueFactory(cellData -> {
                for (FirstLevelDivision division : divisionList) {
                    if (division.getDivisionID() == cellData.getValue().getDivisionID()) {
                        return new SimpleStringProperty(division.getDivisionName());
                    }
                }
                return null;
            });

            /* --------------- */
            /* --Reports Tab-- */
            /* --------------- */

            // Clear Accordion Lists
            appointmentAccordionList.clear();
            contactsAccordionList   .clear();
            usersAccordionList      .clear();

            // Add options to accordion lists
            appointmentAccordionList.add("By Type");
            appointmentAccordionList.add("By Month");
            contactsAccordionList   .add("Schedule");
            usersAccordionList      .add("Appointments");

            // assign options to accordion lists
            appointmentsAccordianListView.setItems(appointmentAccordionList);
            contactsAccordianListView    .setItems(contactsAccordionList);
            usersAccordianListView       .setItems(usersAccordionList);

        // if any queries fail.
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
