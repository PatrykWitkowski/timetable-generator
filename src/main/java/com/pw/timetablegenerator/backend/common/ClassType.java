package com.pw.timetablegenerator.backend.common;

import com.pw.timetablegenerator.backend.entity.properties.Class_;

public enum ClassType {
    LECTURE(Class_.TYPE_LECTURE),
    EXERCISE(Class_.TYPE_EXERCISE),
    LABORATORY(Class_.TYPE_LABORATORY),
    PROJECT(Class_.TYPE_PROJECT),
    SEMINAR(Class_.TYPE_SEMINAR);

    private String property;

    ClassType(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
