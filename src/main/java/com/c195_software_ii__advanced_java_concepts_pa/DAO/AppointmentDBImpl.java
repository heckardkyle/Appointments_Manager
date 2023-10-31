package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentDBImpl {

    static PreparedStatement preparedStatement;
    static ResultSet result;
    static Appointment appointment;


    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        try {
            String sqlSelect = "SELECT * FROM appointments";
            ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
            allAppointments.clear();
            preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                int appointmentID = result.getInt("Appointment_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                String type = result.getString("Type");
                Timestamp startTime = result.getTimestamp("Start");
                Timestamp endTime = result.getTimestamp("End");
                int customerID = result.getInt("Customer_ID");
                int userID = result.getInt("User_ID");
                int contactID = result.getInt("Contact_ID");

                appointment = new Appointment(appointmentID, title, description, location, type, startTime, endTime, customerID, userID, contactID);
                allAppointments.add(appointment);
            }
            return allAppointments;
        }
        catch (SQLException e ) { e.printStackTrace(); }
        return null;
    }

    public static void createAppointment(int appointmentID, String title, String description, String location, String type,
                                            Timestamp start, Timestamp end, int customerID, int userID, int contactID) {

        try{
            String sqlCreate = "INSERT INTO Appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = JDBC.connection.prepareStatement(sqlCreate);
            preparedStatement.setInt(1, appointmentID);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, location);
            preparedStatement.setString(5, type);
            preparedStatement.setTimestamp(6, start);
            preparedStatement.setTimestamp(7, end);
            preparedStatement.setInt(8, customerID);
            preparedStatement.setInt(9, userID);
            preparedStatement.setInt(10, contactID);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    public static void updateAppointment(int appointmentID, String title, String description, String location, String type,
                                         Timestamp start, Timestamp end, int customerID, int userID, int contactID) {

        try {
            String sqlUpdate = "UPDATE appointments SET (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) WHERE Appointment_ID = ?";

            preparedStatement = JDBC.connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, start);
            preparedStatement.setTimestamp(6, end);
            preparedStatement.setInt(7, customerID);
            preparedStatement.setInt(8, userID);
            preparedStatement.setInt(9, contactID);
            preparedStatement.setInt(10, appointmentID);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteAppointment(int appointmentID) {
        try {
            String sqlDelete = "DELETE FROM appointments WHERE Appointment_ID = ?";

            preparedStatement = JDBC.connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, appointmentID);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }
}
