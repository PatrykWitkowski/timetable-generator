package com.pw.timetablegenerator.backend.service.impl;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.User;
import com.pw.timetablegenerator.backend.jpa.ClassRepository;
import com.pw.timetablegenerator.backend.jpa.EnrollmentGroupRepository;
import com.pw.timetablegenerator.backend.service.EnrollmentGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentGroupImpl implements EnrollmentGroupService {

    @Autowired
    private EnrollmentGroupRepository enrollmentGroupRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public List<EnrollmentGroup> findEnrollmentGroups(User user, String value) {
        List<EnrollmentGroup> enrollmentGroups = enrollmentGroupRepository.findByOwner(user);

        if(StringUtils.isNotBlank(value)){
            return enrollmentGroups.stream()
                    .filter(e -> StringUtils.containsIgnoreCase(e.getName(), value))
                    .collect(Collectors.toList());
        }
        return enrollmentGroups;
    }

    @Override
    public EnrollmentGroup saveEnrollmentGroup(EnrollmentGroup enrollmentGroup) {
        return enrollmentGroupRepository.save(enrollmentGroup);
    }

    @Override
    public void deleteEnrollmentGroup(EnrollmentGroup enrollmentGroup) {
        enrollmentGroupRepository.delete(enrollmentGroup);
    }

}
