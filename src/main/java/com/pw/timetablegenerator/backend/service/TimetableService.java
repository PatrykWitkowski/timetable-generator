package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

public interface TimetableService extends Serializable {

    List<Timetable> findTimetables(@NonNull User user, String value);

    Timetable findByTimetableId(long id);

    void delete(@NonNull Timetable timetable);

    void save(@NonNull Timetable timetable);

}
