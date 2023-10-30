package com.c195_software_ii__advanced_java_concepts_pa.Models;


/**
 * Class for creating Appointment objects.
 * Appointment objects are used for displaying in a tableview on the appointments screen for user to view.
 * Appointment objects require a customerID from the Customer Class and the userID from the User Class.
 * Appointment objects will be created as they are retrieved from the database and can be used to update the
 * database as well as be deleted from the database. The appointments for a specific customer must be deleted
 * before said Customer can be deleted.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see User
 * @see Customer
 * @see com.c195_software_ii__advanced_java_concepts_pa.DAO.AppointmentDBImpl
 */
public class Appointment {
    private int    appointmentID;
    private String title;
    private String description;
    private String type;
    private String startTime;
    private String endTime;
    private int    customerID;
    private int    userID;

    /**
     * Default Constructor for Appointment objects
     * @param appointmentID the appointmentID to be set
     * @param title         the appointment title to be set
     * @param description   the appointment description to be set
     * @param type          the appointment type to be set
     * @param startTime     the appointment start time to be set
     * @param endTime       the appointment end time to be set
     * @param customerID    the associated customerID to be set
     * @param userID        the associated userID to be set
     */
    public Appointment(int appointmentID, String title, String description, String type, String startTime, String endTime, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title         = title;
        this.description   = description;
        this.type          = type;
        this.startTime     = startTime;
        this.endTime       = endTime;
        this.customerID    = customerID;
        this.userID        = userID;
    }

    /* --Getters-- */

    /** @return <code>appointmentID</code> */
    public int getAppointmentID() { return appointmentID; }

    /** @return <code>title</code> */
    public String getTitle() { return title; }

    /** @return <code>description</code> */
    public String getDescription() { return description; }

    /** @return <code>type</code> */
    public String getType() { return type; }

    /** @return <code>startTime</code> */
    public String getStartTime() { return startTime; }

    /** @return <code>endTime</code> */
    public String getEndTime() { return endTime; }

    /** @return <code>customerID</code> */
    public int getCustomerID() { return customerID; }

    /** @return <code>userID</code> */
    public int getUserID() { return userID; }

    /* --Setters-- */

    /** @param appointmentID the appointmentID to be set */
    public void setAppointmentID(int appointmentID) { this.appointmentID = appointmentID; }

    /** @param title the appointment title to be set */
    public void setTitle(String title) { this.title = title; }

    /** @param description the appointment description to be set */
    public void setDescription(String description) { this.description = description; }

    /** @param type the appointment type to be set */
    public void setType(String type) { this.type = type; }

    /** @param startTime the appointment start time to be set */
    public void setStartTime(String startTime) { this.startTime = startTime; }

    /** @param endTime the appointment end time to be set */
    public void setEndTime(String endTime) { this.endTime = endTime; }

    /** @param customerID the customerID to associate with the appointment */
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    /** @param userID the userID to associate with the appointment */
    public void setUserID(int userID) { this.userID = userID; }
}
