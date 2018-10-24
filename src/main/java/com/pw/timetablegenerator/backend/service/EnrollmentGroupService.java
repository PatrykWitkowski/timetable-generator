package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.EnrollmentGroup;
import com.pw.timetablegenerator.backend.entity.User;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

public interface EnrollmentGroupService extends Serializable {

    List<EnrollmentGroup> findEnrollmentGroups(@NonNull User user, String value);

}
