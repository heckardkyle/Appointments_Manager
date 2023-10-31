package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ResourceBundle;

public class AppointmentCustomerPageController implements Initializable {

    /* --Stage Declarations-- */
    Stage stage;
    Parent scene;

    /* --Appointments List Declarations-- */
    ObservableList<Appointment> tableAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> monthlyTableAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> weeklyTableAppointments = FXCollections.observableArrayList();

    /* --Customer List Declarations-- */
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    ObservableList<String> customerNames = FXCollections.observableArrayList();

    /* --Appointment FXML Declarations-- */
    @FXML private Tab appointmentsTab;
    @FXML private ToggleGroup MonthlyWeeklyTG;
    @FXML private ToggleButton monthlyViewButton;
    @FXML private ToggleButton weeklyViewButton;
    @FXML private Label weekMonthLabel;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> appointmentIDCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, Integer> contactCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, Timestamp> startDateTimeCol;
    @FXML private TableColumn<Appointment, Timestamp> endDateTimeCol;
    @FXML private TableColumn<Appointment, Integer> customerIDCol;
    @FXML private TableColumn<Appointment, Integer> UserIDCol;
    @FXML private Button newAppointmentButton;
    @FXML private Button updateAppointmentButton;
    @FXML private Button deleteAppointmentButton;

    /* --Customer FXML Declarations-- */
    @FXML private Tab customersTab;
    @FXML private ListView<String> customerListView;
    @FXML private Button newCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;

    /**
     * Enables and disables appointment update and delete buttons if there's an active selection or not.
     * @param event Mouse click event
     */
    @FXML
    public void onMouseTableClick(MouseEvent event) {
        if (!(appointmentTableView.getSelectionModel().getSelectedCells().isEmpty())) {
            updateAppointmentButton.setDisable(false);
            deleteAppointmentButton.setDisable(false);
        }
        else {
            updateAppointmentButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
        }
    }

    /**
     * Enables and disables customer update and delete buttons if there's an active selection or not.
     * @param event Mouse click event
     */
    @FXML
    public void onMouseListClick(MouseEvent event) {
        if (!(customerListView.getSelectionModel().getSelectedItems().isEmpty())) {
            updateCustomerButton.setDisable(false);
            deleteCustomerButton.setDisable(false);
        }
        else {
            updateCustomerButton.setDisable(true);
            deleteCustomerButton.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            // clear tableAppointments list then retrieve upon initializing controller
            tableAppointments.clear();
            tableAppointments.setAll(AppointmentDBImpl.getAllAppointments());
            appointmentTableView.setItems(tableAppointments);

            // Fill appointmentTableView Columns
            appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            UserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

            // clear customerList and customerName list then retrieve customers to customerList
            customerList.clear();
            customerList.setAll(CustomerDBImpl.getAllCustomers());
            customerNames.clear();

            // insert customer names into customerName list from customerList
            for (Customer customer : customerList) {
                customerNames.add(customer.getCustomerName());
            }

            // fill customerListView with customerNames
            customerListView.setItems(customerNames);

        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
