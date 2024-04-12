package com.c195_software_ii__advanced_java_concepts_pa.Models;

import java.time.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * Class for creating Business Objects.
 * Business Objects are used for checking created/updated appointment start and end times against business hours
 * of operation.
 *
 * @author Kyle Heckard
 * @version 1.0
 */
public class Business {

    /* --Members-- */

    private Set<DayOfWeek> businessDaysOfWeekOpen;
    private ZoneId         businessZoneID;
    private ZonedDateTime  businessOpenTime;
    private ZonedDateTime  businessClosingTime;

    /* --Constructors-- */

    /**
     * Default Constructor for Business Object
     */
    public Business () {

        // Default business hours of operation
        Set<DayOfWeek> daysOfWeekOpen = EnumSet.range(DayOfWeek.MONDAY,DayOfWeek.FRIDAY); // Open Mon thru Fri
        String         operatingZone  = "America/New_York"; // Eastern Time
        LocalTime      openTime       = LocalTime.of(8, 0); // Opens at 8AM
        LocalTime      closeTime      = LocalTime.of(22,0); // Closes at 10PM

        this.businessDaysOfWeekOpen = daysOfWeekOpen;
        this.businessZoneID         = ZoneId   .of(operatingZone);
        this.businessOpenTime       = openTime .atDate(LocalDate.now()).atZone(businessZoneID);
        this.businessClosingTime    = closeTime.atDate(LocalDate.now()).atZone(businessZoneID);
    }

    /* --Getters-- */

    /** Gets the DaysOfWeek the Business operates.
     *  @return <code>businessDaysOfWeekOpen</code> */
    public Set<DayOfWeek> getBusinessDaysOfWeekOpen() { return businessDaysOfWeekOpen; }

    /** Gets the Time Zone the Business operates in.
     *  @return <code>businessZoneID</code> */
    public ZoneId getBusinessZoneID() { return businessZoneID; }

    /** Gets the Open Time of the Business.
     *  @return <code>businessOpenTime</code> */
    public ZonedDateTime getBusinessOpenTime() { return businessOpenTime; }

    /** Gets the Close Time of the Business
     *  @return <code>businessClosingTime</code> */
    public ZonedDateTime getBusinessClosingTime() { return businessClosingTime; }

    /* --Setters-- */

    /** Sets the DaysOfWeek the Business operates.
     *  @param businessDaysOfWeekOpen The operating DaysOfWeek to set */
    public void setBusinessDaysOfWeekOpen(Set<DayOfWeek> businessDaysOfWeekOpen) {
        this.businessDaysOfWeekOpen = businessDaysOfWeekOpen;
    }

    /** Sets the Time Zone the Business operates in.
     *  @param businessZoneID The Time Zone to set */
    public void setBusinessZoneID(ZoneId businessZoneID) { this.businessZoneID = businessZoneID; }

    /** Sets the Business Open Time.
     *  @param businessOpenTime The Open Time to set */
    public void setBusinessOpenTime(ZonedDateTime businessOpenTime) { this.businessOpenTime = businessOpenTime; }

    /** Sets the Business Close Time.
     *  @param businessClosingTime The Close Time to set */
    public void setBusinessClosingTime(ZonedDateTime businessClosingTime) {
        this.businessClosingTime = businessClosingTime;
    }
}
