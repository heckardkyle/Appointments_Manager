package com.c195_software_ii__advanced_java_concepts_pa.Models;

/**
 * Class for creating Country Objects.
 * Country Objects are used for ComboBoxes when adding and updating customers. The Country Object
 * is necessary for dictating which FirstLevelDivisions appear in the Division ComboBox.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see FirstLevelDivision
 */
public class Country {

    /* --Members-- */

    private int    countryID;
    private String countryName;

    /* --Constructors-- */

    /**
     * Default Constructor for Country Object
     *
     * @param countryID   the countryID to be set
     * @param countryName the countryName to be set
     */
    public Country (int countryID, String countryName) {

        this.countryID   = countryID;
        this.countryName = countryName;
    }

    /* --Getters-- */

    /** Gets the countryID.
     *  @return <code>countryID</code> */
    public int getCountryID() { return countryID; }

    /** Gets the countryName.
     *  @return <code>countryName</code> */
    public String getCountryName() { return countryName; }

    /**
     * Used for ComboBoxes to display the countryNames
     * @return <code>countryName</code>
     */
    public String toString() { return countryName; }

    /* --Setters-- */

    /** Sets the countryID.
     *  @param countryID */
    public void setCountryID(int countryID) { this.countryID = countryID; }

    /** Sets the countryName.
     *  @param countryName */
    public void setCountryName(String countryName) { this.countryName = countryName; }
}
