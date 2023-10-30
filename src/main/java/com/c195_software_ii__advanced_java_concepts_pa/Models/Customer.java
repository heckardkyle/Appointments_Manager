package com.c195_software_ii__advanced_java_concepts_pa.Models;


/**
 * Class for creating Customer objects.
 * Customer objects can be used to view, update, and delete customers.
 * The customerID is also necessary to create appointment objects from the Appointment class.
 * Customers will be retrieved from database, and can be used to update customer in database and delete customer
 * from database.
 *
 * @author Kyle Heckard
 * @version 1.0
 * @see Appointment
 * @see com.c195_software_ii__advanced_java_concepts_pa.DAO.CustomerDBImpl
 */
public class Customer {
    private int    customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;

    /* --Constructors-- */

    /**
     * Default Constructor for Customer object
     * @param customerID   the customerID to be set
     * @param customerName the customerName to be set
     * @param address      the customers address to be set
     * @param postalCode   the customers postalCode to be set
     * @param phoneNumber  the customers phoneNumber to be set
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber) {
        this.customerID   = customerID;
        this.customerName = customerName;
        this.address      = address;
        this.postalCode   = postalCode;
        this.phoneNumber  = phoneNumber;
    }

    /* --Getters-- */

    /** @return <code>customerID</code> */
    public int getCustomerID() { return customerID; }

    /** @return <code>customerName</code> */
    public String getCustomerName() { return customerName; }

    /** @return <code>address</code> */
    public String getAddress() { return address; }

    /** @return <code>postalCode</code> */
    public String getPostalCode() { return postalCode; }

    /** @return <code>phoneNumber</code> */
    public String getPhoneNumber() { return phoneNumber; }

    /* --Setters-- */

    /** @param customerID the customerID to be set */
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    /** @param customerName the customerName to be set */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /** @param address the customers address to be set */
    public void setAddress(String address) { this.address = address; }

    /** @param postalCode the customers postalCode to be set */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /** @param phoneNumber the customers phoneNumber to be set */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
