package com.c195_software_ii__advanced_java_concepts_pa.Models;

/**
 * Class for creating FirstLevelDivision Objects.
 * FirstLevelDivision Objects are used for ComboBoxes when adding and updating customers and is displayed in the
 * Customer TableView. The FirstLevelDivision objects that appear in the ComboBox are dictated by which Country is
 * selected and are related by their countryID.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Country
 * @see Customer
 */
public class FirstLevelDivision {

    /* --Members-- */

    private int    divisionID;
    private String divisionName;
    private int    countryID;

    /* --Constructors-- */

    /**
     * Default Constructor for the FirstLevelDivision Object.
     *
     * @param divisionID   The divisionID to be set.
     * @param divisionName The divisionName to be set.
     * @param countryID    The associated countryID to be set.
     */
    public FirstLevelDivision (int divisionID, String divisionName, int countryID) {

        this.divisionID   = divisionID;
        this.divisionName = divisionName;
        this.countryID    = countryID;
    }

    /* --Getters-- */

    /** Gets the divisionID.
     *  @return <code>divisionID</code> */
    public int getDivisionID() { return divisionID; }

    /** Gets the divisionName.
     *  @return <code>divisionName</code> */
    public String getDivisionName() { return divisionName; }

    /** Gets the associated countryID.
     *  @return <code>countryID</code> */
    public int getCountryID() { return countryID; }

    /** Used for Division ComboBoxes to display the divisionNames
     *  @return <code>divisionName</code> */
    public String toString() { return divisionName; }

    /* --Setters-- */

    /** Sets the divisionID.
     *  @param divisionID the divisionID to be set */
    public void setDivisionID(int divisionID) { this.divisionID = divisionID; }

    /** Sets the divisionName.
     *  @param divisionName the divisionName to be set */
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    /** Sets the division's associated countryID.
     *  @param countryID the countryID to associate */
    public void setCountryID(int countryID) { this.countryID = countryID; }
}
