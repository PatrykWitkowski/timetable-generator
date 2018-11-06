package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ClassRepository extends JpaRepository<Class, Long>, Serializable {
}
