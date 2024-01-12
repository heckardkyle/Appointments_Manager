package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Access Object for Customer Queries.
 * Customers can be inserted into database, deleted from database, and updated in the database.
 * Customers can also be fetched from database and stored as Customer Objects and returned as
 * an ObservableList.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Customer
 */

public class CustomerDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet         result;
    static Customer          customer;

    /**
     * Fetches all customers from the Database.
     * A Customer object is created from each result in query. Each Customer Object is added to
     * ObservableList allCustomers. The ObservableList is returned.
     *
     * @return <code>allCustomers</code>
     * @throws SQLException if Query fails
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        try {
            String sqlSelect = "SELECT * FROM customers"; // Query
            ObservableList<Customer> allCustomers = FXCollections.observableArrayList(); // Table for storing Customer objects.
            allCustomers.clear(); // Clear table, prevents potential duplication
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            while (result.next()) { // For each result in query
                // Store each value from result
                int    customerID   = result.getInt   ("Customer_ID");
                String customerName = result.getString("Customer_Name");
                String address      = result.getString("Address");
                String postalCode   = result.getString("Postal_Code");
                String phone        = result.getString("Phone");
                int    divisionID   = result.getInt   ("Division_ID");

                // Create Customer object using stored values and add to ObservableList
                customer = new Customer(customerID, customerName, address, postalCode, phone, divisionID);
                allCustomers.add(customer);
            }
            return allCustomers;
        }
        // return null upon exception
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Creates a customer and inserts into database
     *
     * @param customerID   The customerID to set
     * @param customerName The customerName to set
     * @param address      The customer address to set
     * @param postalCode   The customer postalCode to set
     * @param phone        The customer phone to set
     * @param divisionID   The customer divisionID to set
     */
    public static void createCustomer(int customerID, String customerName, String address, String postalCode,
                                      String phone, int divisionID) {
        try {
            // Prepare Insert Query
            String sqlCreate = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            // Fill values in query using arguments
            preparedStatement = JDBC.connection.prepareStatement(sqlCreate);
            preparedStatement.setInt   (1, customerID);
            preparedStatement.setString(2, customerName);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, postalCode);
            preparedStatement.setString(5, phone);
            preparedStatement.setInt   (6, divisionID);

            //Execute insertion
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Updates a Customer in the database.
     * The Customer to update is located using the customerID.
     *
     * @param customerID The customerID to update
     * @param customerName The customerName to set
     * @param address The customer address to set
     * @param postalCode The customer postalCode to set
     * @param phone The customer phone to set
     * @param divisionID The customer divisionID to set
     */
    public static void updateCustomer(int customerID, String customerName, String address, String postalCode,
                                      String phone, int divisionID) {
        try {
            // Prepare Update Query
            String sqlUpdate = "UPDATE customers " +
                    "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?";

            // Fill values in query using arguments
            preparedStatement = JDBC.connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt   (5, divisionID);
            preparedStatement.setInt   (6, customerID);

            // Execute update
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Deletes a Customer from the database.
     * The Customer to delete is located using the customerID.
     *
     * @param customerID The Customer to delete.
     */
    public static void deleteCustomer(int customerID) {
        try {
            // Prepare Delete Query
            String sqlDelete = "DELETE FROM customers WHERE Customer_ID = ?";

            // Fill values in query using argument
            preparedStatement = JDBC.connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, customerID);

            // Execute deletion
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }
}
