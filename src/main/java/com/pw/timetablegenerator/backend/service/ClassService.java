package com.pw.timetablegenerator.backend.service;

import com.pw.timetablegenerator.backend.entity.Class;
import lombok.NonNull;

import java.io.Serializable;

public interface ClassService extends Serializable {

    Class saveClass(@NonNull Class c);

    void deleteClass(@NonNull Class c);

}
