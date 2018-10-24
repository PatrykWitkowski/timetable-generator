package com.pw.timetablegenerator.backend.jpa;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface EnrollmentGroupRepository extends JpaRepository<EnrollmentGroup, Long>, Serializable {

    List<EnrollmentGroup> findByOwner(User owner);
}
