package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import com.pw.timetablegenerator.backend.entity.User;
import lombok.NonNull;

import java.util.List;

public interface CourseService {

    List<Course> findCourses(@NonNull User user, String value);

    List<Course> findByClassOwner(Class classOwner);

    Course saveCourse(@NonNull Course course);

    void deleteCourse(@NonNull Course course);
}
