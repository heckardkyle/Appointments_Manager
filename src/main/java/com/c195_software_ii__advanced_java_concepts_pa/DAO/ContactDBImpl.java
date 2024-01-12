package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database Access Object for Contact Queries.
 * Contacts are fetched from database and turned into Contact Objects for use in Combo Boxes when adding or updating
 * an appointment.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Contact
 */
public class ContactDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet         result;
    static Contact           contact;

    /**
     * Fetches all Contacts from the Database.
     * A Contact Object is created from each result in the query. Each Contact is added to ObservableList contacts.
     * The ObservableList is returned.
     *
     * @return <code>contacts</code>
     * @throws SQLException if Query fails
     */
    public static ObservableList<Contact> getContacts() throws SQLException {
        try {
            String sqlSelect = "SELECT * FROM contacts"; // Query
            ObservableList<Contact> contacts = FXCollections.observableArrayList(); // Table for storing Contact Objects
            contacts.clear(); // Clear table, prevents potential duplicates
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            while (result.next()) { // For each result in query
                // Store each value from result
                int    contactID    = result.getInt   ("Contact_ID");
                String contactName  = result.getString("Contact_Name");
                String contactEmail = result.getString("Email");

                // Create Contact Object using stored values and add to ObservableList
                contact = new Contact(contactID, contactName, contactEmail);
                contacts.add(contact);
            }
            return contacts;
        }
        // return null upon exception
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

}