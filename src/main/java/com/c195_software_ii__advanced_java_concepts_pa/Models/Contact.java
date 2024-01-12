package com.c195_software_ii__advanced_java_concepts_pa.Models;

/**
 * Class for creating ContactObjects.
 * Contact Objects are used for Combo Boxes when adding and updating appointments.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class Contact {

    /* --Members-- */

    private int    contactID;
    private String contactName;
    private String contactEmail;

    /* --Constructors-- */

    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID    = contactID;
        this.contactName  = contactName;
        this.contactEmail = contactEmail;
    }

    /* --Getters-- */

    /**
     * Gets the contactID
     * @return <code>contactID</code>
     */
    public int getContactID() { return contactID; }

    /**
     * Gets the contactName
     * @return <code>contactName</code>
     */
    public String getContactName() { return contactName; }

    /**
     * Gets the contactEmail
     * @return <code>contactEmail</code>
     */
    public String getContactEmail() { return contactEmail; }

    /**
     * Used in the AppointmentController for the Contact ComboBox to display contactNames.
     * @return <code>contactName</code>
     */
    public String toString() { return contactName; }

    /* --Setters-- */

    /**
     * Sets the contactID
     * @param contactID
     */
    public void setContactID(int contactID) { this.contactID = contactID; }

    /**
     * Sets the contactName
     * @param contactName
     */
    public void setContactName(String contactName) { this.contactName = contactName; }

    /**
     * Sets the contactEmail
     * @param contactEmail
     */
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}
