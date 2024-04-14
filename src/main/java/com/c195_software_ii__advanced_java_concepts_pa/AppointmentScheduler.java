package com.c195_software_ii__advanced_java_concepts_pa;

import com.c195_software_ii__advanced_java_concepts_pa.DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Starts AppointmentScheduler application.
 * Opens a connection to the database and opens a window on the UserLogin Page.
 * Closes connection to database when the application is closed.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class AppointmentScheduler extends Application {

    /**
     * Starts application.
     * Opens a window on the UserLoginForm page.
     * @param stage to set up scene
     * @throws IOException Generic exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppointmentScheduler.class.getResource("UserLoginForm.fxml"));
        ResourceBundle rb = ResourceBundle.getBundle("UserLogin", Locale.getDefault());
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle(rb.getString("title"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens Connection with database and launches application.
     * Closes Connection with database upon ending application.
     * @param args Generic args parameter
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}