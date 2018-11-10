package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.common.ParityOfTheWeek;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;

import java.util.Objects;

public class ClassParityWeekPreferenceDts extends PreferenceDts {

    private ParityOfTheWeek preferenceParityOfTheWeek;
    private Class preferenceClass;

    public ClassParityWeekPreferenceDts(Class c, ParityOfTheWeek parityOfTheWeek, Integer force) {
        super(force);
        this.preferenceClass = c;
        this.preferenceParityOfTheWeek = parityOfTheWeek;
    }

    @Override
    protected boolean preferencePreconditions(Course course) {
        return preferenceClass.getCourses().stream()
                .filter(c -> Objects.equals(c, course))
                .findFirst()
                .filter(c -> c.getParityOfTheWeek() == preferenceParityOfTheWeek)
                .isPresent();
    }
}
