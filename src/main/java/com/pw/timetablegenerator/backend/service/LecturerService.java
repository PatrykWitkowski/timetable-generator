package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.Lecturer;
import lombok.NonNull;

public interface LecturerService {

    Lecturer saveLecturer(@NonNull Lecturer lecturer);

    void deleteLecturer(@NonNull Lecturer lecturer);
}
