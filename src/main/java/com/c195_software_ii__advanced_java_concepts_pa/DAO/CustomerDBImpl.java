package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet result;
    static Customer customer;


    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        try {
            String sqlSelect = "SELECT * FROM customers";
            ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
            allCustomers.clear();
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                int customerID = result.getInt("Customer_ID");
                String customerName = result.getString("Customer_Name");
                String address = result.getString("Address");
                String postalCode = result.getString("Postal_Code");
                String phone = result.getString("Phone");
                int divisionID = result.getInt("Division_ID");

                customer = new Customer(customerID, customerName, address, postalCode, phone, divisionID);
                allCustomers.add(customer);
            }
            return allCustomers;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static void createCustomer(int customerID, String customerName, String address, String postalCode,
                                      String phone, int divisionID) {
        try {
            String sqlCreate = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            preparedStatement = JDBC.connection.prepareStatement(sqlCreate);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setString(2, customerName);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, postalCode);
            preparedStatement.setString(5, phone);
            preparedStatement.setInt(6, divisionID);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    public static void updateCustomer(int customerID, String customerName, String address, String postalCode,
                                      String phone, int divisionID) {
        try {
            String sqlUpdate = "UPDATE customers SET (Customer_Name, Address, Postal_Code, Phone, Division_ID) "
                    + "VALUES (?, ?, ?, ?, ?) WHERE Customer_ID = ?";

            preparedStatement = JDBC.connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, divisionID);
            preparedStatement.setInt(6, customerID);

        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteCustomer(int customerID) {
        try {
            String sqlDelete = "DELETE FROM customers WHERE Customer_ID = ?";

            preparedStatement = JDBC.connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, customerID);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }
}
