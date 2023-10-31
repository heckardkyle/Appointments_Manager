package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl;
import com.c195_software_ii__advanced_java_concepts_pa.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class LoginAlert {

    public static void LoginAlert() throws SQLException {

        LocalDate localDate = LocalDateTime.now(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = LocalDateTime.now(ZoneId.systemDefault()).toLocalTime();

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        allAppointments = AppointmentDBImpl.getAllAppointments();

        String alertString;

        for(Appointment appointment : allAppointments) {

        }

        if ( 1 + 1 == 3) {
            alertString = "";
        }
        else {
            alertString = "There are no upcoming appointments";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertString);
        alert.showAndWait();

        allAppointments.removeAll();
    }

}
