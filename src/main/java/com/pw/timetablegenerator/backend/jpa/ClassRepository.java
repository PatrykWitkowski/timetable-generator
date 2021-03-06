package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.Class;
import com.pw.timetablegenerator.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long>, Serializable {

    List<Class> findByOwner(User owner);

}
