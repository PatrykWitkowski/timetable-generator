package com.pw.timetablegenerator.backend.common;

import com.pw.timetablegenerator.backend.entity.properties.Class_;
import com.pw.timetablegenerator.backend.entity.properties.Course_;
import com.pw.timetablegenerator.backend.entity.properties.Timetable_;

public enum GroupType {
    ENROLLMENT_GROUP(Timetable_.ENROLLMENT),
    CLASS(Class_.CLASS),
    COURSE(Course_.COURSE);

    private String property;

    GroupType(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
