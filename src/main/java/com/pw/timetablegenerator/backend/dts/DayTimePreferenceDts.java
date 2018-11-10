package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.common.DayTime;
import com.pw.timetablegenerator.backend.entity.Course;

public class DayTimePreferenceDts extends PreferenceDts {

    private DayTime preferenceDayTime;

    public DayTimePreferenceDts(DayTime dayTime, Integer force){
        this.preferenceDayTime = dayTime;
        super.setForce(force);
    }

    public Integer calculatePreferenceForce(Course course){
        final DayTime actualDayTime = DayTime.getDayTime(course.getCourseStartTime());
        if(actualDayTime == preferenceDayTime){
            return getForce();
        }
        return getNoForce();
    }
}
