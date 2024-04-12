package com.c195_software_ii__advanced_java_concepts_pa.Models;

/**
 * Class for creating Contact Objects.
 * Contact Objects are used for Combo Boxes when adding and updating appointments and in the Reports Tab.
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

    /**
     * Default Constructor for Contact Objects.
     *
     * @param contactID    the contactID to be set
     * @param contactName  the contactName to be set
     * @param contactEmail the contactEmail to be set
     */
    public Contact(int contactID, String contactName, String contactEmail) {

        this.contactID    = contactID;
        this.contactName  = contactName;
        this.contactEmail = contactEmail;
    }

    /* --Getters-- */

    /** Gets the contactID.
     *  @return <code>contactID</code> */
    public int getContactID() { return contactID; }

    /** Gets the contactName.
     *  @return <code>contactName</code> */
    public String getContactName() { return contactName; }

    /** Gets the contactEmail.
     *  @return <code>contactEmail</code> */
    public String getContactEmail() { return contactEmail; }

    /** Used for ComboBoxes to display contactNames.
     *  @return <code>contactName</code> */
    public String toString() { return contactName; }

    /* --Setters-- */

    /** Sets the contactID.
     *  @param contactID the contactID to be set */
    public void setContactID(int contactID) { this.contactID = contactID; }

    /** Sets the contactName.
     *  @param contactName the contactName to be set */
    public void setContactName(String contactName) { this.contactName = contactName; }

    /** Sets the contactEmail.
     *  @param contactEmail the contactEmail to be set */
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}
