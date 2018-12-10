package com.pw.timetablegenerator.backend.service.impl;

import com.google.common.collect.Lists;
import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.ClassRepository;
import com.pw.timetablegenerator.backend.jpa.CourseRepository;
import com.pw.timetablegenerator.backend.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<Course> findCourses(User user, String value) {
        final List<Class> byOwner = classRepository.findByOwner(user);
        final Set<Course> courses = byOwner.stream()
                .flatMap(c -> courseRepository.findByClassOwner(c).stream())
                .collect(Collectors.toSet());

        if (StringUtils.isNotBlank(value)) {
            return courses.stream()
                    .filter(e -> StringUtils.containsIgnoreCase(e.getName(), value))
                    .collect(Collectors.toList());
        }
        return Lists.newArrayList(courses);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }
}