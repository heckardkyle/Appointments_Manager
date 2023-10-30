package com.c195_software_ii__advanced_java_concepts_pa.Models;


/**
 * Class for creating User objects.
 * This is for the purpose of logging into the application and tracking login activity.
 * The userID is necessary to create Appointments
 * Objects will be created as they are retrieved from the Database.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Appointment
 * @see com.c195_software_ii__advanced_java_concepts_pa.DAO.UserDBImpl
 */
public class User {
    private int    userID;
    private String userName;
    private String password;

    /* --Constructors-- */

    /**
     * Default constructor for User object
     * @param userID   the userID to be set
     * @param userName the userName to be set
     * @param password the password to be set
     */
    public User(int userID, String userName, String password) {
        this.userID   = userID;
        this.userName = userName;
        this.password = password;
    }

    /* --Getters-- */

    /** @return <code>userID</code> */
    public int getUserID() { return userID; }

    /** @return <code>userName</code> */
    public String getUserName() { return userName; }

    /** @return <code>password</code> */
    public String getPassword() { return password; }

    /*  --Setters-- */

    /** @param userID the userID to set */
    public void setUserID(int userID) { this.userID = userID; }

    /** @param userName the userName to set */
    public void setUserName(String userName) { this.userName = userName; }

    /** @param password the password to set */
    public void setPassword(String password) { this.password = password; }
}
