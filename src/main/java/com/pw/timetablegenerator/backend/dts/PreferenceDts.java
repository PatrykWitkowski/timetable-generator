package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.Course;
import lombok.Getter;
import lombok.Setter;

public abstract class PreferenceDts {

    @Getter
    private static final Integer noForce = 0;

    @Getter
    @Setter
    private Integer force;

    public abstract Integer calculatePreferenceForce(Course course);

}
