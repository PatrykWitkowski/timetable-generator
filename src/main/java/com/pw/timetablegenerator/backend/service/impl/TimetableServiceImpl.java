package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.TimetableRepository;
import com.pw.timetablegenerator.backend.service.TimetableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Override
    public List<Timetable> findTimetables(User user, String value) {
        List<Timetable> timetables = timetableRepository.findByOwner(user);

        if(StringUtils.isNotBlank(value)){
            List<Timetable> filteredTimetables = filterTimetablesByName(timetables, value);
            filteredTimetables = filterTimetablesByStatus(timetables, filteredTimetables, value);
            timetables = filteredTimetables;
        }

        return timetables;
    }

    private List<Timetable> filterTimetablesByStatus(List<Timetable> timetables,
                                                     List<Timetable> filteredTimetables, String value) {
        if(filteredTimetables.isEmpty()){
            filteredTimetables = timetables.stream()
                    .filter(timetable -> StringUtils.containsIgnoreCase(timetable.getTimetableType().toString(), value))
                    .collect(Collectors.toList());
        }
        return filteredTimetables;
    }

    private List<Timetable> filterTimetablesByName(List<Timetable> timetables, String value) {
        return timetables.stream()
                .filter(timetable -> StringUtils.containsIgnoreCase(timetable.getName(), value))
                .collect(Collectors.toList());
    }

    @Override
    public Timetable findByTimetableId(long id) {
        return timetableRepository.findByTimetableId(id);
    }

    @Override
    public void delete(Timetable timetable) {
        timetableRepository.delete(timetable);
    }

    @Override
    public void save(Timetable timetable) {
        timetableRepository.save(timetable);
    }
}
