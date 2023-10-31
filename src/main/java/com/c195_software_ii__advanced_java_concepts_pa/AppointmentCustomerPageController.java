package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentCustomerPageController implements Initializable {

    Stage stage;
    Parent scene;

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

    @FXML private Tab customersTab;
    @FXML private ListView<String> customerListView;
    @FXML private Button newCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;

    public void onMouseTableClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (!(appointmentTableView.getSelectionModel().getSelectedCells().isEmpty())) {
            updateAppointmentButton.setDisable(false);
            deleteAppointmentButton.setDisable(false);
        }
        else {
            updateAppointmentButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            appointmentTableView.setItems(AppointmentDBImpl.getAllAppointments());

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

            ObservableList<Customer> allCustomers = CustomerDBImpl.getAllCustomers();
            ObservableList<String> customerNames = FXCollections.observableArrayList();
            for (Customer customer : allCustomers) {
                customerNames.add(customer.getCustomerName());
            }

            customerListView.setItems(customerNames);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
