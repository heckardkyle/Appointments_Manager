package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDBImpl {
    static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    static PreparedStatement preparedStatement;
    static ResultSet result;
    static Customer customer;
    static String sqlSelect = "SELECT * FROM customers";

    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
        result = preparedStatement.executeQuery();

        while (result.next()) {
            int       customerID   = result.getInt("Customer_ID");
            String    customerName = result.getString("Customer_Name");
            String    address      = result.getString("Address");
            String    postalCode   = result.getString("Postal_Code");
            String    phone        = result.getString("Phone");
            int       divisionID   = result.getInt("Division_ID");

            customer = new Customer(customerID,customerName,address,postalCode,phone,divisionID);
            allCustomers.add(customer);
        }
        return allCustomers;
    }
}
