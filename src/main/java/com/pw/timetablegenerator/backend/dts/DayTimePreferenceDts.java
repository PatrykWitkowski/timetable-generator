package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.common.DayTime;
import com.pw.timetablegenerator.backend.entity.Course;

public class DayTimePreferenceDts extends PreferenceDts {

    private DayTime preferenceDayTime;

    public DayTimePreferenceDts(DayTime dayTime, Integer force){
        super(force);
        this.preferenceDayTime = dayTime;
    }

    @Override
    protected boolean preferencePreconditions(Course course) {
        final DayTime actualDayTime = DayTime.getDayTime(course.getCourseStartTime());
        return actualDayTime == preferenceDayTime;
    }
}
