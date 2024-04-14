package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

/**
 * Functional Interface for displaying Alerts to User.
 * Intended to be used in catch clauses for Custom Exceptions.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
//public interface ShowAlert{
public interface AlertInterface {

    /**
     * Lambda 1, Displays an alert to the User.
     * Justification for Lambda for this method is it allows the programmer to change the alert type and add additional
     * functions to the alert if necessary.
     * Intended to be used in the catch clause of a custom Exception.
     * The title will typically be the name of the exception thrown and the message param displays a simple message
     * to the user why the User cannot proceed. The User cannot continue until confirming in the prompt.
     *
     * @param s1 The title of the alert to display
     * @param s2 The message of the alert to display
     */
    void showAlert(String s1, String s2);
}
