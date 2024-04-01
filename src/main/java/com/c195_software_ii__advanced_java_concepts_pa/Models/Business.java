package com.c195_software_ii__advanced_java_concepts_pa.Models;

import java.time.*;
import java.util.EnumSet;
import java.util.Set;

public class Business {

    //private ObservableList<Integer> businessDaysOfWeekOpen = FXCollections.observableArrayList();
    private Set<DayOfWeek> businessDaysOfWeekOpen;
    private ZoneId businessZoneID;
    private ZonedDateTime businessOpenTime;
    private ZonedDateTime businessClosingTime;

    public Business () {
        Set<DayOfWeek> daysOfWeekOpen = EnumSet.range(DayOfWeek.MONDAY,DayOfWeek.FRIDAY);
        this.businessDaysOfWeekOpen = daysOfWeekOpen;
        // 8am to 10pm
        LocalTime openTime = LocalTime.of(8,0);
        LocalTime closeTime = LocalTime.of(22,0);
        this.businessZoneID = ZoneId.of("America/New_York");
        this.businessOpenTime = openTime.atDate(LocalDate.now()).atZone(businessZoneID);
        this.businessClosingTime = closeTime.atDate(LocalDate.now()).atZone(businessZoneID);
    }

    public Set<DayOfWeek> getBusinessDaysOfWeekOpen() {
        return businessDaysOfWeekOpen;
    }

    public ZoneId getBusinessZoneID() {
        return businessZoneID;
    }

    public ZonedDateTime getBusinessOpenTime() {
        return businessOpenTime;
    }

    public ZonedDateTime getBusinessClosingTime() {
        return businessClosingTime;
    }

    public void setBusinessDaysOfWeekOpen(Set<DayOfWeek> businessDaysOfWeekOpen) {
        this.businessDaysOfWeekOpen = businessDaysOfWeekOpen;
    }

    public void setBusinessZoneID(ZoneId businessZoneID) {
        this.businessZoneID = businessZoneID;
    }

    public void setBusinessOpenTime(ZonedDateTime businessOpenTime) {
        this.businessOpenTime = businessOpenTime;
    }

    public void setBusinessClosingTime(ZonedDateTime businessClosingTime) {
        this.businessClosingTime = businessClosingTime;
    }
}
