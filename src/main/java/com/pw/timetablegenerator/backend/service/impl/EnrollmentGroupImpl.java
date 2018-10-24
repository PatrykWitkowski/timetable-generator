package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.EnrollmentGroupRepository;
import com.pw.timetablegenerator.backend.service.EnrollmentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentGroupImpl implements EnrollmentGroupService {

    @Autowired
    private EnrollmentGroupRepository enrollmentGroupRepository;

    @Override
    public List<EnrollmentGroup> findEnrollmentGroups(User user, String value) {
        return enrollmentGroupRepository.findByOwner(user);
    }
}
