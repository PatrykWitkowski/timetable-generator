package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;

import java.time.DayOfWeek;
import java.util.Objects;

public class ClassOnDayPreferenceDts extends PreferenceDts {

    private DayOfWeek preferenceDay;
    private Class preferenceClass;

    public ClassOnDayPreferenceDts(Class actualClass, DayOfWeek day, Integer force) {
        super(force);
        this.preferenceDay = day;
        this.preferenceClass = actualClass;
    }

    @Override
    protected boolean preferencePreconditions(Course course) {
       return preferenceClass.getCourses().stream()
                .filter(c -> Objects.equals(c, course))
                .findFirst()
                .filter(course1 -> course1.getCourseDay() == preferenceDay)
                .isPresent();
    }
}
