package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

import javafx.scene.control.Alert;

public interface ShowAlert{

    static void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
