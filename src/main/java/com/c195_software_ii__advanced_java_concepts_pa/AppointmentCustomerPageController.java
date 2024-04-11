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

    /* --Reports Accordion Lists-- */
    ObservableList<String> appointmentAccordionList = FXCollections.observableArrayList();
    ObservableList<String> contactsAccordionList = FXCollections.observableArrayList();
    ObservableList<String> usersAccordionList = FXCollections.observableArrayList();
    ObservableList<Appointment> reportsAppointmentList = FXCollections.observableArrayList();
    ObservableList<Contact> reportsContactList = FXCollections.observableArrayList();
    ObservableList<User> reportsUserList = FXCollections.observableArrayList();
    ObservableList<String> comboBoxList1 = FXCollections.observableArrayList();
    ObservableList<String> comboBoxList2 = FXCollections.observableArrayList();

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

    /* --Reports FXMLs-- */
    @FXML private ListView<String> appointmentsAccordianListView;
    @FXML private ListView<String> contactsAccordianListView;
    @FXML private ListView<String> usersAccordianListView;
    @FXML private ComboBox<String> reportsComboBox1;
    @FXML private ComboBox<String> reportsComboBox2;
    @FXML private Label reportsTotalLabel;
    @FXML private TableView<Appointment> reportsTableView;
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
            return appointmentMonth == datePickerMonth;
        }));

        // Filter appointment by selected week for weekly table
        weeklyTableAppointments.setAll(appointmentList.filtered(appointment -> {
            // Get Weeks to compare
            long appointmentWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(appointment.getDateTimeStart());
            long datePickerWeek = WeekFields.SUNDAY_START.weekOfWeekBasedYear().getFrom(datePickerLocalDate);
            // If weeks are same, add to filter
            return appointmentWeek == datePickerWeek;
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

    /* --------------- */
    /* --Reports Tab-- */
    /* --------------- */

    public void setDisableVisibleReportsTab() {
        reportsComboBox1.setDisable(true);
        reportsComboBox2.setDisable(true);
        reportsComboBox1.setVisible(false);
        reportsComboBox2.setVisible(false);
        reportsTotalLabel.setVisible(false);

        reportsTableViewC1.setVisible(false);
        reportsTableViewC2.setVisible(false);
        reportsTableViewC3.setVisible(false);
        reportsTableViewC4.setVisible(false);
        reportsTableViewC5.setVisible(false);
        reportsTableViewC6.setVisible(false);
        reportsTableViewC7.setVisible(false);

        comboBoxList1.clear();
        comboBoxList2.clear();
        reportsAppointmentList.clear();
        reportsContactList.clear();
        reportsUserList.clear();
    }

    @FXML void onMouseClickedAppointmentListView(MouseEvent event) throws IOException, SQLException {
        if (appointmentsAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("By Type")) {

            setDisableVisibleReportsTab();
            contactsAccordianListView.getSelectionModel().clearSelection();
            usersAccordianListView.getSelectionModel().clearSelection();

            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());
            for (Appointment appointment : reportsAppointmentList) {
                if (!comboBoxList1.contains(appointment.getType())) {
                    comboBoxList1.add(appointment.getType());
                }
            }
            reportsComboBox1.setItems(comboBoxList1);

            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("Type");
        }

        if (appointmentsAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("By Month")) {
            setDisableVisibleReportsTab();
            contactsAccordianListView.getSelectionModel().clearSelection();
            usersAccordianListView.getSelectionModel().clearSelection();

            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());
            for (Appointment appointment : reportsAppointmentList) {
                String appointmentYear = String.valueOf(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getYear());
                if (!comboBoxList1.contains(appointmentYear)) {
                    comboBoxList1.add(appointmentYear);
                }
            }
            reportsComboBox1.setItems(comboBoxList1);

            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("Year");
        }
    }

    @FXML void onMouseClickedContactsListView(MouseEvent event) throws IOException, SQLException {
        if (contactsAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("Schedule")) {

            setDisableVisibleReportsTab();
            appointmentsAccordianListView.getSelectionModel().clearSelection();
            usersAccordianListView.getSelectionModel().clearSelection();

            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());

            reportsContactList.addAll(ContactDBImpl.getContacts());
            for (Contact contact : reportsContactList) {
                comboBoxList1.add(contact.getContactName());
            }
            reportsComboBox1.setItems(comboBoxList1);

            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("Contact");
        }
    }

    @FXML void onMouseClickedUsersListView(MouseEvent event) throws IOException, SQLException {
        if (usersAccordianListView.getSelectionModel().getSelectedItem().equalsIgnoreCase("Appointments")) {

            setDisableVisibleReportsTab();
            appointmentsAccordianListView.getSelectionModel().clearSelection();
            contactsAccordianListView.getSelectionModel().clearSelection();

            reportsAppointmentList.addAll(AppointmentDBImpl.getAllAppointments());

            reportsUserList.addAll(UserDBImpl.getAllUsers());
            for (User user : reportsUserList) {
                comboBoxList1.add(String.valueOf(user.getUserID()));
            }
            reportsComboBox1.setItems(comboBoxList1);

            reportsComboBox1.setDisable(false);
            reportsComboBox1.setVisible(true);
            reportsComboBox1.setPromptText("User ID");
        }
    }

    @FXML void onActionReportsComboBox1(ActionEvent event) throws IOException {
        if (reportsComboBox1.getSelectionModel().getSelectedItem() != null) {

            if (reportsComboBox1.getPromptText().equalsIgnoreCase("Type")) {

                reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                        appointment.getType().equalsIgnoreCase(reportsComboBox1.getSelectionModel().getSelectedItem())));

                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));

                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }

            if (reportsComboBox1.getPromptText().equalsIgnoreCase("Year")) {

                for (Appointment appointment : reportsAppointmentList.filtered(appointment ->
                        appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getYear()
                        == Integer.parseInt(reportsComboBox1.getSelectionModel().getSelectedItem()))) {
                    String appointmentMonth = String.valueOf(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getMonth());
                    if (!comboBoxList2.contains(appointmentMonth)) {
                        comboBoxList2.add(appointmentMonth);
                    }
                }
                reportsComboBox2.setItems(comboBoxList2);

                reportsComboBox2.setDisable(false);
                reportsComboBox2.setVisible(true);
                reportsComboBox2.setPromptText("Month");
            }

            if (reportsComboBox1.getPromptText().equalsIgnoreCase("Contact")) {

                for (Contact contact : reportsContactList) {
                    if (contact.getContactName().equalsIgnoreCase(reportsComboBox1.getSelectionModel().getSelectedItem())) {
                        reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                                appointment.getCustomerID() == contact.getContactID()));
                    }
                }

                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));

                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }

            if (reportsComboBox1.getPromptText().equalsIgnoreCase("User ID")) {

                for (User user : reportsUserList) {
                    if (String.valueOf(user.getUserID()).equalsIgnoreCase(reportsComboBox1.getSelectionModel().getSelectedItem())) {
                        reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                                appointment.getUserID() == user.getUserID()));
                    }
                }

                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));

                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }

        }
    }

    @FXML void onActionReportsComboBox2(ActionEvent event) throws IOException {
        if (reportsComboBox2.getSelectionModel().getSelectedItem() != null) {

            if (reportsComboBox2.getPromptText().equalsIgnoreCase("Month")) {

                reportsTableView.setItems(reportsAppointmentList.filtered(appointment ->
                        appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getYear()
                                == Integer.parseInt(reportsComboBox1.getSelectionModel().getSelectedItem())
                        && String.valueOf(appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).getMonth())
                                .equalsIgnoreCase(reportsComboBox2.getSelectionModel().getSelectedItem())));

                reportsTableViewC1.setVisible(true);
                reportsTableViewC2.setVisible(true);
                reportsTableViewC3.setVisible(true);
                reportsTableViewC4.setVisible(true);
                reportsTableViewC5.setVisible(true);
                reportsTableViewC6.setVisible(true);
                reportsTableViewC7.setVisible(true);

                reportsTableViewC1.setText("Appointment ID");
                reportsTableViewC2.setText("Title");
                reportsTableViewC3.setText("Type");
                reportsTableViewC4.setText("Description");
                reportsTableViewC5.setText("Start Time");
                reportsTableViewC6.setText("End Time");
                reportsTableViewC7.setText("Customer ID");

                reportsTableViewC2.setCellValueFactory(new PropertyValueFactory<>("title"));
                reportsTableViewC3.setCellValueFactory(new PropertyValueFactory<>("type"));
                reportsTableViewC4.setCellValueFactory(new PropertyValueFactory<>("description"));

                reportsTableViewC1.setCellValueFactory(cellData -> {
                    String appointmentIDString = String.valueOf(cellData.getValue().getAppointmentID());
                    return new SimpleStringProperty(appointmentIDString);
                });
                reportsTableViewC5.setCellValueFactory(cellData -> {
                    String formattedStartTime = cellData.getValue().getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedStartTime);
                });
                reportsTableViewC6.setCellValueFactory(cellData -> {
                    String formattedEndTime = cellData.getValue().getDateTimeEnd().withZoneSameInstant(ZoneId.systemDefault()).format(tableFormat);
                    return new SimpleStringProperty(formattedEndTime);
                });
                reportsTableViewC7.setCellValueFactory(cellData -> {
                    String customerIDString = String.valueOf(cellData.getValue().getCustomerID());
                    return new SimpleStringProperty(customerIDString);
                });

                String reportsTotalLabelString = reportsTableView.getItems().size() + " Appointments";

                reportsTotalLabel.setVisible(true);
                reportsTotalLabel.setText(reportsTotalLabelString);
            }
        }
    }

    @FXML void onActionReportsLogout(ActionEvent event) throws IOException {
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

            /* --------------- */
            /* --Reports Tab-- */
            /* --------------- */

            appointmentAccordionList.clear();
            contactsAccordionList.clear();
            usersAccordionList.clear();

            appointmentAccordionList.add("By Type");
            appointmentAccordionList.add("By Month");
            contactsAccordionList.add("Schedule");
            usersAccordionList.add("Appointments");

            appointmentsAccordianListView.setItems(appointmentAccordionList);
            contactsAccordianListView.setItems(contactsAccordionList);
            usersAccordianListView.setItems(usersAccordionList);

        // if any queries fail.
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
