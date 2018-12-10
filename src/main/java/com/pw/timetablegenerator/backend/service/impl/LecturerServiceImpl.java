package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import com.pw.timetablegenerator.backend.jpa.LecturerRepository;
import com.pw.timetablegenerator.backend.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public Lecturer saveLecturer(Lecturer lecturer) {
        return lecturerRepository.save(lecturer);
    }

    @Override
    public void deleteLecturer(Lecturer lecturer) {
        lecturerRepository.delete(lecturer);
    }
}
