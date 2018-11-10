package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.Course;

public class AvoidBreakPreferenceDts extends PreferenceDts {

    public AvoidBreakPreferenceDts(Integer force) {
        super(force);
    }

    @Override
    protected boolean preferencePreconditions(Course course) {
        //TODO: implement
        return false;
    }
}
