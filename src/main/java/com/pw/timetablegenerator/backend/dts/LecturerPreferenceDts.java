package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.Lecturer;
import org.apache.commons.lang3.StringUtils;

public class LecturerPreferenceDts extends PreferenceDts {

    private Lecturer preferenceLecturer;

    public LecturerPreferenceDts(Lecturer lecturer, Integer force) {
        super(force);
        this.preferenceLecturer = lecturer;
    }

    @Override
    protected boolean preferencePreconditions(Course course) {
        final String actualLecturer = course.getLecturer().getName();
        return StringUtils.equalsIgnoreCase(actualLecturer, preferenceLecturer.getName());
    }
}
