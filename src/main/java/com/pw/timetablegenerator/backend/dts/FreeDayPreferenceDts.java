package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.Course;

import java.time.DayOfWeek;

public class FreeDayPreferenceDts extends PreferenceDts {

    private DayOfWeek preferenceFreeDay;

    public FreeDayPreferenceDts(DayOfWeek dayOfWeek, Integer force) {
        super(force);
        this.preferenceFreeDay = dayOfWeek;
    }

    @Override
    protected boolean preferencePreconditions(Course course) {
        final DayOfWeek courseDay = course.getCourseDay();
        return courseDay != preferenceFreeDay;
    }
}
