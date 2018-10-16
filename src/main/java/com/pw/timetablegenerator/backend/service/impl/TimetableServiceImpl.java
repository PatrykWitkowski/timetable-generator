package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.TimetableRepository;
import com.pw.timetablegenerator.backend.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Override
    public List<Timetable> findTimetables(User user, String value) {
        return timetableRepository.findByOwner(user);
    }

    @Override
    public Timetable findByTimetableId(long id) {
        return timetableRepository.findByTimetableId(id);
    }

    @Override
    public void delete(Timetable timetable) {
        timetableRepository.delete(timetable);
    }
}
