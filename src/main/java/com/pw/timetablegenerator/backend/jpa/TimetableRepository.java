package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable,Long>, Serializable {

    List<Timetable> findByOwner(User owner);

    Timetable findByTimetableId(long id);
}
