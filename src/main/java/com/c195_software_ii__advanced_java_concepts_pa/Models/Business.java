package com.c195_software_ii__advanced_java_concepts_pa.Models;

import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Business {

    private ObservableList<Integer> businessDaysOfWeekOpen;
    private ZonedDateTime businessOpenTime;
    private ZonedDateTime businessClosingTime;

    public Business () {
        this.businessDaysOfWeekOpen.addAll(1,2,3,4,5);
        // 8am to 10pm
        LocalDateTime openTime = LocalDateTime.from(LocalTime.of(8, 0));
        LocalDateTime closeTime = LocalDateTime.from(LocalTime.of(22,0));
        ZoneId zone = ZoneId.of("ET");
        this.businessOpenTime = ZonedDateTime.of(openTime, zone);
        this.businessClosingTime = ZonedDateTime.of(closeTime, zone);
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
