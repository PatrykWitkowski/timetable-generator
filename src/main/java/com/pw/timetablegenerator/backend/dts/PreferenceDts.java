package com.pw.timetablegenerator.backend.dts;

import com.pw.timetablegenerator.backend.entity.Course;
import lombok.Getter;

@Getter
public abstract class PreferenceDts {

    @Getter
    private static final Integer noForce = 0;

    private Integer force;

    public PreferenceDts(Integer force) {
        this.force = force;
    }

    public Integer calculatePreferenceForce(Course course){
        return preferencePreconditions(course) ? getForce() : getNoForce();
    }

    protected abstract boolean preferencePreconditions(Course course);

}
