package com.pw.timetablegenerator.backend.common;

import com.pw.timetablegenerator.backend.entity.properties.Course_;

public enum ParityOfTheWeek {
    EVEN(Course_.PARITY_EVEN),
    ODD(Course_.PARITY_ODD),
    WEEKLY(Course_.PARITY_WEEKLY);

    private String property;

    ParityOfTheWeek(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
