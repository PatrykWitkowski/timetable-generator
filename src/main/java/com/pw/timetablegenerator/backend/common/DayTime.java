package com.pw.timetablegenerator.backend.common;

import com.pw.timetablegenerator.backend.entity.properties.Timetable_;

import java.time.LocalTime;

public enum DayTime {
    MORNING(Timetable_.DAY_TIME_MORNING),
    AFTERNOON(Timetable_.DAY_TIME_AFTERNOON),
    EVENING(Timetable_.DAY_TIME_EVENING);

    public static DayTime getDayTime(LocalTime time){
        if(time.isBefore(LocalTime.parse("12:00"))){
            return MORNING;
        } else if(time.isBefore(LocalTime.parse("17:00"))){
            return AFTERNOON;
        } else if(time.isBefore(LocalTime.parse("21:00"))){
            return EVENING;
        }
        return null;
    }

    private String property;

    DayTime(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
