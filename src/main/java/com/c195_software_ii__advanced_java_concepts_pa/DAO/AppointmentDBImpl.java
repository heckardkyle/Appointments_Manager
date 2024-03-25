package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Database Access Object for Appointment Queries.
 * Appointments can be inserted into database, deleted from database, and updated in the database.
 * Appointments can also be fetched from database and stored as Appointment Objects and returned as
 * an ObservableList.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Appointment
 */
public class AppointmentDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet         result;
    static Appointment       appointment;

    /**
     * Fetches all appointments from the Database.
     * An Appointment object is created from each result in query. Each Appointment Object is added to
     * ObservableList allAppointments. The ObservableList is returned.
     *
     * @return <code>allAppointments</code>
     * @throws SQLException if Query fails
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        try {
            String sqlSelect = "SELECT * FROM appointments"; // Query
            ObservableList<Appointment> allAppointments = FXCollections.observableArrayList(); // Table for storing Appointment objects.
            allAppointments.clear(); // clear table, prevents potential duplication
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            while (result.next()) { // For each result in query
                // Store each value from result
                int       appointmentID = result.getInt      ("Appointment_ID");
                String    title         = result.getString   ("Title");
                String    description   = result.getString   ("Description");
                String    location      = result.getString   ("Location");
                String    type          = result.getString   ("Type");
                Timestamp dateTimeStart     = result.getTimestamp("Start");
                Timestamp dateTimeEnd       = result.getTimestamp("End");
                int       customerID    = result.getInt      ("Customer_ID");
                int       userID        = result.getInt      ("User_ID");
                int       contactID     = result.getInt      ("Contact_ID");

                // Create appointment object using stored values and add to ObservableList
                appointment = new Appointment(appointmentID, title, description, location, type, dateTimeStart, dateTimeEnd, customerID, userID, contactID);
                allAppointments.add(appointment);
            }
            return allAppointments;
        }
        // return null upon exception
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Creates an appointment and inserts into database.
     *
     * @param appointmentID The appointmentID to set
     * @param title         The appointment title to set
     * @param description   The appointment description to set
     * @param location      The appointment location to set
     * @param type          The appointment type to set
     * @param start         The appointment start DateTime to set
     * @param end           The appointment end DateTime to set
     * @param customerID    The associated customerID to set
     * @param userID        The associated userID to set
     * @param contactID     The associated contactID to set
     */
    public static void createAppointment(int appointmentID, String title, String description, String location, String type,
                                            Timestamp start, Timestamp end, int customerID, int userID, int contactID) {
        try {
            // Prepare Insert Query
            String sqlCreate = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Fill values in query using arguments
            preparedStatement = JDBC.connection.prepareStatement(sqlCreate);
            preparedStatement.setInt      (1, appointmentID);
            preparedStatement.setString   (2, title);
            preparedStatement.setString   (3, description);
            preparedStatement.setString   (4, location);
            preparedStatement.setString   (5, type);
            preparedStatement.setTimestamp(6, start);
            preparedStatement.setTimestamp(7, end);
            preparedStatement.setInt      (8, customerID);
            preparedStatement.setInt      (9, userID);
            preparedStatement.setInt      (10, contactID);

            // Execute insertion
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Updates an Appointment in the database.
     * The appointment to update is located using the appointmentID.
     *
     * @param appointmentID The appointmentID to update
     * @param title         The appointment title to set
     * @param description   The appointment description to set
     * @param location      The appointment location to set
     * @param type          The appointment type to set
     * @param start         The appointment start DateTime to set
     * @param end           The appointment end DateTime to set
     * @param customerID    The associated customerID to set
     * @param userID        The associated userID to set
     * @param contactID     The associated contactID to set
     */
    public static void updateAppointment(int appointmentID, String title, String description, String location, String type,
                                         Timestamp start, Timestamp end, int customerID, int userID, int contactID) {
        try {
            // Prepare Update Query
            String sqlUpdate = "UPDATE appointments SET (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) WHERE Appointment_ID = ?";

            // Fill values in query using arguments
            preparedStatement = JDBC.connection.prepareStatement(sqlUpdate);
            preparedStatement.setString   (1, title);
            preparedStatement.setString   (2, description);
            preparedStatement.setString   (3, location);
            preparedStatement.setString   (4, type);
            preparedStatement.setTimestamp(5, start);
            preparedStatement.setTimestamp(6, end);
            preparedStatement.setInt      (7, customerID);
            preparedStatement.setInt      (8, userID);
            preparedStatement.setInt      (9, contactID);
            preparedStatement.setInt      (10, appointmentID);

            // Execute update
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Deletes an appointment from the database.
     * The appointment to delete is located using the appointmentID.
     *
     * @param appointmentID The appointment to delete.
     */
    public static void deleteAppointment(int appointmentID) {
        try {
            // Prepare Delete Query
            String sqlDelete = "DELETE FROM appointments WHERE Appointment_ID = ?";

            // Fill values in query using argument
            preparedStatement = JDBC.connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, appointmentID);

            // Execute deletion
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }
}
