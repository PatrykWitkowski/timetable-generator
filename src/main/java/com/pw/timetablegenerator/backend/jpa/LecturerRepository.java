package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface LecturerRepository extends JpaRepository<Lecturer, Long>, Serializable {
}
