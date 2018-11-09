package com.pw.timetablegenerator.backend.common;

import com.pw.timetablegenerator.backend.entity.properties.User_;

public enum Gender {
    MALE(User_.MALE),
    FEMALE(User_.FEMALE);

    private String property;

    Gender(String property){
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
