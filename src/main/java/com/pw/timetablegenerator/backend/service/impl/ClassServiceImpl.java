package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.ClassRepository;
import com.pw.timetablegenerator.backend.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<Class> findByOwner(User owner) {
        return classRepository.findByOwner(owner);
    }

    @Override
    public Class saveClass(Class c) {
        return classRepository.save(c);
    }

    @Override
    public void deleteClass(Class c) {
        classRepository.delete(c);
    }
}
