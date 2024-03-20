package com.c195_software_ii__advanced_java_concepts_pa.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;

public class Business {

    private ObservableList<Integer> businessDaysOfWeekOpen = FXCollections.observableArrayList();
    private ZonedDateTime businessOpenTime;
    private ZonedDateTime businessClosingTime;

    public Business () {
        this.businessDaysOfWeekOpen.addAll(1,2,3,4,5);
        // 8am to 10pm
        LocalTime openTime = LocalTime.of(8,0);
        LocalTime closeTime = LocalTime.of(22,0);
        ZoneId zone = ZoneId.of("America/New_York");
        this.businessOpenTime = openTime.atDate(LocalDate.now()).atZone(zone);
        this.businessClosingTime = closeTime.atDate(LocalDate.now()).atZone(zone);
    }

    public ObservableList<Integer> getBusinessDaysOfWeekOpen() {
        return businessDaysOfWeekOpen;
    }

    public ZonedDateTime getBusinessOpenTime() {
        return businessOpenTime;
    }

    public ZonedDateTime getBusinessClosingTime() {
        return businessClosingTime;
    }

    public void setBusinessDaysOfWeekOpen(ObservableList<Integer> businessDaysOfWeekOpen) {
        this.businessDaysOfWeekOpen = businessDaysOfWeekOpen;
    }

    public void setBusinessOpenTime(ZonedDateTime businessOpenTime) {
        this.businessOpenTime = businessOpenTime;
    }

    public void setBusinessClosingTime(ZonedDateTime businessClosingTime) {
        this.businessClosingTime = businessClosingTime;
    }
}
