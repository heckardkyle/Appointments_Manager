package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.CountryDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.FirstLevelDivisionDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Exceptions.EmptyFieldsException;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Country;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import com.c195_software_ii__advanced_java_concepts_pa.Models.FirstLevelDivision;
import com.c195_software_ii__advanced_java_concepts_pa.Utilities.AlertInterface;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Customer page for updating and creating new appointments.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class CustomerController implements Initializable {

    /* --Scene Declarations-- */
    Stage  stage;
    Parent scene;

    /* --ObservableLists-- */
    ObservableList<Country>            countryList  = FXCollections.observableArrayList();
    ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

    /* --Booleans-- */
    Boolean updatingCustomer = false;

    /* --FXML IDs-- */
    @FXML private Label                        customerPageLabel;
    @FXML private TextField                    customerIDTextField;
    @FXML private TextField                    customerNameTextField;
    @FXML private TextField                    phoneNumberTextField;
    @FXML private TextField                    addressTextField;
    @FXML private TextField                    postalCodeTextField;
    @FXML private ComboBox<Country>            countryComboBox;
    @FXML private ComboBox<FirstLevelDivision> divisionComboBox;
    @FXML private Button                       cancelButton;
    @FXML private Button                       saveCustomerButton;

    /* --Lambdas-- */
    AlertInterface warningAlert = (title, message) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    };

    /**
     * Finds the next available customerID.
     * @return <code>newCustomerID</code>
     * @throws SQLException
     */
    public int getAvailableCustomerID() throws SQLException {
        // Declare variable to store largest customerID used
        int newCustomerID = 0;

        // Look for largest CustomerID in database
        for (Customer customer : CustomerDBImpl.getAllCustomers()) {
            if (customer.getCustomerID() > newCustomerID)
                newCustomerID = customer.getCustomerID();
        }
        // Return next available ID
        return newCustomerID + 1;
    }

    /**
     * Fetches FirstLevelDivision Object that matches the customer's divisionID
     * @param customer the customer to find FirstLevelDivision for
     * @return <code>FirstLevelDivision</code>
     */
    public FirstLevelDivision selectFirstLevelDivision(Customer customer) {
        // For each division in divisionList
        for (FirstLevelDivision firstLevelDivision : divisionList) {
            // If division match customer's divisionID
            if (firstLevelDivision.getDivisionID() == customer.getDivisionID()) {
                return firstLevelDivision;
            }
        }
        // If no match
        return null;
    }

    /**
     * Fetches the Country Object that match the division's countryID.
     * @param division the FirstLevelDivision to find Country for
     * @return <code>Country</code>
     */
    public Country selectCountry(FirstLevelDivision division) {
        // for each country in countryList
        for (Country country : countryList) {
            // if country matches division's countryID
            if (country.getCountryID() == division.getCountryID()) {
                return country;
            }
        }
        // If no match
        return null;
    }

    /**
     * Sends customer to Customer page.
     * When update button is pressed in AppointmentCustomerPage, the selected customer's information is sent to the
     * customer page to fill each of the fields, and the page is set to "update mode".
     * @param customer the customer to update
     */
    public void sendCustomer (Customer customer) {
        // Fill fields with sent customer's info.
        customerIDTextField  .setText(String.valueOf(customer.getCustomerID()));
        customerNameTextField.setText(customer.getCustomerName());
        phoneNumberTextField .setText(customer.getPhoneNumber());
        addressTextField     .setText(customer.getAddress());
        divisionComboBox     .getSelectionModel().select(selectFirstLevelDivision(customer));
        countryComboBox      .getSelectionModel().select(selectCountry(divisionComboBox.getSelectionModel().getSelectedItem()));
        postalCodeTextField  .setText(customer.getPostalCode());

        // Set up ComboBox
        divisionComboBox.setItems(divisionList.filtered(firstLevelDivision ->
                firstLevelDivision.getCountryID() == countryComboBox.getSelectionModel().getSelectedItem().getCountryID()));

        // Change page to update customer mode
        updatingCustomer = true;
        divisionComboBox  .setDisable(false);
        customerPageLabel .setText("Update Customer");
        saveCustomerButton.setText("Update Customer");
    }

    /**
     * After selecting a country, enables and filters divisionComboBox.
     * @param event when user interacts with countryComboBox
     */
    @FXML
    void onActionCountryComboBox(ActionEvent event) {
        // Once Country is selected, enable division box
        divisionComboBox.setDisable(false);

        // Filter Divisions to those associated with selected country
        divisionComboBox.setItems(divisionList.filtered(firstLevelDivision ->
                firstLevelDivision.getCountryID() == countryComboBox.getSelectionModel().getSelectedItem().getCountryID()));
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
        if (customerNameTextField.getText().isBlank()
                && phoneNumberTextField.getText().isBlank()
                && addressTextField.getText().isBlank()
                && divisionComboBox.getSelectionModel().isEmpty()
                && postalCodeTextField.getText().isBlank()) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // Else, ask if user would like to continue.
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
     * Create/Update Customer after button is pressed.
     * Afterwords, user is sent to AppointmentCustomerPage. If any values are empty, user is alerted.
     * @param event Create/Update Customer Button is pressed
     * @throws IOException
     */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws IOException {
        try {
            // If any fields empty, alert user
            if (customerNameTextField.getText().isBlank()
                    || phoneNumberTextField.getText().isBlank()
                    || addressTextField.getText().isBlank()
                    || divisionComboBox.getSelectionModel().isEmpty()
                    || postalCodeTextField.getText().isEmpty()) {
                throw new EmptyFieldsException(); }

            // Fill variables with values from fields
            int    newCustomerID         = Integer.parseInt(customerIDTextField.getText());
            String newCustomerName       = customerNameTextField.getText();
            String newCustomerPhone      = phoneNumberTextField.getText();
            String newCustomerAddress    = addressTextField.getText();
            int    newCustomerFLDivision = divisionComboBox.getSelectionModel().getSelectedItem().getDivisionID();
            String newCustomerPostalCode = postalCodeTextField.getText();

            // Create Customer or Update Customer if in page is in update mode.
            if (updatingCustomer) {
                CustomerDBImpl.updateCustomer(newCustomerID, newCustomerName, newCustomerAddress,
                        newCustomerPostalCode, newCustomerPhone, newCustomerFLDivision);
            }
            else { // Create new customer
                CustomerDBImpl.createCustomer(newCustomerID, newCustomerName, newCustomerAddress,
                        newCustomerPostalCode, newCustomerPhone, newCustomerFLDivision);
            }

            // Send user to AppointmentCustomerPage
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // Catch Exceptions
        catch (Exception e) {
            if (e instanceof EmptyFieldsException) {
                warningAlert.showAlert("Warning Dialog", "All fields must have a value before continuing."); }
        }
    }

    /**
     * Initializes page.
     * Sets the customerIDTextField and prepares comboBoxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Assign ID to customer ID text field
        try { customerIDTextField.setText(String.valueOf(getAvailableCustomerID())); } catch (SQLException e) { e.printStackTrace(); }

        // Clear lists to prevent potential duplication
        countryList.clear();
        divisionList.clear();

        // Fill lists
        try {
            countryList.setAll(CountryDBImpl.getCountries());
            divisionList.setAll(FirstLevelDivisionDBImpl.getFirstLevelDivisions());
        } catch (SQLException e) { e.printStackTrace(); }

        // Set lists in combo boxes
        countryComboBox.setItems(countryList);
        divisionComboBox.setItems(divisionList);
    }
}

