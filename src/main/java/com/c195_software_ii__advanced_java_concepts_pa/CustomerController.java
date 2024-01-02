package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.CountryDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.DAO.FirstLevelDivisionDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Country;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import com.c195_software_ii__advanced_java_concepts_pa.Models.FirstLevelDivision;
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

public class CustomerController implements Initializable {

    /* --Scene Declarations-- */
    Stage  stage;
    Parent scene;

    /* --ObservableList Declarations-- */
    ObservableList<Country>            countryList  = FXCollections.observableArrayList();
    ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

    /* --FXML IDs-- */
    @FXML private TextField                    customerIDTextField;
    @FXML private TextField                    customerNameTextField;
    @FXML private TextField                    phoneNumberTextField;
    @FXML private TextField                    addressTextField;
    @FXML private ComboBox<Country>            countryComboBox;
    @FXML private ComboBox<FirstLevelDivision> divisionComboBox;
    @FXML private Button                       cancelButton;
    @FXML private Button                       SaveCustomerButton;

    public class EmptyFieldsException extends Exception {
        public EmptyFieldsException() {}
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int getAvailableCustomerID() throws SQLException {
        int newCustomerID = -1;

        // Look for largest CustomerID in database
        for (Customer customer : CustomerDBImpl.getAllCustomers()) {
            if (customer.getCustomerID() > newCustomerID)
                newCustomerID = customer.getCustomerID();
        }
        // Return smallest available ID
        return newCustomerID + 1;
    }

    @FXML
    void onActionCountryComboBox(ActionEvent event) {
        // Once Country is selected, enable division box
        divisionComboBox.setDisable(false);

        // Filter Divisions to those associated with selected country
        divisionComboBox.setItems(divisionList.filtered(firstLevelDivision ->
                firstLevelDivision.getCountryID() == countryComboBox.getSelectionModel().getSelectedItem().getCountryID()));
    }

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        // If no entries have been made, exit without prompt
        if (customerNameTextField.getText().isBlank()
                && phoneNumberTextField.getText().isBlank()
                && addressTextField.getText().isBlank()
                && countryComboBox.getSelectionModel().isEmpty()
                && divisionComboBox.getSelectionModel().isEmpty()) {
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

    @FXML
    void onActionSaveCustomer(ActionEvent event) throws IOException {
        try {
            if (customerNameTextField.getText().isBlank()
                    || phoneNumberTextField.getText().isBlank()
                    || addressTextField.getText().isBlank()
                    || countryComboBox.getSelectionModel().isEmpty()
                    || divisionComboBox.getSelectionModel().isEmpty()) {
                throw new EmptyFieldsException(); }

            int    newCustomerID;
            String newCustomerName;
            String newCustomerPhone;
            String newCustomerAddress;
            String newCustomerCountry;
            String newCustomerFLDivision;

            newCustomerID         = Integer.parseInt(customerIDTextField.getText());
            newCustomerName       = customerNameTextField.getText();
            newCustomerPhone      = phoneNumberTextField.getText();
            newCustomerAddress    = addressTextField.getText();
            newCustomerCountry    = countryComboBox.getSelectionModel().getSelectedItem().getCountryName();
            newCustomerFLDivision = divisionComboBox.getSelectionModel().getSelectedItem().getDivisionName();



            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentCustomerPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }
        catch (EmptyFieldsException e) {
            if (e instanceof EmptyFieldsException) {
                showAlert("All fields must have a value before continuing."); }
        }
    }

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

