package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.ClassRepository;
import com.pw.timetablegenerator.backend.jpa.EnrollmentGroupRepository;
import com.pw.timetablegenerator.backend.service.EnrollmentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentGroupImpl implements EnrollmentGroupService {

    @Autowired
    private EnrollmentGroupRepository enrollmentGroupRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<EnrollmentGroup> findEnrollmentGroups(User user, String value) {
        return enrollmentGroupRepository.findByOwner(user);
    }

    @Override
    public EnrollmentGroup saveEnrollmentGroup(EnrollmentGroup enrollmentGroup) {
        return enrollmentGroupRepository.save(enrollmentGroup);
    }

    @Override
    public void deleteEnrollmentGroup(EnrollmentGroup enrollmentGroup) {
        enrollmentGroupRepository.delete(enrollmentGroup);
    }

    @Override
    public EnrollmentGroup findEnrollmentGroupById(Long id) {
        return enrollmentGroupRepository.findById(id).orElse(null);
    }
}
