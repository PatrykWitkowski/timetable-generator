package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, Serializable {

    List<Course> findByClassOwner(Class classOwner);

}
