package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.*;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Contact;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import com.c195_software_ii__advanced_java_concepts_pa.Models.FirstLevelDivision;
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
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.c195_software_ii__advanced_java_concepts_pa.Utilities.ShowAlert.showAlert;

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

    /* --Formatters-- */
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
    @FXML private TableColumn<Customer, Integer> customerDivisionIDCol;
    @FXML private Button newCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button customerLogoutButton;

    /* ------------------- */
    /* --Appointment Tab-- */
    /* ------------------- */

    @FXML void onActionMonthlyViewButton(ActionEvent event) {
        monthlyViewButton.setSelected(true); // Prevents button from being un-clicked
        appointmentTableView.setItems(monthlyTableAppointments);
        appointmentTableView.refresh();
    }

    @FXML void onActionWeeklyViewButton(ActionEvent event) {
        weeklyViewButton.setSelected(true); // Prevents button from being un-clicked
        appointmentTableView.setItems(weeklyTableAppointments);
        appointmentTableView.refresh();
    }

    @FXML void onActionDatePicker(ActionEvent event) {

        monthlyTableAppointments.clear();
        weeklyTableAppointments.clear();

        LocalDate datePickerLocalDate = LocalDate.parse(datePicker.getEditor().getText(), datePickerFormat);

        monthlyTableAppointments.setAll(appointmentList.filtered(appointment -> {
            // Get Months to Compare
            Month appointmentMonth = appointment.getDateTimeStart().getMonth();
            Month datePickerMonth  = datePickerLocalDate.getMonth();
            // If Months are same, add to filter
            if (appointmentMonth == datePickerMonth) {
                return true;
            } else return false;
        }));

        // Filter appointment by selected week for weekly table
        weeklyTableAppointments.setAll(appointmentList.filtered(appointment -> {
            // Get Weeks to compare
            long appointmentWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(appointment.getDateTimeStart());
            long datePickerWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(datePickerLocalDate);
            // If weeks are same, add to filter
            if (appointmentWeek == datePickerWeek ) {
                return true;
            } else return false;
        }));

        if (monthlyViewButton.isSelected()) {
            if (!monthlyTableAppointments.isEmpty()) {
                appointmentTableView.setItems(monthlyTableAppointments);
            } else appointmentTableView.setItems(null);
        }

        if (weeklyViewButton.isSelected() ) {
            if (!weeklyTableAppointments.isEmpty()) {
                appointmentTableView.setItems(weeklyTableAppointments);
            } else appointmentTableView.setItems(null);
        }

        appointmentTableView.refresh();
    }

    /**
     * Enables and disables appointment update and delete buttons if there's an active selection or not.
     * @param event Mouse click event
     */
    @FXML public void onMouseTableClick(MouseEvent event) {
        if (!(appointmentTableView.getSelectionModel().getSelectedCells().isEmpty())) { // If selection made, enable buttons
            updateAppointmentButton.setDisable(false);
            deleteAppointmentButton.setDisable(false);
        }
        else { // if no selection, disable buttons
            updateAppointmentButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
        }
    }

    @FXML void onActionCreateNewAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void onActionUpdateAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointment.fxml"));
        loader.load();

        AppointmentController appointmentController = loader.getController();
        appointmentController.sendAppointment(appointmentTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void onActionDeleteAppointment(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();

            AppointmentDBImpl.deleteAppointment(appointment.getAppointmentID());
            String message = "Cancelled appointment:\n"
                    + "Appointment ID : " + appointment.getAppointmentID() + "\n"
                    + "Appointment Type: " + appointment.getType();
            showAlert("Information", message);

            updateAppointmentButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
            appointmentList.clear();
            appointmentList.setAll(AppointmentDBImpl.getAllAppointments());
            appointmentTableView.refresh();

        }
    }

    @FXML void onActionAppointmentLogout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

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

    private void addDivisionName(ObservableList<Customer> customerList, ObservableList<FirstLevelDivision> divisionList) {
        for (Customer customer : customerList) {
            for (FirstLevelDivision division : divisionList) {
                if (customer.getDivisionID() == division.getDivisionID()) {
                    customer.setDivisionName(division.getDivisionName());
                }
            }
        }
    }

    /**
     * Enables and disables customer update and delete buttons if there's an active selection or not.
     * @param event Mouse click event
     */
    @FXML public void onMouseListClick(MouseEvent event) {
        if (!(customerTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            updateCustomerButton.setDisable(false);
            deleteCustomerButton.setDisable(false);
        }
        else {
            updateCustomerButton.setDisable(true);
            deleteCustomerButton.setDisable(true);
        }
    }

    @FXML void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void onActionUpdateCustomer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Customer.fxml"));
        loader.load();

        CustomerController customerController = loader.getController();
        customerController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer and all associated appointments?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Appointment appointment : appointmentList) {
                if (appointment.getCustomerID() == customerTableView.getSelectionModel().getSelectedItem().getCustomerID()) {
                    AppointmentDBImpl.deleteAppointment(appointment.getAppointmentID());
                }
            }
            CustomerDBImpl.deleteCustomer(customerTableView.getSelectionModel().getSelectedItem().getCustomerID());

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

    @FXML void onActionCustomerLogout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

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
                if (appointmentMonth == datePickerMonth) {
                    return true;
                } else return false;
            }));

            // Filter appointment by selected week for weekly table
            weeklyTableAppointments.setAll(appointmentList.filtered(appointment -> {
                // Get Weeks to compare
                LocalDate datePickerLocalDate = LocalDate.parse(datePicker.getEditor().getText(), datePickerFormat);
                long appointmentWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(appointment.getDateTimeStart());
                long datePickerWeek  = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(datePickerLocalDate);
                // If weeks are same, add to filter
                if (appointmentWeek == datePickerWeek ) {
                    return true;
                } else return false;
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

            // clear customerList then retrieve customers to customerList upon initialize
            // Name of customer's FirstLevelDivision for user-friendliness.
            customerList.clear();
            divisionList.clear();
            customerList.setAll(CustomerDBImpl.getAllCustomers());
            divisionList.setAll(FirstLevelDivisionDBImpl.getFirstLevelDivisions());
            addDivisionName(customerList, divisionList);
            customerTableView.setItems(customerList);

            // Fill table columns
            customerCustomerIDCol     .setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerCustomerNameCol   .setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerCustomerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerPostalCodeCol     .setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerPhoneNumberCol    .setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            customerDivisionIDCol     .setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        // if any queries fail.
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
