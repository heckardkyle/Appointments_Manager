package com.c195_software_ii__advanced_java_concepts_pa.DAO;

import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentDBImpl {

    static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    static PreparedStatement preparedStatement;
    static ResultSet result;
    static Appointment appointment;
    static String sqlSelect = "SELECT * FROM appointments";

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        preparedStatement = JDBC.connection.prepareStatement(sqlSelect);
        result = preparedStatement.executeQuery();

        while (result.next()) {
            int       appointmentID = result.getInt("Appointment_ID");
            String    title         = result.getString("Title");
            String    description   = result.getString("Description");
            String    location      = result.getString("Location");
            String    type          = result.getString("Type");
            Timestamp startTime     = result.getTimestamp("Start");
            Timestamp endTime       = result.getTimestamp("End");
            int       customerID    = result.getInt("Customer_ID");
            int       userID        = result.getInt("User_ID");
            int       contactID     = result.getInt("Contact_ID");

            appointment = new Appointment(appointmentID,title,description,location,type,startTime,endTime,customerID,userID,contactID);
            allAppointments.add(appointment);
        }
        return allAppointments;
    }
}
