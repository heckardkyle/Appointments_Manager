package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

import javafx.scene.control.Alert;

/**
 * Interface for displaying Alerts to User.
 * Intended to be used in catch clauses for Custom Exceptions.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public interface ShowAlert{

    /**
     * Displays an alert to the User.
     * Intended to be used in the catch clause of a custom Exception.
     * The title will typically be the name of the exception thrown and the message param displays a simple message
     * to the user why the User cannot proceed. The User cannot continue until confirming in the prompt.
     *
     * @param title The title of the alert to display
     * @param message The message of the alert to display
     */
    static void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
