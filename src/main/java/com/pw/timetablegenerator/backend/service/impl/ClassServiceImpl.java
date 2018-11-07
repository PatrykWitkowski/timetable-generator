package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.ClassRepository;
import com.pw.timetablegenerator.backend.service.ClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<Class> findByOwner(User owner) {
        return classRepository.findByOwner(owner);
    }

    @Override
    public List<Class> findClasses(User user, String value) {
        List<Class> classes = classRepository.findByOwner(user);

        if(StringUtils.isNotBlank(value)){
            return classes.stream()
                    .filter(e -> StringUtils.containsIgnoreCase(e.getName(), value))
                    .collect(Collectors.toList());
        }
        return classes;
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
