package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Holds LoginAlert method to alert User of upcoming appointment upon logging into application.
 * @author Kyle Heckard
 * @version 1.0
 */
public class LoginAlert {

    /**
     * Alerts User of upcoming appointment occurring within 15 minutes of User logging into Application.
     * The method checks for every appointment that is within 15 minutes of User logging into application. The String
     * that is displayed varies depending on the number of appointments and will display each appointment, it's ID,
     * and the time it starts. The information is displayed in an Information Alert box and will not proceed until
     * the User accepts.
     *
     * @throws SQLException
     */
    public static void LoginAlert() throws SQLException {

        // Holds appointments that meet criteria
        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();

        // Format to display appointment start time in
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a v");

        // Declare alertString for Alert
        String alertString;

        // For each appointment
        for(Appointment appointment : AppointmentDBImpl.getAllAppointments()) {

            // if appointment start time is within 15 minutes of now, add to upcomingAppointments ObservableList
            if ((ChronoUnit.MINUTES.between(ZonedDateTime.now(ZoneId.systemDefault()), appointment.getDateTimeStart()) >= 0)
                    && (ChronoUnit.MINUTES.between(ZonedDateTime.now(ZoneId.systemDefault()), appointment.getDateTimeStart()) <= 15)) {
                upcomingAppointments.add(appointment);
            }
        }

        // If no upcoming appointments
        if (upcomingAppointments.isEmpty()) {
            alertString = "There are no upcoming appointments";
        }

        // If only 1 upcoming appointment
        else if (upcomingAppointments.size() == 1) {
            alertString = "There is 1 upcoming appointment:\n"
                    + "Appointment ID: " + upcomingAppointments.get(0).getAppointmentID() + "\n"
                    + "Start time:     " + upcomingAppointments.get(0).getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(format);
        }

        // If many upcoming appointments
        else {
            // Start string
            alertString = "There are " + upcomingAppointments.size() + " upcoming appointments:";

            // track appointment number in list
            int index = 1;

            // Each appointment, display position in list, ID, and start time
            for (Appointment appointment : upcomingAppointments) {
                alertString += "\n\n\tAppointment " + index + "\n"
                        + "Appointment ID: " + appointment.getAppointmentID() + "\n"
                        + "Start time:     " + appointment.getDateTimeStart().withZoneSameInstant(ZoneId.systemDefault()).format(format);
                index += 1;
            }
        }

        //Issue alert Prompt
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertString);
        alert.showAndWait();

        // Clear table...just in case
        upcomingAppointments.clear();
    }
}
